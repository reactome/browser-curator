package org.reactome.web.elv.client.manager.ga;

import com.google.gwt.user.client.ui.ToggleButton;
import org.reactome.web.elv.client.center.content.analysis.event.AnalysisCompletedEvent;
import org.reactome.web.elv.client.common.Controller;
import org.reactome.web.elv.client.common.EventBus;
import org.reactome.web.elv.client.common.LocationHelper;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;
import org.reactome.web.elv.client.common.data.model.*;
import org.reactome.web.elv.client.common.model.Path;
import org.reactome.web.elv.client.common.utils.Console;
import org.reactome.web.elv.client.details.model.DetailsTabType;
import org.reactome.web.elv.client.details.tabs.analysis.events.AnalysisTabPathwaySelected;
import uk.ac.ebi.pwp.utils.analytics.client.GATracker;

/**
 * GAManager keeps track of those events that are of interest in order to get statistics of the web application.
 *
 * A part from the "trackPageview" option that GA offers, we track some internal events that have been defined to
 * know how the users use the application. NOTE: trackPageview does not differentiate pages by token (string after #)
 *
 * CATEGORY: The name supplied for the group of objects we want to track.
 * ACTION: A string that is uniquely paired with each category (commonly used to define the type of user interaction).
 * LABEL: An optional string to provide additional dimensions to the event data.
 *
 * There are actions like peptide selection that can be done in more than one place (PCM, PeptideTable or StateManager),
 * using Label we can differentiate the actions in order to know later on what the user use the most when working with
 * the webapp
 *
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings({"PointlessBooleanExpression", "ConstantConditions"})
public class GAManager extends Controller {
    private static final String PREFIX = "\t\t[GAManager] ";

    //Set to true in order to see in the console what the GAManager is doing
    private static final boolean TRACK_GA_MANAGER = true;

    private boolean gaTrackerActive = false;

    private GAManagerState state = new GAManagerState();

    public GAManager(EventBus eventBus) {
        super(eventBus);

        LocationHelper.Location location = LocationHelper.getLocation();
        boolean inHost;
        switch (location){
            case PRODUCTION:
                GATracker.setAccount("UA-42985898-1");
                GATracker.setDomainName("reactome.org");
                inHost = true;
                break;
            case DEV:
                GATracker.setAccount("UA-42985898-2");
                GATracker.setDomainName("oicr.on.ca");
                inHost = true;
                break;
            case CURATOR:
                GATracker.setAccount("UA-42985898-3");
                GATracker.setDomainName("oicr.on.ca");
                inHost = true;
                break;
            default:
                inHost = false;
        }
        if(inHost){
            GATracker.trackPageview();
            this.gaTrackerActive = true;
        }
        if(!this.gaTrackerActive && TRACK_GA_MANAGER && Console.VERBOSE){
            Console.info("[GAManager] set for DEV purposes");
        }
    }

    private void trackEvent(String element, GAAction action, GAModule module){
        if(gaTrackerActive){
            GATracker.trackEvent(element, action.toString(), module.toString());
        }
        if(TRACK_GA_MANAGER && Console.VERBOSE){
            Console.info(PREFIX + "Event tracked: [" + element + ", " + action + ", " + module + "]");
        }
    }

    private void trackEvent(GACategory category, GAAction action, GAModule module){
        if(gaTrackerActive){
            GATracker.trackEvent(category.toString(), action.toString(), module.toString());
        }
        if(TRACK_GA_MANAGER && Console.VERBOSE){
            Console.info(PREFIX + "Event tracked: [" + category + ", " + action + ", " + module + "]");
        }
    }

    private void trackEvent(DatabaseObject databaseObject, GAAction action, GAModule module){
        SchemaClass schemaClass = databaseObject.getSchemaClass();
        if(gaTrackerActive){
            GATracker.trackEvent(schemaClass.toString(), action.toString(), module.toString());
        }
        if(TRACK_GA_MANAGER && Console.VERBOSE){
            Console.info(PREFIX + "Event tracked: [" + schemaClass + ", " + action + ", " + module + "]");
        }
    }

    //############################################################################################//
    //###################################### EVENTS TRACKED ######################################//
    //############################################################################################//

    @Override
    public void onTopBarDiagramKeyButtonToggled(ToggleButton btn) {
        if(state.isDiagramKeyToggledValid()){
            this.trackEvent(GACategory.DIAGRAM_KEY, GAAction.TOGGLED, GAModule.TOP_BAR);
        }
    }

    @Override
    public void onTopBarHierarchyButtonToggled(ToggleButton btn) {
        this.trackEvent(GACategory.HIERARCHY_PANEL, GAAction.TOGGLED, GAModule.EVENT_HIERARCHY);
    }

    @Override
    public void onTopBarDetailsButtonToggled(ToggleButton btn) {
        this.trackEvent(GACategory.DETAILS_PANEL, GAAction.TOGGLED, GAModule.DETAILS);
    }

    @Override
    public void onTopBarSpeciesSelected(Species species) {
        this.trackEvent(GACategory.SPECIES_LIST, GAAction.SELECTED, GAModule.TOP_BAR);
    }

    @Override
    public void onTopBarAnalysisSelected() {
        this.trackEvent(GACategory.ANALYSIS_TOOL, GAAction.SELECTED, GAModule.TOP_BAR);
    }

    @Override
    public void onDetailsPanelTabChanged(DetailsTabType tabType) {
        GAModule tab = GAModule.getCorrespondence(tabType);
        if(tab!=null){
            this.trackEvent(GACategory.DETAILS_PANEL, GAAction.SELECTED, tab);
        }
    }

    @Override
    public void onAnalysisTabPathwaySelected(AnalysisTabPathwaySelected selected) {
//        Species species = selected.getSpecies(); //TODO: Add species tracking
        Pathway diagram = selected.getDiagram();
        Pathway pathway = selected.getPathway();

        Pathway sel = diagram.equals(pathway)?diagram:pathway;
        if(sel!=null && !sel.equals(state.getLastDatabaseObjectSelected())){
            state.setLastDatabaseObjectSelected(sel);
            this.trackEvent(sel, GAAction.SELECTED, GAModule.ANALYSIS_TAB);
        }
    }

    @Override
    public void onAnalysisTabResourceSelected(String resource) {
        this.trackEvent(resource, GAAction.SELECTED, GAModule.ANALYSIS_TAB);
    }

    @Override
    public void onAnalysisCompleted(AnalysisCompletedEvent event) {
        this.trackEvent(GACategory.ANALYSIS_TOOL, GAAction.PERFORMED, GAModule.ANALYSIS_TOOL);
    }

    @Override
    public void onDiagramEntitySelected(DatabaseObject databaseObject) {
        if(!databaseObject.equals(this.state.getLastDatabaseObjectSelected())){
            this.state.setLastDatabaseObjectSelected(databaseObject);
            this.trackEvent(databaseObject, GAAction.SELECTED, GAModule.DIAGRAM);
        }
    }

    @Override
    public void onDiagramFigureSelected(Figure figure){
        if(!figure.equals(this.state.getLastFigureVisited())){
            this.state.setLastFigureVisited(figure);
            this.trackEvent(figure, GAAction.SELECTED, GAModule.DIAGRAM);
        }
    }

    @Override
    public void onDiagramIllustrationClosed() {
        Figure last = this.state.getLastFigureVisited();
        if(last!=null){
            this.state.setLastFigureVisited(null);
            this.trackEvent(last, GAAction.HIDDEN, GAModule.DIAGRAM);
        }
    }

    @Override
    public void onHierarchyEventSelected(Path path, Pathway pathway, Event event) {
        DatabaseObject databaseObject = (pathway!=event && event!=null) ? event : pathway;
        if(!databaseObject.equals(this.state.getLastDatabaseObjectSelected())){
            this.state.setLastDatabaseObjectSelected(databaseObject);
            this.trackEvent(event, GAAction.SELECTED, GAModule.EVENT_HIERARCHY);
        }
    }

    @Override
    public void onOverviewItemSelected(DatabaseObject databaseObject) {
        if(!databaseObject.equals(this.state.getLastDatabaseObjectSelected())){
            this.state.setLastDatabaseObjectSelected(databaseObject);
            this.trackEvent(databaseObject,GAAction.SELECTED, GAModule.OVERVIEW_TAB);
        }
    }

    @Override
    public void onMoleculesDownloadStarted() {
        this.trackEvent("DOWNLOAD", GAAction.SELECTED, GAModule.MOLECULES_TAB);
    }

    @Override
    public void onMoleculesItemSelected(DatabaseObject molecule) {
        if(!molecule.equals(this.state.getLastDatabaseObjectSelected())){
            this.state.setLastDatabaseObjectSelected(molecule);
            this.trackEvent(molecule,GAAction.SELECTED, GAModule.MOLECULES_TAB);
        }
    }

    @Override
    public void onDetailedViewLoaded(DatabaseObject databaseObject){
        if(!databaseObject.equals(this.state.getLastDatabaseObjectExpanded())){
            this.state.setLastDatabaseObjectExpanded(databaseObject);
            SchemaClass schemaClass = databaseObject.getSchemaClass();
            if(!schemaClass.equals(SchemaClass.SUMMATION)){
                this.trackEvent(databaseObject, GAAction.EXPANDED, GAModule.DETAILS);
            }
        }
    }

    @Override
    public void onTourManagerTourStarted() {
        this.trackEvent(GACategory.TOUR, GAAction.STARTED, GAModule.GENERAL);
    }

    @Override
    public void onTourManagerTourFinished() {
        this.trackEvent(GACategory.TOUR, GAAction.FINISHED, GAModule.GENERAL);
    }

    @Override
    public void onTourManagerTourCancelled() {
        this.trackEvent(GACategory.TOUR, GAAction.CANCELED, GAModule.GENERAL);
    }
}
