package org.reactome.web.elv.client.common;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.ToggleButton;
import org.reactome.web.elv.client.center.content.analysis.event.AnalysisCompletedEvent;
import org.reactome.web.elv.client.center.content.analysis.event.AnalysisErrorEvent;
import org.reactome.web.elv.client.center.model.CenterToolType;
import org.reactome.web.elv.client.common.data.model.*;
import org.reactome.web.elv.client.common.events.ELVEvent;
import org.reactome.web.elv.client.common.events.ELVEventHandler;
import org.reactome.web.elv.client.common.events.ELVEventType;
import org.reactome.web.elv.client.common.model.Pair;
import org.reactome.web.elv.client.common.model.Path;
import org.reactome.web.elv.client.details.events.DetailsSelection;
import org.reactome.web.elv.client.details.model.DetailsTabType;
import org.reactome.web.elv.client.details.tabs.analysis.events.AnalysisTabPathwaySelected;
import org.reactome.web.elv.client.hierarchy.model.HierarchySelection;
import org.reactome.web.elv.client.manager.state.AdvancedState;
import org.reactome.web.elv.client.manager.state.StateSelection;
import org.reactome.web.elv.client.manager.tour.TourStage;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings({"UnusedParameters", "unchecked"})
public abstract class Controller implements ELVEventHandler {

    protected final EventBus eventBus;

    public Controller(EventBus eventBus) {
		this.eventBus = eventBus;
        this.eventBus.addHandler(ELVEvent.TYPE, this);
    }

