package org.reactome.web.elv.client.center.content.fireworks.presenter;

import com.google.gwt.core.client.Duration;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.*;
import org.reactome.web.elv.client.center.content.fireworks.view.FireworksView;
import org.reactome.web.elv.client.center.model.CenterToolType;
import org.reactome.web.elv.client.center.presenter.CenterPresenter;
import org.reactome.web.elv.client.common.Controller;
import org.reactome.web.elv.client.common.EventBus;
import org.reactome.web.elv.client.common.data.model.*;
import org.reactome.web.elv.client.common.events.ELVEventType;
import org.reactome.web.elv.client.common.events.EventHoverEvent;
import org.reactome.web.elv.client.common.events.EventHoverResetEvent;
import org.reactome.web.elv.client.common.handlers.EventHoverHandler;
import org.reactome.web.elv.client.common.handlers.EventHoverResetHandler;
import org.reactome.web.elv.client.common.model.Pair;
import org.reactome.web.elv.client.manager.data.DataManager;
import org.reactome.web.elv.client.manager.messages.MessageObject;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class FireworksPresenter extends Controller implements FireworksView.Presenter, EventHoverHandler, EventHoverResetHandler {

    /**
     * Static files are cached by the browser, but we want to force download at least once per session
     * to avoid old fireworks layout views
     */
    private final double SESSION_STATIC_VERSION = Duration.currentTimeMillis();

    private boolean firstLoad = true;

    private FireworksView view;

    private Pathway selected;
    private boolean visible = true;

    public FireworksPresenter(EventBus eventBus, FireworksView view) {
        super(eventBus);
        this.view = view;
        this.view.setPresenter(this);

        this.eventBus.addHandler(EventHoverEvent.TYPE, this);
        this.eventBus.addHandler(EventHoverResetEvent.TYPE, this);
    }

    @Override
    public void onEventHovered(EventHoverEvent e) {
        if(e.getSource()==this) return;
        Pathway toHighlight;
        if(e.getEvent() instanceof Pathway){
            toHighlight = (Pathway) e.getEvent();
        }else{
            toHighlight = (Pathway) e.getPath().get(e.getPath().size()-1);
        }
        if(this.visible) {
            this.view.highlightPathway(toHighlight);
        }
    }

    @Override
    public void onAnalysisTabResourceSelected(String resource) {
        this.view.setAnalysisResource(resource);
    }

    @Override
    public void onDiagramFireworksRequired(Pathway pathway) { //IMPORTANT: DO NOT USE pathway here. use this.toSelect
        this.visible = true;
        this.view.selectPathway(selected);
    }

    @Override
    public void onEventHoveredReset() {
        this.view.resetHighlight();
    }

    @Override
    public void onStateManagerAnalysisTokenSelected(String token) {
        this.view.setAnalysisToken(token);
    }

    @Override
    public void onStateManagerAnalysisTokenReset() {
        this.view.resetAnalysisToken();
    }

    @Override
    public void onStateManagerSpeciesSelected(Species species) {
        this.view.resetView(); //this forces the view to "carry" the following actions in the EventBus
        String speciesName = species.getDisplayName().replaceAll(" ", "_");
        loadSpeciesFireworks(speciesName);
    }

    @Override
    public void onStateManagerDatabaseObjectsSelected(List<Event> path, Pathway pathway, DatabaseObject databaseObject) {
        Pathway toSelect = null;
        if(databaseObject instanceof Pathway){
            toSelect = (Pathway) databaseObject;
        }else if(path!=null && !path.isEmpty()){
            toSelect = (Pathway) path.get(path.size()-1);
        }

        if(this.visible && toSelect!=null) {
            if(firstLoad || ( databaseObject!=null && databaseObject instanceof ReactionLikeEvent) ){
                this.view.openPathway(toSelect);
            }else if(!toSelect.equals(this.selected)){
                this.view.selectPathway(toSelect);
            }
        }
        this.selected = toSelect;
    }

    @Override
    public void onStateManagerInstancesInitialStateReached() {
        this.view.resetSelection();
    }

    @Override
    public void onStateManagerTargetReached() {
        this.firstLoad = false;
    }

    @Override
    public void selectPathway(Long dbId) {
        if(this.selected!=null && dbId.equals(this.selected.getDbId())) return;
        try {
            DataManager.getDataManager().databaseObjectDetailedViewRequired(dbId, new DataManager.DataManagerObjectRetrievedHandler() {
                @Override
                public void onDatabaseObjectRetrieved(DatabaseObject databaseObject) {
                    selected = (Pathway) databaseObject;
                    eventBus.fireELVEvent(ELVEventType.FIREWORKS_PATHWAY_SELECTED, selected);
                }

                @Override
                public void onError(MessageObject messageObject) {
                    eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, messageObject);
                }
            });
        } catch (Exception e) {
            //VERY UNLIKELY TO HAPPEN HERE :)
            if(!GWT.isScript()) e.printStackTrace();
        }
    }

    @Override //This is weird but needed since the object didn't exist before;
    public void onFireworksPathwaySelected(Pathway pathway) {
        this.selected = pathway;
    }

    @Override
    public void resetPathwaySelection() {
        this.selected = null;
        this.eventBus.fireELVEvent(ELVEventType.FIREWORKS_PATHWAY_SELECTION_RESET);
    }

    @Override
    public void highlightPathway(Long dbId) {
        final FireworksPresenter _this = this;
        try {
            DataManager.getDataManager().databaseObjectDetailedViewRequired(dbId, new DataManager.DataManagerObjectRetrievedHandler() {
                @Override
                public void onDatabaseObjectRetrieved(DatabaseObject databaseObject) {
                    eventBus.fireEventFromSource(new EventHoverEvent((Pathway) databaseObject), _this);
                }

                @Override
                public void onError(MessageObject messageObject) {
                    eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, messageObject);
                }
            });
        } catch (Exception e) {
            //VERY UNLIKELY TO HAPPEN HERE :)
            if(!GWT.isScript()) e.printStackTrace();
        }
    }

    @Override
    public void profileChanged(String profileName) {
        eventBus.fireELVEvent(ELVEventType.FIREWORKS_PROFILE_CHANGED, profileName);
    }

    @Override
    public void resetAnalysis() {
        eventBus.fireELVEvent(ELVEventType.FIREWORKS_ANALYSIS_RESET);
    }

    @Override
    public void resetPathwayHighlighting() {
        eventBus.fireEventFromSource(new EventHoverResetEvent(), this);
    }

    @Override
    public void onStateManagerToolsInitialStateReached() {
        this.visible = CenterPresenter.CURRENT_DISPLAY.equals(CenterPresenter.Display.FIREWORKS);
    }

    @Override
    public void onStateManagerToolSelected(CenterToolType tool) {
        switch (tool){
            case ANALYSIS:
                this.visible = false;
                break;
            default:
                this.visible = CenterPresenter.CURRENT_DISPLAY.equals(CenterPresenter.Display.FIREWORKS);
        }
        if(this.visible){
            this.view.selectPathway(selected);
        }
    }

    @Override
    public void showPathwayDiagram(Long dbId) {
        this.visible = false;
        if(this.selected==null || !this.selected.getDbId().equals(dbId)) {
            Pair<Long, ELVEventType> tuple = new Pair<Long, ELVEventType>(dbId, ELVEventType.FIREWORKS_PATHWAY_OPENED);
            this.eventBus.fireELVEvent(ELVEventType.DATABASE_OBJECT_REQUIRED, tuple);
        }else{
            //Here we carry out the selection event, so this.selected contains the proper object already
            this.eventBus.fireELVEvent(ELVEventType.FIREWORKS_PATHWAY_OPENED, this.selected);
        }
    }


    public void loadSpeciesFireworks(String species){
        String url = "/download/current/fireworks/" + species + ".json?t=" + SESSION_STATIC_VERSION;
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    try{
                        String json = response.getText();
                        view.loadSpeciesFireworks(json);
                    }catch (Exception ex){
                        //ModelFactoryException, NullPointerException, IllegalArgumentException, JSONException
//                        MessageObject msgObj = new MessageObject("The received object for the required detailed view" +
//                                "\n'DbId=" + dbId + "' is empty or faulty and could not be parsed.\n" +
//                                "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                        if(!GWT.isScript()) ex.printStackTrace();
                    }
                }
                @Override
                public void onError(Request request, Throwable exception) {
                    /*replaced: eventBus.fireELVEvent(ELVEventType.DATA_MANAGER_LOAD_ERROR, exception.getMessage());*/
//                    MessageObject msgObj = new MessageObject("The detailed view request for 'DbId=" + dbId + "'\n" +
//                            "received an error instead of a valid response.\n" +
//                            "ERROR: " + exception.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                    if(!GWT.isScript()) exception.printStackTrace();
                }
            });
        }catch (RequestException ex) {
//            MessageObject msgObj = new MessageObject("The requested detailed view for 'DbId=" + dbId
//                    + "' could not be received.\n" +
//                    "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
            if(!GWT.isScript()) ex.printStackTrace();
        }
    }
}
