package org.reactome.web.elv.client.details.presenter;

import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import org.reactome.web.elv.client.common.Controller;
import org.reactome.web.elv.client.common.EventBus;
import org.reactome.web.elv.client.common.data.factory.ModelFactory;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Event;
import org.reactome.web.elv.client.common.data.model.LiteratureReference;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.common.events.ELVEventType;
import org.reactome.web.elv.client.common.model.Ancestors;
import org.reactome.web.elv.client.common.model.Path;
import org.reactome.web.elv.client.details.events.*;
import org.reactome.web.elv.client.details.model.DetailsTabType;
import org.reactome.web.elv.client.details.tabs.DetailsTabView;
import org.reactome.web.elv.client.details.tabs.molecules.model.data.Molecule;
import org.reactome.web.elv.client.details.view.DetailsView;
import org.reactome.web.elv.client.manager.tour.TourStage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DetailsPresenter extends Controller implements DetailsView.Presenter, DataRequiredHandler, InstanceSelectedHandler {

	private DetailsView view;

    private List<DetailsTabView.Presenter> tabsPresenter;

    private Pathway diagram;
    private DatabaseObject databaseObject;
    private DetailsTabType selectedTabType = DetailsTabType.getDefault();

	public DetailsPresenter(EventBus eventBus, DetailsView detailsView) {
		super(eventBus);
        this.view = detailsView;
        this.diagram = null;
        this.databaseObject = null;
        this.tabsPresenter = TabsFactory.getDetailsTabs(eventBus);

        this.view.setPresenter(this);

        //Needed because the view implementation will be listening to all the OverviewPanels for required data
        DataRequiredListener.getDataRequiredListener().setDataRequiredHandler(this);

        //Needed because the view implementation will be listening to all the DetailsPanel for instance selected
        InstanceSelectedListener.getInstanceSelectedListener().setInstanceSelectedHandler(this);
    }

    @Override
    public List<DetailsTabView.Presenter> getDetailsTabs() {
        return tabsPresenter;
    }

    @Override
    public void onStateManagerDatabaseObjectsSelected(List<Event> path, Pathway pathway, DatabaseObject databaseObject) {
        this.diagram = pathway;
        this.databaseObject = databaseObject;

        for (DetailsTabView.Presenter tab : tabsPresenter){
            tab.showInstanceDetailsIfExists(pathway, databaseObject);
        }
        showInstanceDetails(DetailsTabType.getIndex(selectedTabType));
    }

    @Override
    public void onStateManagerDetailsTabSelected(DetailsTabType tab) {
        if(!tab.equals(selectedTabType))
            view.selectTab(DetailsTabType.getIndex(tab));
    }

    @Override
    public void onStateManagerInstancesInitialStateReached() {
        this.diagram = null;
        this.databaseObject = null;
        for (DetailsTabView.Presenter tab : tabsPresenter) {
            tab.setInstancesInitialState();
        }
    }

    @Override
    public void onTourManagerTourCancelled() {
        this.view.tourFadeOut();
    }

    @Override
    public void onTourManagerTourFinished() {
        this.view.selectTab(0);
        this.view.tourFadeOut();
    }

    @Override
    public void onTourManagerTourProgress(TourStage stage, Integer step) {
        if(stage==TourStage.SHOW_MODULES){
            if(step==4){
                this.view.tourFadeOut();
            }else{
                this.view.tourFadeIn();
            }
        }else  if(stage==TourStage.TEST_DETAILS){
            switch (step){
                case 1: this.view.selectTab(DetailsTabType.getIndex(DetailsTabType.OVERVIEW));                  break;
                case 3: this.view.selectTab(DetailsTabType.getIndex(DetailsTabType.PARTICIPATING_MOLECULES));   break;
                case 4: this.view.selectTab(DetailsTabType.getIndex(DetailsTabType.STRUCTURES));                break;
                case 5: this.view.selectTab(DetailsTabType.getIndex(DetailsTabType.EXPRESSION));                break;
                case 6: this.view.selectTab(DetailsTabType.getIndex(DetailsTabType.ANALYSIS));                  break;
                case 7: this.view.selectTab(DetailsTabType.getIndex(DetailsTabType.PARTICIPATING_PROCESSES));   break;
                case 8: this.view.selectTab(DetailsTabType.getIndex(DetailsTabType.DOWNLOADS));                 break;
            }
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
    public void showInstanceDetails(Integer index) {
        DetailsTabView.Presenter tab = tabsPresenter.get(index);
        DetailsTabType tabType = tab.getView().getDetailTabType();

        if(!selectedTabType.equals(tabType)){
            selectedTabType = tabType;
            eventBus.fireELVEvent(ELVEventType.DETAILS_PANEL_TAB_CHANGED, tabType);
        }

        if(this.diagram!=null){ //at least diagram has to exist, otherwise nothing can be shown
            tab.showInstanceDetails(this.diagram, this.databaseObject);
        }else{
            tab.setInstancesInitialState();
        }
    }

    @Override
    public void onDataRequired(DatabaseObject databaseObject) {
        final long dbId = databaseObject.getDbId();
        String url = "/ReactomeRESTfulAPI/RESTfulWS/detailedView/DatabaseObject/" + dbId;
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    JSONObject json = JSONParser.parseStrict(response.getText()).isObject();
                    DatabaseObject databaseObject = ModelFactory.getDatabaseObject(json);
                    DataRequiredListener.getDataRequiredListener().setRequiredData(dbId, databaseObject);
                    eventBus.fireELVEvent(ELVEventType.DETAILED_VIEW_LOADED, databaseObject);
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    //TODO
                }
            });
        }
        catch (RequestException ex) {
            //TODO
        }
    }

    @Override
    public void onPathRequired(Event event) {
        final long dbId = event.getDbId();
        String url = "/ReactomeRESTfulAPI/RESTfulWS/queryEventAncestors/" + dbId;
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    JSONArray list = JSONParser.parseStrict(response.getText()).isArray();
                    Ancestors ancestors = new Ancestors(list);
                    DataRequiredListener.getDataRequiredListener().setRequiredAncestors(dbId, ancestors);
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    //TODO
                }
            });
        } catch (RequestException e) {
            //TODO
        }
    }

    @Override
    public void onMoleculeDataRequired(Molecule molecule){
        String url = "/ReactomeRESTfulAPI/RESTfulWS/queryById/ReferenceEntity/" + molecule.getDbId();
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {

            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    JSONObject json  = JSONParser.parseStrict(response.getText()).isObject();
                    Molecule molecule = new Molecule(ModelFactory.getDatabaseObject(json).getSchemaClass(), json);

                    DataRequiredListener.getDataRequiredListener().setRequiredMoleculeData(molecule);

                    //eventBus.fireELVEvent(ELVEventType.MOLECULES_VIEW_LOADED, databaseObject);
                    //view.setMoleculesDetails(fullMolecules);
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    //TODO
                }
            });
        }catch (RequestException ex) {
            //TODO
        }
    }

    @Override
    public void onReferencesRequired(final Long dbId) {
        String url = "/ReactomeRESTfulAPI/RESTfulWS/queryReferences/" + dbId;
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    JSONArray list = JSONParser.parseStrict(response.getText()).isArray();
                    List<LiteratureReference> references = new ArrayList<LiteratureReference>();
                    for(int i=0; i<list.size(); ++i){
                        JSONObject object = list.get(i).isObject();
                        references.add((LiteratureReference) ModelFactory.getDatabaseObject(object));
                    }
                    DataRequiredListener.getDataRequiredListener().setRequiredReferences(dbId, references);
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    //TODO
                }
            });
        }
        catch (RequestException ex) {
            //TODO
        }
    }

    @Override
    public void instanceSelected(DatabaseObject databaseObject) {
        eventBus.fireELVEvent(ELVEventType.OVERVIEW_ITEM_SELECTED, databaseObject);
    }

    @Override
    public void eventSelected(Path path, Pathway pathway, Event event) {
        DetailsSelection selection = new DetailsSelection(path, pathway, event);
        eventBus.fireELVEvent(ELVEventType.OVERVIEW_EVENT_SELECTED, selection);
    }
}
