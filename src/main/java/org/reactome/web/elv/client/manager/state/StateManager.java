package org.reactome.web.elv.client.manager.state;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import org.reactome.web.elv.client.center.content.analysis.event.AnalysisCompletedEvent;
import org.reactome.web.elv.client.center.model.CenterToolType;
import org.reactome.web.elv.client.common.Controller;
import org.reactome.web.elv.client.common.EventBus;
import org.reactome.web.elv.client.common.LocationHelper;
import org.reactome.web.elv.client.common.analysis.helper.AnalysisHelper;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Event;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.common.data.model.Species;
import org.reactome.web.elv.client.common.events.ELVEventType;
import org.reactome.web.elv.client.common.model.Path;
import org.reactome.web.elv.client.details.model.DetailsTabType;
import org.reactome.web.elv.client.details.tabs.analysis.events.AnalysisTabPathwaySelected;
import org.reactome.web.elv.client.manager.messages.MessageObject;
import org.reactome.web.elv.client.manager.messages.MessageType;
import org.reactome.web.elv.client.manager.title.TitleManager;

import java.util.LinkedList;
import java.util.List;

/**
 * Manages the app state in order to control the History.
 *
 * While the user is enjoying the web application xDD, this class is keeping track of
 * the instances selection and details tab changing and quietly modifies the URL.
 * Moreover, when the user plays with the browser back and forward buttons, the manager
 * fires the convenient events for placing the app in the right state.
 *
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class StateManager extends Controller implements ValueChangeHandler<String>, AdvancedState.AdvancedStateLoadedHandler {
    private Pathway selectedDiagram;

    private boolean analysisAvailable =  LocationHelper.isAnalysisAvailable();

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
        //ToDo: Replacement okay?
        //replaced this.eventBus.fireELVEvent(ELVEventType.STATE_MANAGER_WRONG_STATE, token);
        //with message from implementation of onStateManagerWrongStateReached in MsgManager
        MessageObject msgObj = new MessageObject("URL Token error: " + token + " is not build properly.\n" +
                "Please check it complies with the format.", getClass(), MessageType.INTERNAL_ERROR);
        this.eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
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

        //SIXTH STEP -> check the analysis token (only if analysis is Available for the current server)
        if(this.analysisAvailable && !currentState.hasReachedAnalysisState(desiredState)){
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
                            MessageObject msgObj = new MessageObject("Analysis: " + message, getClass(), MessageType.INTERNAL_ERROR);
                            eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
                            //DialogBoxFactory.alert("Analysis", message);
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
//        this.desiredState.setCenterTool(CenterToolType.getDefault()); //TODO: Test if still needed
        History.newItem(this.desiredState.toString(), true);
    }

    @Override
    public void onAnalysisCompleted(AnalysisCompletedEvent event) {
        this.desiredState = new AdvancedState(this.currentState);
        this.desiredState.setCenterTool(CenterToolType.getDefault());
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
    public void onFireworksAnalysisReset() {
        currentState.setAnalysisToken(null);
        History.newItem(currentState.toString(), false);
        this.eventBus.fireELVEvent(ELVEventType.STATE_MANAGER_ANALYSIS_TOKEN_RESET);
    }

    @Override
    public void onFireworksPathwaySelected(final Pathway pathway) {
        if(pathway==null || pathway.equals(this.currentState.getPathway())) return;
        /**
         * The following procedure is quite complicated and it is because we are projecting here from a graph point
         * of view of the pathways to a tree based representation. The main difference is that for each node in a graph
         * we can find now several locations and at the same time for "subpathways" that are only once in the graph
         * the location opened in the hierarchy is related to the current location (where the browser is focused)
         */
        //1) The ancestors have to be retrieved in order to start the calculations
        PathRetriever.retrieveAncestors(pathway, new PathRetriever.PathHandler() {
            @Override
            public void onPathsRetrieved(List<Path> paths) {
                List<Event> targetPath; //Can not defined it as final here
                List<Event> currentPath = currentState.getPath();

                //2) By default the targetPath is the first one provided
                targetPath = paths.get(0).getPath();
                if(!currentPath.isEmpty()){
                    //3) In case there is currently a path selected, we pick the path which includes it (or leave the default)
                    for (Path path : paths) {
                        if(path.contains(currentPath)){
                            targetPath = path.getPath();
                            break;
                        }
                    }
                }
                final List<Event> pathAux = targetPath; //It could not be defined final a few lines above :(
                //4) Simulates a stableIdentifier loader (using the dbId, yes it works ;D) and mixes the resulting state
                //   with the currentState and the path calculated previously
                new StableIdentifierLoader(pathway.getDbId().toString(), new StableIdentifierLoader.StableIdentifierLoadedHandler() {
                    @Override
                    public void onStableIdentifierLoaded(AdvancedState advancedState) {
                        AdvancedState aux = new AdvancedState(currentState);
                        aux.setPathway(advancedState.getPathway());
                        aux.setPath(pathAux);
                        aux.setInstance(advancedState.getInstance());
                        History.newItem(aux.toString(), true);
                    }
                });
            }
        });
    }

    @Override
    public void onFireworksPathwaySelectionReset() {
        this.desiredState = new AdvancedState(this.currentState);
        this.desiredState.setPathway(null);
        this.desiredState.setInstance(null);
        this.desiredState.setPath(new LinkedList<Event>());
        History.newItem(this.desiredState.toString(), true);
    }

    @Override
    public void onOverviewPathwaySelected(Pathway pathway) {
        AdvancedState state = new AdvancedState(this.currentState);
        state.resetInstancesState();
        state.setPathway(pathway);
        state.setPath(new LinkedList<Event>());
        History.newItem(state.toString(), true);
    }

    @Override
    public void onHierarchyEventSelected(Path path, Pathway pathway, Event event) {
        this.desiredState = new AdvancedState(this.currentState);
        this.desiredState.setPathway(pathway);
        this.desiredState.setInstance(event);
        this.desiredState.setPath(path.getPath());
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
        state.changeCenterToolIfNeeded(true);
        History.newItem(state.toString(), true);
    }

    @Override
    public void onTopBarSpeciesLoaded() {
        History.fireCurrentHistoryState();
    }

    @Override
    public void onTopBarAnalysisSelected(){
        AdvancedState state = new AdvancedState(this.currentState);
        state.setCenterTool(CenterToolType.ANALYSIS);
        History.newItem(state.toString(), true);
    }

    private void setCurrentState(Pathway pathway, DatabaseObject databaseObject){
        if( pathway==null ){
            this.selectedDiagram = null;
            this.currentState.setPathway(null);
            this.currentState.setInstance(null);
        } else {
            this.selectedDiagram = pathway;
            this.currentState.setPathway(pathway);
            if( databaseObject==null || databaseObject.equals(pathway) ){
                this.currentState.setInstance(null);
            } else {
                this.currentState.setInstance(databaseObject);
            }
        }
    }
}