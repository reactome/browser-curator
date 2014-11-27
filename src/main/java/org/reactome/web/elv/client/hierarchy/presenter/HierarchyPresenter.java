package org.reactome.web.elv.client.hierarchy.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import org.reactome.web.elv.client.common.Controller;
import org.reactome.web.elv.client.common.EventBus;
import org.reactome.web.elv.client.common.analysis.factory.AnalysisModelFactory;
import org.reactome.web.elv.client.common.analysis.helper.AnalysisHelper;
import org.reactome.web.elv.client.common.analysis.model.PathwaySummary;
import org.reactome.web.elv.client.common.data.factory.ModelFactory;
import org.reactome.web.elv.client.common.data.model.*;
import org.reactome.web.elv.client.common.events.ELVEventType;
import org.reactome.web.elv.client.common.events.EventHoverEvent;
import org.reactome.web.elv.client.common.events.EventHoverResetEvent;
import org.reactome.web.elv.client.common.handlers.EventHoverHandler;
import org.reactome.web.elv.client.common.handlers.EventHoverResetHandler;
import org.reactome.web.elv.client.common.model.Ancestors;
import org.reactome.web.elv.client.common.model.Path;
import org.reactome.web.elv.client.common.utils.Console;
import org.reactome.web.elv.client.hierarchy.model.HierarchySelection;
import org.reactome.web.elv.client.hierarchy.view.HierarchyView;
import org.reactome.web.elv.client.manager.messages.MessageObject;
import org.reactome.web.elv.client.manager.messages.MessageType;
import org.reactome.web.elv.client.manager.tour.TourStage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class HierarchyPresenter extends Controller implements HierarchyView.Presenter, AnalysisHelper.ResourceChosenHandler,
        EventHoverHandler, EventHoverResetHandler {

	private HierarchyView view;

    private Species currentSpecies;

    private Path pathToExpand;
    private int openingPath;

    private Pathway loadedDiagram;
    private DatabaseObject selectedDatabaseObject;

    private String analysisToken;
    private String resource;

	public HierarchyPresenter(EventBus eventBus, HierarchyView treeView) {
        super(eventBus);
		this.view = treeView;
		this.view.setPresenter(this);

//        this.eventBus.addHandler(EventHoverEvent.TYPE, this);
//        this.eventBus.addHandler(EventHoverResetEvent.TYPE, this);

        this.pathToExpand = new Path();
        this.openingPath = -1;
        this.loadedDiagram = null;
        this.selectedDatabaseObject = null;
	}

    @Override
    public void eventHovered(Path path, Pathway pathway, Event event) {
        this.eventBus.fireEventFromSource(new EventHoverEvent(path, pathway, event), this);
    }

    @Override
    public void eventHoveredReset() {
        this.eventBus.fireEventFromSource(new EventHoverResetEvent(), this);
    }

    @Override
    public void eventSelected(Path path, Pathway pathway, Event event) {
        HierarchySelection selection = new HierarchySelection(path, pathway, event);
        this.eventBus.fireELVEvent(ELVEventType.HIERARCHY_EVENT_SELECTED, selection);
    }

    @Override
    public void eventChildrenRequired(final Path path, final Event event) {
        String url = "/ReactomeRESTfulAPI/RESTfulWS/queryById/Pathway/" + event.getDbId();
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    try{
                        JSONObject json = JSONParser.parseStrict(response.getText()).isObject();
                        Pathway pathway = new Pathway(json);
                        List<Event> children = pathway.getHasEvent();
                        view.loadItemChildren(currentSpecies, path, pathway, children);
                        //Next bit is to load analysis details in the hierarchy items by demand
                        Set<Long> dbIds = new HashSet<Long>();
                        Set<Long> aux = new HashSet<Long>();
                        for (Event child : children) {
                            if(child instanceof ReactionLikeEvent){
                                aux.add(event.getDbId());
                            }else{
                                dbIds.add(child.getDbId());
                            }
                        }
                        getAnalysisData(dbIds, aux);
                    }catch (Exception ex){
                        //ModelFactoryException, NullPointerException, IllegalArgumentException, JSONException
                        MessageObject msgObj = new MessageObject("The received data about the children of '" + event.getDisplayName()
                                + "'\nis empty or faulty and could not be parsed.\n" +
                                "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                        Console.error(getClass() + " ERROR: The received data is empty or faulty and could not be parsed.");
                        errorMsg(msgObj);
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    if(!GWT.isScript()){
                        Console.error(getClass() + " received an error instead of a response.");
                    }
                    MessageObject msgObj = new MessageObject("The request for data about the children of '" + event.getDisplayName()
                            + "'\nreceived an error instead of a valid response.\n" +
                            "ERROR: " + exception.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                    errorMsg(msgObj);
                }
            });
        } catch (RequestException ex) {
            MessageObject msgObj = new MessageObject("The date about the children of '" + event.getDisplayName() +
                    "' could not be received.\n" +
                    "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
            Console.error(getClass() + ex.getMessage());
            errorMsg(msgObj);
        }
    }

    private void expandPath() throws Exception {
        if(this.openingPath<this.pathToExpand.size()-1){
            DatabaseObject next = this.pathToExpand.get(this.openingPath);
            Path path = this.pathToExpand.getSubPath(this.openingPath);
            this.openingPath += 1;
            if(next instanceof Pathway){
                this.view.expandPathway(path, (Pathway) next);
            }else{
                this.finishedExpandingPath(this.loadedDiagram, this.selectedDatabaseObject);
            }
        }else{
            this.finishedExpandingPath(this.loadedDiagram, this.selectedDatabaseObject);
        }
    }

    private void finishedExpandingPath(Pathway pathway, DatabaseObject databaseObject) throws Exception {
        this.openingPath = -1;
        if(databaseObject instanceof Event){
            this.view.highlightPath(this.pathToExpand, pathway, (Event) databaseObject);
        }else{
            this.view.highlightPath(this.pathToExpand, pathway, null);
        }
    }

    @Override
    public void frontPageItemsRequired(final Species species) {
        String speciesName = species.getDisplayName().replaceAll(" ", "+");
        String url = "/ReactomeRESTfulAPI/RESTfulWS/frontPageItems/" + speciesName;
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    try{
                        JSONArray list = JSONParser.parseStrict(response.getText()).isArray();
                        List<Event> children = new ArrayList<Event>();
                        for(int i=0; i<list.size(); ++i){
                            Event child = (Event) ModelFactory.getDatabaseObject(list.get(i).isObject());
                            children.add(child);
                        }
                        view.loadItemChildren(currentSpecies, null, null, children);
                        hierarchyReady();
                    }catch (Exception ex){
                        //ModelFactoryException, NullPointerException, IllegalArgumentException, JSONException
                        MessageObject msgObj = new MessageObject("The received data for the front page items of '"
                                + species.getDisplayName() + "'\nis empty or faulty and could not be parsed.\n" +
                                "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                        Console.error(getClass() + " ERROR: The received data for the front page items " +
                                "is empty or faulty and could not be parsed");
                        errorMsg(msgObj);
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    /* ToDo: Check before submit
                       Method for DATA_MANANGER_LOAD_ERROR in Controller is not implemented
                    eventBus.fireELVEvent(ELVEventType.DATA_MANAGER_LOAD_ERROR, exception.getMessage());
                    */
                    if(!GWT.isScript()){
                        Console.error(getClass() + " received an error instead of a response. "
                                + exception.getMessage());
                    }

                    MessageObject msgObj = new MessageObject("The front-page-items-request for '" + species.getDisplayName() +
                            "' received an error instead of a valid response.\n" +
                            "ERROR: " + exception.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                    errorMsg(msgObj);
                    Console.error(getClass() + " The front-page-items-request received an error instead of a valid response");
                }
            });
        } catch (RequestException ex) {
            /* ToDo: Check before submit
               Method for DATA_MANANGER_LOAD_ERROR in Controller is not implemented
            eventBus.fireELVEvent(ELVEventType.DATA_MANAGER_LOAD_ERROR, ex.getMessage());
            */
            MessageObject msgObj = new MessageObject("The front page items for '" + species.getDisplayName() +
                    "' could not be received.\n" +
                    "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
            errorMsg(msgObj);
            Console.error(getClass() + " The front page items could not be received.");
        }
    }

    @Override
    public void getAnalysisData(Set<Long> eventIds, Set<Long> pathwaysWithReactions) {
        if(this.analysisToken==null) return;
        if(!eventIds.isEmpty()){
            getAnalysisDataForPathways(eventIds);
        }
        if(!pathwaysWithReactions.isEmpty()){
            getAnalysisDataForPathwaysWithReactions(pathwaysWithReactions);
        }
    }

    private void getAnalysisDataForPathways(Set<Long> eventIds){
        StringBuilder post = new StringBuilder();
        for (Long id : eventIds) {
            post.append(id).append(",");
        }
        post.delete(post.length()-1, post.length());

        String url = AnalysisHelper.URL_PREFIX + "/token/" + this.analysisToken + "/filter/pathways?resource=" + this.resource;
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(post.toString(), new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    try {
                        List<PathwaySummary> result = AnalysisModelFactory.getPathwaySummaryList(response.getText());
                        view.showAnalysisResult(result);
                    } catch (Exception ex) {
                        //AnalysisModelFactory, NullPointerException, IllegalArgumentException, JSONException
                        MessageObject msgObj = new MessageObject("The received object for token=" + analysisToken
                                + "\nand resource=" + resource + " is empty or faulty and could not be parsed.\n"
                                + "The analysis values for pathways cannot be set.\n" +
                                "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                        eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
                        Console.error(getClass() + " ERROR: " + ex.getMessage());
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    if(!GWT.isProdMode() && GWT.isClient()){
                        Console.error(getClass() + " ERROR: " + exception.getMessage());
                    }

                    MessageObject msgObj = new MessageObject("The request for token=" + analysisToken +
                            "\nand resource=" + resource + " received an error instead of a valid response.\n"
                            + "The analysis values for pathways cannot be set.\n" +
                            "ERROR: " + exception.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                    eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
                }
            });

        }catch (RequestException ex) {
            MessageObject msgObj = new MessageObject("The requested analysis data for token=" + analysisToken +
                    "\nand resource=" + resource + " could not be received.\n"
                    + "The analysis values for pathways cannot be set.\n" +
                    "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
            eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
            Console.error(getClass() + " ERROR: " + ex.getMessage());
        }
    }

    private void getAnalysisDataForPathwaysWithReactions(Set<Long> pathwaysWithReactions){
        StringBuilder post = new StringBuilder();
        for (Long id : pathwaysWithReactions) {
            post.append(id).append(",");
        }
        post.delete(post.length()-1, post.length());

        String url = AnalysisHelper.URL_PREFIX + "/token/" + this.analysisToken + "/reactions/pathways?resource=" + this.resource;
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(post.toString(), new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    try{
                        JSONArray res = JSONParser.parseStrict(response.getText()).isArray();
                        Set<Long> hitReactions = new HashSet<Long>();
                        for (int i = 0; i < res.size(); i++) {
                            hitReactions.add(Long.valueOf(res.get(i).toString()));
                        }
                        view.highlightHitReactions(hitReactions);
                    }catch (Exception ex){
                        //AnalysisModelFactory, NullPointerException, IllegalArgumentException, JSONException
                        MessageObject msgObj = new MessageObject("The received object for token=" + analysisToken
                                + "\nand resource=" + resource + " is empty or faulty and could not be parsed.\n"
                                + "The analysis values for reactions cannot be set.\n" +
                                "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                        eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
                        Console.error(getClass() + " ERROR: " + ex.getMessage());
                    }

                }

                @Override
                public void onError(Request request, Throwable exception) {
                    if(!GWT.isProdMode() && GWT.isClient()){
                        Console.error(getClass() + " ERROR: " + exception.getMessage());
                    }

                    MessageObject msgObj = new MessageObject("The request for token=" + analysisToken +
                            "\nand resource=" + resource + " received an error instead of a valid response.\n"
                            + "The analysis values for reactions cannot be set.\n" +
                            "ERROR: " + exception.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                    eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
                }
            });

        }catch (RequestException ex) {
            MessageObject msgObj = new MessageObject("The requested analysis data for token=" + analysisToken +
                    "\nand resource=" + resource + " could not be received.\n"
                    + "The analysis values for reactions cannot be set.\n" +
                    "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
            eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
            Console.error(getClass() + " ERROR: " + ex.getMessage());
        }
    }

    private void getInstanceAncestors(final List<Event> path, final Long dbId) {
        String url = "/ReactomeRESTfulAPI/RESTfulWS/queryEventAncestors/" + dbId;
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    try {
                        JSONArray list = JSONParser.parseStrict(response.getText()).isArray();
                        Ancestors ancestors = new Ancestors(list);
                        setAncestorsListToExpand(path, ancestors);
                    } catch (Exception ex){
                        //ModelFactoryException, NullPointerException, IllegalArgumentException, JSONException
                        MessageObject msgObj = new MessageObject("The received data to expand the path down to 'dbId="
                                + dbId +"' is empty or faulty and could not be parsed.\n" +
                                "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                        errorMsg(msgObj);
                        Console.error(getClass() + " The received data is empty or faulty and could not be parsed.");
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    //eventBus.fireELVEvent(ELVEventType.HIERARCHY_INSTANCE_LOAD_ERROR, exception.getMessage());
                    MessageObject msgObj = new MessageObject("The request to expand the path down to 'dbId=" + dbId +
                            "' received an error instead of a valid response.\n" +
                            "ERROR: " + exception.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                    errorMsg(msgObj);
                    Console.error(getClass() +
                            " The request to expand the path received an error instead of a valid response.");
                }
            });
        }catch (RequestException ex) {
            MessageObject msgObj = new MessageObject("The path down to 'dbId=" + dbId +
                    "' could not be expanded.\n" +
                    "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
            errorMsg(msgObj);
            Console.error(getClass() +" The path could not be expanded.");
        }
    }

    @Override
    public void hierarchyReady() {
        eventBus.fireELVEvent(ELVEventType.HIERARCHY_READY);
        if(this.analysisToken!=null && !this.analysisToken.isEmpty()){
            AnalysisHelper.chooseResource(this.analysisToken, this);
        }
    }

    @Override
    public void onAnalysisTabResourceSelected(String resource) {
        this.resource = resource;
        this.view.clearAnalysisResult();
        this.getAnalysisData(this.view.getContainedEventIds(), this.view.getHierarchyPathwaysWithReactionsLoaded());
    }

    @Override
    public void onStateManagerAnalysisTokenSelected(String token) {
        this.view.clearAnalysisResult();
        this.analysisToken = token;
        AnalysisHelper.chooseResource(this.analysisToken, this);
    }

    @Override
    public void onStateManagerAnalysisTokenReset() {
        this.analysisToken = null;
        this.view.clearAnalysisResult();
    }

    @Override
    public void onStateManagerDatabaseObjectsSelected(List<Event> path, Pathway pathway, DatabaseObject databaseObject) {
        //This is here because the same pathway can be in different places in the hierarchy
        this.loadedDiagram = pathway;
        this.selectedDatabaseObject = databaseObject;
        if(databaseObject!=null && pathway!=databaseObject && databaseObject instanceof Event){
            this.getInstanceAncestors(path, databaseObject.getDbId());
        }else{
            this.getInstanceAncestors(path, pathway.getDbId());
        }
    }

    @Override
    public void onStateManagerInstancesInitialStateReached() {
        this.view.setInitialState();
    }

    @Override
    public void onStateManagerSpeciesSelected(Species species) {
        this.currentSpecies = species;
        this.view.showHierarchyForSpecies(species);
    }

    @Override
    public void onTourManagerTourCancelled() {
        this.view.tourFadeOut();
    }

    @Override
    public void onTourManagerTourFinished() {
        this.view.tourFadeOut();
    }

    @Override
    public void onTourManagerTourProgress(TourStage stage, Integer step) {
        if(stage==TourStage.SHOW_MODULES){
            if(step==2){
                this.view.tourFadeOut();
            }else{
                this.view.tourFadeIn();
            }
        }else  if(stage==TourStage.TEST_HIERARCHY){
            this.view.tourFadeOut();
        }else{
            this.view.tourFadeIn();
        }
    }

    @Override
    public void onTourManagerTourStarted() {
        this.view.tourFadeIn();
    }

    @Override
    public void pathwayExpanded(Pathway pathway) {
        try{
            if(pathway!=null && this.openingPath!=-1){
                //Next method will do something just if there are ancestors to expand yet
                this.expandPath();
            }
        }catch (Exception ex){
            MessageObject msgObj = new MessageObject("The path for pathway '" + pathway.getDisplayName() +
                    "' could not be expanded.\n" +
                    "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
            Console.error(getClass() + ex.getMessage());
            errorMsg(msgObj);
        }

    }

    @Override
    public void errorMsg(MessageObject msgObj) {
        eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
    }

    protected void setAncestorsListToExpand(List<Event> path, Ancestors ancestors) throws Exception {
        List<Path> candidatePaths;
        if(path.isEmpty()){
            candidatePaths = ancestors.getPathsContaining(this.loadedDiagram);
        }else{
            candidatePaths = ancestors.getPathsContaining(path);
        }

        if(!candidatePaths.isEmpty()){
            this.pathToExpand = candidatePaths.get(0);
        }else{
            this.pathToExpand = new Path();
        }

        if(!this.pathToExpand.isEmpty()){
            this.openingPath = 0;
            this.expandPath();
        }else{
            try{
                this.finishedExpandingPath(this.loadedDiagram, this.selectedDatabaseObject);
            }catch (Exception ex){
                Console.error(getClass() + ": No ancestors path found through the loaded diagram.");
                throw new Exception(getClass() + ": No ancestors path found through the loaded diagram", ex);
            }
        }
    }

    @Override
    public void onResourceChosen(String resource) {
        this.resource = resource;
        this.getAnalysisData(view.getContainedEventIds(), view.getHierarchyPathwaysWithReactionsLoaded());
    }

    @Override
    public void onEventHovered(EventHoverEvent e) {
        if(e.getSource() == this) return;
        System.out.println(e.getSource().getClass());
        System.out.println(getClass().getSimpleName() + " >> Fireworks " +  e.getPathway().getName() + " hovered");
    }

    @Override
    public void onEventHoveredReset() {
        System.out.println("Fireworks hovering reset");
    }
}