    @Override
    public void onEventThrown(ELVEvent event) {
        Object obj = event.getEventAttachedObject();
        switch (event.getELVEventType()){
                    /* GENERAL */
            case ELV_READY:
                onELVReady();
                break;
            case DATABASE_OBJECT_REQUIRED:
                Pair<Long, ELVEventType> dor = (Pair<Long, ELVEventType>) obj;
                onDatabaseObjectRequired(dor.getA(), dor.getB());
                break;
            case DATABASE_OBJECT_DETAILED_VIEW_REQUIRED:
                onDatabaseObjectDetailedViewRequired((Long) obj);
                break;

                    /* ANALYSIS EVENTS */
            case ANALYSIS_COMPLETED:
                onAnalysisCompleted((AnalysisCompletedEvent) obj);
                break;
            case ANALYSIS_ERROR:
                onAnalysisError((AnalysisErrorEvent) obj);
                break;
            case ANALYSIS_TAB_PATHWAY_SELECTED:
                onAnalysisTabPathwaySelected((AnalysisTabPathwaySelected) obj);
                break;
            case ANALYSIS_TAB_RESOURCE_SELECTED:
                onAnalysisTabResourceSelected((String) obj);
                break;

                    /* HIERARCHY EVENTS */
            case HIERARCHY_READY:
                onHierarchyReady();
                break;
            case HIERARCHY_EVENT_SELECTED:
                HierarchySelection hes = (HierarchySelection) obj;
                onHierarchyEventSelected(hes.getPath(), hes.getDiagram(), hes.getEvent());
                break;

                    /* DIAGRAM EVENTS */
            case DIAGRAM_ANALYSIS_ID_RESET:
                onDiagramAnalysisIdReset();
                break;
            case DIAGRAM_ENTITY_SELECTED:
                onDiagramEntitySelected((DatabaseObject) obj);
                break;
            case DIAGRAM_ENTITIES_SELECTED:
                List<Long> list = (List<Long>) obj;
                onDiagramEntitiesSelected(list);
                break;
            case DIAGRAM_FIGURE_SELECTED:
                onDiagramFigureSelected((Figure) obj);
                break;
            case DIAGRAM_ILLUSTRATION_CLOSED:
                onDiagramIllustrationClosed();
                break;
            case DIAGRAM_OVERLAY_CLEARED:
                onDiagramOverlayCleared();
                break;
            case DIAGRAM_SUBPATHWAY_SELECTED:
                Pair<Pathway, Pathway> dss = (Pair<Pathway, Pathway>) obj;
                onDiagramSubpathwaySelected(dss.getA(), dss.getB());
                break;
            case DIAGRAM_LOADED:
                onDiagramLoaded((DatabaseObject) obj);
                break;

                    /* PANELS */
            case DETAILS_PANEL_RESIZED:
                onDetailPanelResized((Integer) obj);
                break;
            case DETAILS_PANEL_TAB_CHANGED:
                onDetailsPanelTabChanged((DetailsTabType) obj);
                break;
            case HIERARCHY_PANEL_RESIZED:
                onHierarchyPanelResized((Integer) obj);
                break;

                    /* TOP BAR EVENTS */
            case TOPBAR_SPECIES_LIST_REQUIRED:
                onTopBarSpeciesListRequired();
                break;
            case TOPBAR_SPECIES_LOADED:
                onTopBarSpeciesLoaded();
                break;
            case TOPBAR_SPECIES_SELECTED:
                onTopBarSpeciesSelected((Species) obj);
                break;
            case TOPBAR_SHOW_DETAILS_BUTTON_TOGGLED:
                onTopBarDetailsButtonToggled((ToggleButton) obj);
                break;
            case TOPBAR_SHOW_HIERARCHY_BUTTON_TOGGLED:
                onTopBarHierarchyButtonToggled((ToggleButton) obj);
                break;
            case TOPBAR_SHOW_DIAGRAM_KEY_BUTTON_TOGGLED:
                onTopBarDiagramKeyButtonToggled((ToggleButton) obj);
                break;
            case TOPBAR_ANALYSIS_SELECTED:
                onTopBarAnalysisSelected();
                break;

                    /* POPUP MANAGER EVENTS */
            case DIAGRAM_KEY_CLOSED:
                onDiagramKeyClosed();
                break;
            case DIAGRAM_KEY_OPENED:
                onDiagramKeyOpened();
                break;

                    /* PARTICIPATING MOLECULES TAB*/
            case MOLECULES_INSTANCE_LOADED:
                Pair<DatabaseObject, JSONObject> mil = (Pair<DatabaseObject, JSONObject>) obj;
                onMoleculesInstanceLoaded(mil.getA(), mil.getB());
                break;
            case MOLECULES_INSTANCE_LOAD_ERROR:
                onMoleculesInstanceRetrieveError((String) obj);
                break;
            case MOLECULES_DATA_QUERIED:
                Pair<DatabaseObject, JSONObject> mdq = (Pair<DatabaseObject, JSONObject>) obj;
                onMoleculesDataQuery(mdq.getA(), mdq.getB());
                break;
            case MOLECULES_DATA_QUERY_ERROR:
                onMoleculesDataQueryError((String) obj);
                break;
            case MOLECULES_DATA_DOWNLOADED:
                onMoleculesDataDownloaded();
                break;

                    /* DETAILS VIEW */
            case DETAILED_VIEW_LOADED:
                onDetailedViewLoaded((DatabaseObject) obj);
                break;

                    /* OVERVIEW TAB */
            case OVERVIEW_EVENT_SELECTED:
                DetailsSelection oes = (DetailsSelection) obj;
                onOverviewEventSelected(oes.getPath(), oes.getDiagram(), oes.getEvent());
                break;
            case OVERVIEW_ITEM_SELECTED:
                onOverviewItemSelected((DatabaseObject) obj);
                break;

                    /* DATA MANAGER EVENTS*/
            case DATA_MANAGER_LOAD_ERROR:
                onDataManagerLoadError((String) obj);
                break;
            case DATA_MANAGER_OBJECT_DETAILED_VIEW_RETRIEVED:
                onDataManagerObjectDetailedViewRetrieved((DatabaseObject) obj);
                break;
            case DATA_MANAGER_OBJECT_TUPLE_RETRIEVED:
                Pair<Pathway, DatabaseObject> smor = (Pair<Pathway, DatabaseObject>) obj;
                onDataManagerObjectTupleRetrieved(smor.getA(), smor.getB());
                break;
            case DATA_MANAGER_SPECIES_LIST_RETRIEVED:
                onDataManagerSpeciesListRetrieved((List<Species>) obj);
                break;

                    /* ORTHOLOGOUS MANAGER */
            case ORTHOLOGOUS_MANAGER_STATE_SELECTED:
                onOrthologousManagerStateSelected((AdvancedState) obj);
                break;

                    /* STATE MANAGER EVENTS */
            case STATE_MANAGER_ANALYSIS_TOKEN_SELECTED:
                onStateManagerAnalysisTokenSelected((String) obj);
                break;
            case STATE_MANAGER_ANALYSIS_TOKEN_RESET:
                onStateManagerAnalysisTokenReset();
                break;
            case STATE_MANAGER_DATABASE_OBJECTS_REQUIRED:
                Pair<Long, Long> smdor = (Pair<Long, Long>) obj;
                onStateManagerDatabaseObjectsRequired(smdor.getA(), smdor.getB());
                break;
            case STATE_MANAGER_DATABASE_OBJECTS_SELECTED:
                StateSelection smdos = (StateSelection) obj;
                onStateManagerDatabaseObjectsSelected(smdos.getPath(), smdos.getPathway(), smdos.getDatabaseObject());
                break;
            case STATE_MANAGER_DETAILS_TAB_SELECTED:
                onStateManagerDetailsTabSelected((DetailsTabType) obj);
                break;
            case STATE_MANAGER_ERROR:
                onStateManagerError((String) obj);
                break;
            case STATE_MANAGER_INSTANCES_INITIAL_STATE:
                onStateManagerInstancesInitialStateReached();
                break;
            case STATE_MANAGER_SPECIES_SELECTED:
                onStateManagerSpeciesSelected((Species) obj);
                break;
            case STATE_MANAGER_TARGET_REACHED:
                onStateManagerTargetReached();
                break;
            case STATE_MANAGER_TOOL_SELECTED:
                onStateManagerToolSelected((CenterToolType) obj);
                break;
            case STATE_MANAGER_TOOLS_INITIAL_STATE:
                onStateManagerToolsInitialStateReached();
                break;
            case STATE_MANAGER_WRONG_STATE:
                onStateManagerWrongStateReached((String) obj);
                break;

                    /* TOUR MANAGER EVENTS */
            case TOUR_MANAGER_TOUR_CANCELLED:
                onTourManagerTourCancelled();
                break;
            case TOUR_MANAGER_TOUR_FINISHED:
                onTourManagerTourFinished();
                break;
            case TOUR_MANAGER_TOUR_PROGRESS:
                Pair<TourStage,Integer> tmtp = (Pair<TourStage,Integer>) obj;
                onTourManagerTourProgress(tmtp.getA(), tmtp.getB());
                break;
            case TOUR_MANAGER_TOUR_STARTED:
                onTourManagerTourStarted();
                break;

        }
    }

