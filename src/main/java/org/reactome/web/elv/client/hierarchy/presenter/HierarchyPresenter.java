package org.reactome.web.elv.client.hierarchy.presenter;

import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import org.reactome.web.elv.client.common.Controller;
import org.reactome.web.elv.client.common.EventBus;
import org.reactome.web.elv.client.common.analysis.factory.AnalysisModelException;
import org.reactome.web.elv.client.common.analysis.factory.AnalysisModelFactory;
import org.reactome.web.elv.client.common.analysis.helper.AnalysisHelper;
import org.reactome.web.elv.client.common.analysis.model.AnalysisResult;
import org.reactome.web.elv.client.common.analysis.model.PathwaySummary;
import org.reactome.web.elv.client.common.data.factory.ModelFactory;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Event;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.common.data.model.Species;
import org.reactome.web.elv.client.common.events.ELVEventType;
import org.reactome.web.elv.client.common.model.Ancestors;
import org.reactome.web.elv.client.common.model.Path;
import org.reactome.web.elv.client.common.utils.Console;
import org.reactome.web.elv.client.hierarchy.model.HierarchySelection;
import org.reactome.web.elv.client.hierarchy.view.HierarchyView;
import org.reactome.web.elv.client.manager.tour.TourStage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class HierarchyPresenter extends Controller implements HierarchyView.Presenter, AnalysisHelper.ResourceChosenHandler {

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

        this.pathToExpand = new Path();
        this.openingPath = -1;
        this.loadedDiagram = null;
        this.selectedDatabaseObject = null;
	}

    @Override
    public void eventSelected(Path path, Pathway pathway, Event event) {
        HierarchySelection selection = new HierarchySelection(path, pathway, event);
        this.eventBus.fireELVEvent(ELVEventType.HIERARCHY_EVENT_SELECTED, selection);
    }

    @Override
    public void eventChildrenRequired(final Path path, Event event) {
        String url = "/ReactomeRESTfulAPI/RESTfulWS/queryById/Pathway/" + event.getDbId();
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    JSONObject json = JSONParser.parseStrict(response.getText()).isObject();
                    Pathway pathway = new Pathway(json);
                    List<Event> children = pathway.getHasEvent();
                    view.loadItemChildren(currentSpecies, path, pathway, children);
                    //Next bit is to load analysis details in the hierarchy items by demand
                    Set<Long> dbIds = new HashSet<Long>();
                    for (Event child : children) {
                        dbIds.add(child.getDbId());
                    }
                    getAnalysisData(dbIds);
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    //eventBus.fireELVEvent(ELVEventType.HIERARCHY_CHILDREN_LOAD_ERROR, exception.getMessage());
                }
            });
        } catch (RequestException ex) {
            //eventBus.fireELVEvent(ELVEventType.HIERARCHY_CHILDREN_LOAD_ERROR, exception.getMessage());
        }
    }

    private void expandPath(){
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

    private void finishedExpandingPath(Pathway pathway, DatabaseObject databaseObject){
        this.openingPath = -1;
        if(databaseObject instanceof Event){
            this.view.highlightPath(this.pathToExpand, pathway, (Event) databaseObject);
        }else{
            this.view.highlightPath(this.pathToExpand, pathway, null);
        }
    }

    @Override
    public void frontPageItemsRequired(Species species) {
        String speciesName = species.getDisplayName().replaceAll(" ", "+");
        String url = "/ReactomeRESTfulAPI/RESTfulWS/frontPageItems/" + speciesName;
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    JSONArray list = JSONParser.parseStrict(response.getText()).isArray();
                    List<Event> children = new ArrayList<Event>();
                    for(int i=0; i<list.size(); ++i){
                        Event child = (Event) ModelFactory.getDatabaseObject(list.get(i).isObject());
                        children.add(child);
                    }
                    view.loadItemChildren(currentSpecies, null, null, children);
                    hierarchyReady();
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    eventBus.fireELVEvent(ELVEventType.DATA_MANAGER_LOAD_ERROR, exception.getMessage());
                }
            });
        } catch (RequestException ex) {
            eventBus.fireELVEvent(ELVEventType.DATA_MANAGER_LOAD_ERROR, ex.getMessage());
        }
    }

    @Override
    public void getAnalysisData(Set<Long> eventIds) {
        if(this.analysisToken==null || eventIds.isEmpty()) return;

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
                    } catch (AnalysisModelException e) {
                        Console.error(e.getMessage());
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    Console.error(exception.getMessage());
                }
            });
        }catch (RequestException ex) {
            Console.error(ex.getMessage());
        }
    }

    private void getInstanceAncestors(final List<Event> path, Long dbId) {
        String url = "/ReactomeRESTfulAPI/RESTfulWS/queryEventAncestors/" + dbId;
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    JSONArray list = JSONParser.parseStrict(response.getText()).isArray();
                    Ancestors ancestors = new Ancestors(list);
                    setAncestorsListToExpand(path, ancestors);
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    //eventBus.fireELVEvent(ELVEventType.HIERARCHY_INSTANCE_LOAD_ERROR, exception.getMessage());
                }
            });
        }
        catch (RequestException ex) {
            //eventBus.fireELVEvent(ELVEventType.HIERARCHY_INSTANCE_LOAD_ERROR, exception.getMessage());
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
        this.getAnalysisData(this.view.getContainedEventIds());
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
        if(pathway!=null && this.openingPath!=-1){
            //Next method will do something just if there are ancestors to expand yet
            this.expandPath();
        }
    }

    protected void setAncestorsListToExpand(List<Event> path, Ancestors ancestors) {
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
            this.finishedExpandingPath(this.loadedDiagram, this.selectedDatabaseObject);
            Console.error(getClass() + " no ancestors path found through the loaded diagram");
        }
    }

    @Override
    public void onResourceChosen(String resource) {
        this.resource = resource;
        this.getAnalysisData(view.getContainedEventIds());
    }
}
