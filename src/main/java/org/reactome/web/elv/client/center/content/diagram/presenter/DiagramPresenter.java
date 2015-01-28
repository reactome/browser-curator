package org.reactome.web.elv.client.center.content.diagram.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import org.reactome.web.elv.client.center.content.diagram.view.DiagramView;
import org.reactome.web.elv.client.common.Controller;
import org.reactome.web.elv.client.common.EventBus;
import org.reactome.web.elv.client.common.data.factory.ModelFactory;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Event;
import org.reactome.web.elv.client.common.data.model.Figure;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.common.events.ELVEventType;
import org.reactome.web.elv.client.common.model.Pair;
import org.reactome.web.elv.client.common.utils.Console;
import org.reactome.web.elv.client.manager.messages.MessageObject;
import org.reactome.web.elv.client.manager.messages.MessageType;
import org.reactome.web.elv.client.manager.state.AdvancedState;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DiagramPresenter extends Controller implements DiagramView.Presenter{

    private DiagramView view;

    private Long loadedPathwayId;
    private Long entitySelected;

    private boolean visible = false; // True when the diagram has to be shown
    private AdvancedState targetState; //Used to keep the state to be loaded when the diagram becomes visible

    private DatabaseObject selectAfterLoadingDiagram;

    public DiagramPresenter(EventBus eventBus,DiagramView diagramView) {
        super(eventBus);
		this.view = diagramView;
		this.view.setPresenter(this);
        this.selectAfterLoadingDiagram = null;
    }

    private void diagramEntitySelected(Long dbId){
        //Avoid sending a useless event to the event bus :)
        if(this.entitySelected!=null && this.entitySelected.equals(dbId)) return;

        this.entitySelected = dbId;
        Pair<Long, ELVEventType> tuple = new Pair<Long, ELVEventType>(dbId, ELVEventType.DIAGRAM_ENTITY_SELECTED);
        this.eventBus.fireELVEvent(ELVEventType.DATABASE_OBJECT_REQUIRED, tuple);
    }

    @Override
    public void entitiesSelected(List<Long> selection) {
        if(selection.isEmpty()){
            //if selection size is empty means that the current shown diagram is the "entity" selected
            // -> the user has clicked in the diagram deselecting the current selection
            diagramEntitySelected(loadedPathwayId);
        }else{
            eventBus.fireELVEvent(ELVEventType.DIAGRAM_ENTITIES_SELECTED, selection);
        }
    }

    @Override
    public void entitySelected(Long dbId) {
        diagramEntitySelected(dbId);
    }

    @Override
    public void figureSelected(Figure figure) {
        this.view.showFigure(figure);
        this.eventBus.fireELVEvent(ELVEventType.DIAGRAM_FIGURE_SELECTED, figure);
    }

    @Override
    public void figureClosed() {
        eventBus.fireELVEvent(ELVEventType.DIAGRAM_ILLUSTRATION_CLOSED);
    }

    @Override
    public void onFireworksPathwayOpened(final Pathway pathway) {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                visible = true;
                onStateManagerDatabaseObjectsSelected(targetState.getPath(), targetState.getPathway(), targetState.getInstance());
            }
        });
    }

    @Override
    public void onStateManagerAnalysisTokenSelected(String token) {
        this.view.setAnalysisToken(token);
    }

    @Override
    public void onStateManagerAnalysisTokenReset() {
        this.view.clearOverlays();
    }

    @Override
    public void onAnalysisTabResourceSelected(String resource) {
        this.view.setAnalysisResource(resource);
    }

    @Override
    public void onStateManagerDatabaseObjectsSelected(List<Event> path, Pathway pathway, DatabaseObject databaseObject) {
        //Since Fireworks was introduced, the diagram is not always visible
        if(!visible){
            this.targetState = new AdvancedState();
            this.targetState.setPathway(pathway);
            this.targetState.setInstance(databaseObject);
            this.targetState.setPath(path);
            return;
        }
        this.targetState = null;
        this.view.setFigures(pathway, databaseObject);
        if(this.loadedPathwayId==null || !this.loadedPathwayId.equals(pathway.getDbId())){
            if(pathway.getHasDiagram()){
                this.loadedPathwayId = pathway.getDbId();
                this.view.loadPathway(pathway);
                //The databaseObject will have to be selected after loading the diagram
                if(!pathway.equals(databaseObject)){
                    this.selectAfterLoadingDiagram = databaseObject;
                }
            }else{
                this.loadedPathwayId=null;
                this.selectAfterLoadingDiagram=null;
                this.entitySelected=null;
                this.view.setInitialState();

                MessageObject msgObj = new MessageObject(pathway.getDisplayName() + " does NOT contain diagram!\n" +
                        "ERROR: " + pathway.getDisplayName() + " does NOT contain diagram!", getClass(), MessageType.INTERNAL_ERROR);
                eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
                if(!GWT.isProdMode() && GWT.isClient())
                    Console.error(getClass() + pathway.getDisplayName() + " does NOT contain diagram!");
            }
        }else{
            if(databaseObject!=null){
                setSelection(databaseObject);
            }else{
                resetSelection();
            }
        }
    }

    @Override
    public void onStateManagerInstancesInitialStateReached() {
        this.loadedPathwayId = null;
        this.selectAfterLoadingDiagram = null;
        this.entitySelected = null;
        this.view.setInitialState();
    }

    //The diagram offers the option of "Go to Pathway" and there is not event other than
    //"PathwayChangeEvent". That throwns a "DIAGRAM_LOADED" which is catched here
    //and then the "DIAGRAM_ENTITY_SELECTED" is thrown to the bus to inform other modules
    @Override
    public void pathwayLoaded(Long dbId) {
        if(this.loadedPathwayId == null || !this.loadedPathwayId.equals(dbId)){
            this.loadedPathwayId = dbId;
            Pair<Long, ELVEventType> tuple = new Pair<Long, ELVEventType>(dbId, ELVEventType.DIAGRAM_LOADED);
            this.eventBus.fireELVEvent(ELVEventType.DATABASE_OBJECT_REQUIRED, tuple);
        }
        if(this.selectAfterLoadingDiagram!=null){
            setSelection(this.selectAfterLoadingDiagram);
            this.selectAfterLoadingDiagram = null;
        }
    }

    @Override
    public void resetAnalysisId() {
        this.eventBus.fireELVEvent(ELVEventType.DIAGRAM_ANALYSIS_ID_RESET);
    }

    @Override
    public void subpathwaySelected(final Long pathwayId, final Long subpathwayId) {
        /* TODO How to test? Not used
        Error Handling not yet tested because method doesn't seemed to be used, see also DiagramView */
        String url = "/ReactomeRESTfulAPI/RESTfulWS/queryByIds/DatabaseObject";
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, url);
        requestBuilder.setHeader("Content-Type", "text/plain");
        requestBuilder.setHeader("Accept", "application/json");
        try {
            String post = "ID=" + pathwayId;
            if(subpathwayId!=null){
                post += "," + subpathwayId;
            }
            requestBuilder.sendRequest(post, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    try{
                        JSONArray list = JSONParser.parseStrict(response.getText()).isArray();

                        Pathway pathway = null;
                        Pathway subpathway = null;

                        for(int i=0; i<list.size(); ++i){
                            JSONObject object = list.get(i).isObject();
                            DatabaseObject aux = ModelFactory.getDatabaseObject(object);
                            if(aux.getDbId().equals(pathwayId)){
                                pathway = (Pathway) aux;
                            }else if(subpathwayId!=null && aux.getDbId().equals(subpathwayId)){
                                subpathway = (Pathway) aux;
                            }
                        }

                        Pair<Pathway, Pathway> tuple = new Pair<Pathway, Pathway>(pathway, subpathway);
                        eventBus.fireELVEvent(ELVEventType.DIAGRAM_SUBPATHWAY_SELECTED, tuple);

                    }catch (Exception ex){
                        //ModelFactoryException, NullPointerException, IllegalArgumentException, JSONException
                        MessageObject msgObj = new MessageObject("The received data for selected pathway "
                                + pathwayId + " and subpathway " + subpathwayId + " is empty or faulty and could not be parsed.\n" +
                                "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                        eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
                        if(!GWT.isProdMode() && GWT.isClient())
                            Console.error(getClass() + " ERROR: " + ex.getMessage());
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    MessageObject msgObj = new MessageObject("The request for data for selected pathway "
                            + pathwayId + " and subpathway " + subpathwayId + " received an error instead of a valid response.\n" +
                            "ERROR: " + exception.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                    eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
                    if(!GWT.isProdMode() && GWT.isClient())
                        Console.error(getClass() + " ERROR: " + exception.getMessage());
                }
            });
        } catch (RequestException ex) {
            MessageObject msgObj = new MessageObject("The request for selected pathway "
                    + pathwayId + " and subpathway " + subpathwayId + " could not be received.\n" +
                    "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
            eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
            if(!GWT.isProdMode() && GWT.isClient())
                Console.error(getClass() + " ERROR: " + ex.getMessage());
        }
    }

    @Override
    public void showFireworks(Long dbId) {
        this.visible = false;
        Pair<Long, ELVEventType> tuple = new Pair<Long, ELVEventType>(dbId, ELVEventType.DIAGRAM_FIREWORKS_REQUIRED);
        this.eventBus.fireELVEvent(ELVEventType.DATABASE_OBJECT_REQUIRED, tuple);
    }

    private void setSelection(DatabaseObject databaseObject){
        if(databaseObject==null || databaseObject.getDbId().equals(this.entitySelected)) return;

        boolean forceSelection = true;
        if(databaseObject instanceof Pathway){
            Pathway pathway = (Pathway) databaseObject;
            if(!pathway.getHasDiagram()){
                forceSelection = false;
                this.selectContainedEventIds(pathway);
            }
        }
        if(forceSelection){
            this.entitySelected = databaseObject.getDbId();
            this.view.setSelectionId(databaseObject.getDbId());
        }
    }

    private void selectContainedEventIds(final Pathway pathway){
        String url = "/ReactomeRESTfulAPI/RESTfulWS/getContainedEventIds/" + pathway.getDbId();
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    try{
                        String[] values = response.getText().split(",");
                        List<Long> list = new LinkedList<Long>();

                        //THIS IS A HACK: Used for gk_central when there are inconsistencies
                        list.add(pathway.getDbId()); //Will be removed in view.setSelectionIds if not needed there

                        for (String value : values) {
                            list.add(Long.valueOf(value));
                        }
                        if(!list.isEmpty()){
                            view.setSelectionIds(list);
                        }
                    }catch (Exception ex){
                        MessageObject msgObj = new MessageObject("The received data for selected subpathway " + pathway
                                + "\nis empty or faulty and could not be parsed. Thus no entity can be selected in the Diagram.\n" +
                                "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                        eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
                        if(!GWT.isProdMode() && GWT.isClient())
                            Console.error(getClass() + " ERROR: " + ex.getMessage());
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    MessageObject msgObj = new MessageObject("The request for data for selected subpathway " + pathway
                            + "\nreceived an error instead of a valid response. Thus no entity can be selected in the Diagram.\n" +
                            "ERROR: " + exception.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                    eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
                    if(!GWT.isProdMode() && GWT.isClient())
                        Console.error(getClass() + " ERROR: " + exception.getMessage());
                }
            });
        } catch (RequestException ex) {
            MessageObject msgObj = new MessageObject("The request for selected subpathway " + pathway +
                    "\ncould not be received. Thus no entity can be selected in the Diagram.\n" +
                    "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
            eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
            if(!GWT.isProdMode() && GWT.isClient())
                Console.error(getClass() + " ERROR: " + ex.getMessage());
        }
    }

    private void resetSelection(){
        this.view.clearSelection();
    }
}
