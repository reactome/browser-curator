package org.reactome.web.elv.client.manager.state;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import org.reactome.web.elv.client.center.content.analysis.event.AnalysisCompletedEvent;
import org.reactome.web.elv.client.center.model.CenterToolType;
import org.reactome.web.elv.client.common.Controller;
import org.reactome.web.elv.client.common.EventBus;
import org.reactome.web.elv.client.common.analysis.helper.AnalysisHelper;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Event;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.common.data.model.Species;
import org.reactome.web.elv.client.common.events.ELVEventType;
import org.reactome.web.elv.client.common.model.Path;
import org.reactome.web.elv.client.common.widgets.DialogBoxFactory;
import org.reactome.web.elv.client.details.model.DetailsTabType;
import org.reactome.web.elv.client.details.tabs.analysis.events.AnalysisTabPathwaySelected;

import java.util.LinkedList;

/**
 * Manages the app state in order to control the History.
 *
 * While the user is enjoying the web application xDD, this class is keeping track of
 * the instances selection and details tab changing and quietly modifies the URL.
 * Moreover, when the user play with the browser back and forward buttons, the manager
 * throws the convenient events for placing the app in the right state.
 *
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class StateManager extends Controller implements ValueChangeHandler<String>, AdvancedState.AdvancedStateLoadedHandler {
    private Pathway selectedDiagram;

    private AdvancedState currentState = new AdvancedState();
    private AdvancedState desiredState = new AdvancedState();

    public StateManager(EventBus eventBus) {
        super(eventBus);
        new TitleManager(eventBus);
        History.addValueChangeHandler(this);
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        String token = event.getValue();
        AdvancedState.create(token, this);
    }

    @Override
    public void onAdvancedStateLoaded(AdvancedState state) {
        this.desiredState = state;
        if(this.desiredState.isCorrect()) { //Check if the token parameters are all right and "make sense"
            this.desiredState.changeCenterToolIfNeeded(false); //No force to select!!
            this.goToDesiredState();
        } else {
            this.goToWrongState(History.getToken());
        }
    }

    private void goToWrongState(String token){
        this.eventBus.fireELVEvent(ELVEventType.STATE_MANAGER_WRONG_STATE, token);
        History.newItem("");
    }

    private void goToDesiredState() {
        //FIRST STEP -> check if we have already reached the desired state
        if(this.desiredState.equals(new AdvancedState()) || this.currentState.equals(this.desiredState)){
            this.desiredState = new AdvancedState();
            eventBus.fireELVEvent(ELVEventType.STATE_MANAGER_TARGET_REACHED);
            return; //DO NOT DELETE THIS!
        }

        //SECOND STEP -> check details tab state
        if(!currentState.hasReachedTabState(desiredState)){
            DetailsTabType tab = desiredState.getDetailsTab();
            currentState.setDetailsTab(tab);
            eventBus.fireELVEvent(ELVEventType.STATE_MANAGER_DETAILS_TAB_SELECTED, tab);
        }

        //THIRD STEP -> check the species state
        if(!currentState.hasReachedSpeciesState(desiredState)){
            currentState.setSpecies(desiredState.getSpecies());
            Species species = this.desiredState.getSpecies();
            eventBus.fireELVEvent(ELVEventType.STATE_MANAGER_SPECIES_SELECTED, species);
            return; //DO NOT DELETE THIS!
        }

        //FOURTH STEP -> check for the instances
        if(!currentState.hasReachedInstancesState(desiredState)){
            if(desiredState.isInstancesInitialState()){
                currentState.resetInstancesState();
                this.eventBus.fireELVEvent(ELVEventType.STATE_MANAGER_INSTANCES_INITIAL_STATE);
            }else{
                Pathway pathway = desiredState.getPathway();
                if(pathway!=null){
                    DatabaseObject instance = desiredState.getInstance();
                    currentState.setPath(desiredState.getPath());
                    setCurrentState(pathway, instance);
                    StateSelection selection = new StateSelection(desiredState.getPath(), pathway, instance);
                    this.eventBus.fireELVEvent(ELVEventType.STATE_MANAGER_DATABASE_OBJECTS_SELECTED, selection);
                }
            }
        }

        //FIFTH STEP -> check for the selected tool
        if(!currentState.hasReachToolState(desiredState)){
            if(desiredState.isToolInitialState()){
                currentState.setCenterTool(desiredState.getCenterTool());
                this.eventBus.fireELVEvent(ELVEventType.STATE_MANAGER_TOOLS_INITIAL_STATE);
            }else{
                currentState.setCenterTool(desiredState.getCenterTool());
                this.eventBus.fireELVEvent(ELVEventType.STATE_MANAGER_TOOL_SELECTED, desiredState.getCenterTool());
            }
        }

        //SIXTH STEP -> check the analysis token
        if(!currentState.hasReachedAnalysisState(desiredState)){
            final String token = desiredState.getAnalysisToken();
            currentState.setAnalysisToken(token);
            if(token==null){
                this.eventBus.fireELVEvent(ELVEventType.STATE_MANAGER_ANALYSIS_TOKEN_RESET);
            }else{
                AnalysisHelper.checkTokenAvailability(token, new AnalysisHelper.TokenAvailabilityHandler() {
                    @Override
                    public void onTokenAvailabilityChecked(boolean available, String message) {
                        //We don NOT want to remove the token in this iteration if is not available, but we want to flag
                        //it as "delete" when a new action happens. The analysis state remains reached so no more checks
                        currentState.setAnalysisTokenAvailable(available);
                        if(available){
                            eventBus.fireELVEvent(ELVEventType.STATE_MANAGER_ANALYSIS_TOKEN_SELECTED, token);
                        }else{
                            eventBus.fireELVEvent(ELVEventType.STATE_MANAGER_ANALYSIS_TOKEN_RESET);
                            DialogBoxFactory.alert("Analysis", message);
                        }
                    }
                });
            }
        }

        //This is like FIRST STEP, but we keep both to avoid a History.fireCurrentHistoryState() here :)
        this.desiredState = new AdvancedState();
        eventBus.fireELVEvent(ELVEventType.STATE_MANAGER_TARGET_REACHED);
    }

    @Override
    public void onAnalysisTabPathwaySelected(AnalysisTabPathwaySelected selected) {
        this.desiredState = new AdvancedState(this.currentState);
        this.desiredState.setSpecies(selected.getSpecies());
        this.desiredState.setPathway(selected.getDiagram());
        this.desiredState.setInstance(selected.getPathway());
        this.desiredState.setPath(new LinkedList<Event>());
        this.desiredState.setCenterTool(CenterToolType.getDefault());
        History.newItem(this.desiredState.toString(), true);
    }

    @Override
    public void onAnalysisCompleted(AnalysisCompletedEvent event) {
        this.desiredState = new AdvancedState(this.currentState);
        this.desiredState.setAnalysisToken(event.getAnalysisResult().getSummary().getToken());
        this.desiredState.setDetailsTab(DetailsTabType.ANALYSIS);
        this.desiredState.changeCenterToolIfNeeded(true);
        History.newItem(desiredState.toString(), true);
    }

    @Override
    public void onDataManagerObjectTupleRetrieved(Pathway pathway, DatabaseObject databaseObject) {
        setCurrentState(pathway, databaseObject);
        StateSelection selection = new StateSelection(this.currentState.getPath(), pathway, databaseObject);
        eventBus.fireELVEvent(ELVEventType.STATE_MANAGER_DATABASE_OBJECTS_SELECTED, selection);
        History.newItem(this.currentState.toString(), true);
    }

    @Override
    public void onDetailsPanelTabChanged(DetailsTabType tabType) {
        if(!currentState.getDetailsTab().equals(tabType)){
            currentState.setDetailsTab(tabType);
            History.newItem(currentState.toString(), false);
        }
    }

    @Override
    public void onDiagramAnalysisIdReset() {
        currentState.setAnalysisToken(null);
        History.newItem(currentState.toString(), false);
        this.eventBus.fireELVEvent(ELVEventType.STATE_MANAGER_ANALYSIS_TOKEN_RESET);
    }

    @Override
    public void onDiagramSubpathwaySelected(Pathway pathway, Pathway subpathway) {
        //Using the state here just because we don't have the pathway and subpathway objects, so
        //is easier to rely in the "StateManager" to ensure we reach the proper state.
        this.desiredState = new AdvancedState(this.currentState);
        this.desiredState.setPathway(pathway);
        this.desiredState.setInstance(subpathway);
        History.newItem(this.desiredState.toString(), true);
    }

    @Override
    public void onDiagramOverlayCleared() {
        if(this.desiredState.equals(new AdvancedState()) || this.currentState.equals(this.desiredState)){
            this.currentState.setAnalysisToken(null);
            History.newItem(this.currentState.toString(), false);
        }
    }

    @Override
    public void onDiagramLoaded(DatabaseObject databaseObject) {
        if(!this.selectedDiagram.equals(databaseObject)){
            setCurrentState((Pathway) databaseObject, databaseObject);
            History.newItem(this.currentState.toString(), false);
            StateSelection selection = new StateSelection(this.currentState.getPath(), this.selectedDiagram, databaseObject);
            this.eventBus.fireELVEvent(ELVEventType.STATE_MANAGER_DATABASE_OBJECTS_SELECTED, selection);
        }
    }

    @Override
    public void onHierarchyEventSelected(Path path, Pathway pathway, Event event) {
        this.currentState.setPath(path.getPath());
        setCurrentState(pathway, event);
        this.desiredState = new AdvancedState(this.currentState);
        this.desiredState.setCenterTool(CenterToolType.getDefault());
        History.newItem(this.desiredState.toString(), true);
    }

    @Override
    public void onHierarchyReady() {
        History.fireCurrentHistoryState();
    }

    @Override
    public void onOrthologousManagerStateSelected(AdvancedState state) {
        state.setDetailsTab(this.currentState.getDetailsTab());
        state.setAnalysisToken(this.currentState.getAnalysisToken());
        //Requirement from diagram side
        if(state.getSpecies().equals(this.currentState.getSpecies())){
            state.setAnalysisToken(this.currentState.getAnalysisToken());
        }
        state.changeCenterToolIfNeeded(true);
        History.newItem(state.toString(), true);
    }

    @Override
    public void onTopBarSpeciesLoaded() {
        History.fireCurrentHistoryState();
    }

    @Override
    public void onTopBarAnalysisSelected(){
        this.currentState.setCenterTool(CenterToolType.ANALYSIS);
        History.newItem(this.currentState.toString(), false);
    }

    private void setCurrentState(Pathway pathway, DatabaseObject databaseObject){
        if( pathway==null ){
            this.selectedDiagram = null;
            this.currentState.setPathway(null);
        } else {
            this.selectedDiagram = pathway;
            this.currentState.setPathway(pathway);
        }

        if( databaseObject==null || databaseObject.equals(pathway) ){
            this.currentState.setInstance(null);
        } else {
            this.currentState.setInstance(databaseObject);
        }
    }
}