    /* GENERAL */
    public void onELVReady(){}
    public void onDatabaseObjectRequired(Long dbId, ELVEventType eventType){}
    public void onDatabaseObjectDetailedViewRequired(Long dbId){}


    /* ANALYSIS EVENTS */
    public void onAnalysisCompleted(AnalysisCompletedEvent event){}
    public void onAnalysisError(AnalysisErrorEvent event){}
    public void onAnalysisTabPathwaySelected(AnalysisTabPathwaySelected selected){}
    public void onAnalysisTabResourceSelected(String resource){}

    /* HIERARCHY EVENTS */
    public void onHierarchyReady(){}
    public void onHierarchyEventSelected(Path path, Pathway pathway, Event event){}

    /* DIAGRAM EVENTS */
    public void onDiagramAnalysisIdReset(){}
    public void onDiagramEntitySelected(DatabaseObject databaseObject){}
    public void onDiagramEntitiesSelected(List<Long> selection){}
    public void onDiagramFigureSelected(Figure figure){}
    public void onDiagramIllustrationClosed(){}
    public void onDiagramOverlayCleared(){}
    public void onDiagramSubpathwaySelected(Pathway pathway, Pathway subpathway){}
    public void onDiagramLoaded(DatabaseObject databaseObject){}

    /* PANELS */
    public void onDetailPanelResized(Integer size){}
    public void onDetailsPanelTabChanged(DetailsTabType tabType){}
    public void onHierarchyPanelResized(Integer size){}

    /* TOP BAR EVENTS */
    public void onTopBarSpeciesListRequired(){}
    public void onTopBarSpeciesLoaded(){}
    public void onTopBarSpeciesSelected(Species species){}
    public void onTopBarDetailsButtonToggled(ToggleButton btn){}
    public void onTopBarHierarchyButtonToggled(ToggleButton btn){}
    public void onTopBarDiagramKeyButtonToggled(ToggleButton btn){}
    public void onTopBarAnalysisSelected(){}

    /* POPUP MANAGER EVENTS */
    public void onDiagramKeyClosed(){}
    public void onDiagramKeyOpened(){}

    /* PARTICIPATING MOLECULES TAB*/
    public void onMoleculesInstanceLoaded(DatabaseObject databaseObject, JSONObject params){}
    public void onMoleculesInstanceRetrieveError(String message){}
    public void onMoleculesDataQuery(DatabaseObject databaseObject, JSONObject params){}
    public void onMoleculesDataQueryError(String message){}
    public void onMoleculesDataDownloaded(){}

    /* DETAILS PANEL */
    public void onDetailedViewLoaded(DatabaseObject obj){}

    /* OVERVIEW TAB */
    public void onOverviewEventSelected(Path path, Pathway pathway, Event event){}
    public void onOverviewItemSelected(DatabaseObject databaseObject){}

    /* DATA MANAGER EVENTS*/
    public void onDataManagerLoadError(String message){}
    public void onDataManagerObjectDetailedViewRetrieved(DatabaseObject databaseObject){}
    public void onDataManagerObjectTupleRetrieved(Pathway pathway, DatabaseObject databaseObject){}
    public void onDataManagerSpeciesListRetrieved(List<Species> speciesList){}

    /* ORTHOLOGOUS MANAGER */
    public void onOrthologousManagerStateSelected(AdvancedState state){}

    /* STATE MANAGER EVENTS */
    public void onStateManagerAnalysisTokenSelected(String token){}
    public void onStateManagerAnalysisTokenReset(){}
    public void onStateManagerDatabaseObjectsRequired(Long pathwayId, Long databaseObjectId){}
    public void onStateManagerDatabaseObjectsSelected(List<Event> path, Pathway pathway, DatabaseObject databaseObject){}
    public void onStateManagerDetailsTabSelected(DetailsTabType tab){}
    public void onStateManagerError(String message){}
    public void onStateManagerInstancesInitialStateReached(){}
    public void onStateManagerSpeciesSelected(Species species){}
    public void onStateManagerTargetReached(){}
    public void onStateManagerToolSelected(CenterToolType tool){}
    public void onStateManagerToolsInitialStateReached(){}
    public void onStateManagerWrongStateReached(String token){}

    /* TOUR MANAGER EVENTS */
    public void onTourManagerTourCancelled(){}
    public void onTourManagerTourFinished(){}
    public void onTourManagerTourProgress(TourStage stage, Integer step){}
    public void onTourManagerTourStarted(){}
}