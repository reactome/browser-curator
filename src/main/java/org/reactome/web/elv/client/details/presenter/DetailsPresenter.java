package org.reactome.web.elv.client.details.presenter;

import com.google.gwt.core.client.GWT;
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
import org.reactome.web.elv.client.common.utils.Console;
import org.reactome.web.elv.client.details.events.*;
import org.reactome.web.elv.client.details.model.DetailsTabType;
import org.reactome.web.elv.client.details.tabs.DetailsTabView;
import org.reactome.web.elv.client.details.tabs.molecules.model.data.Molecule;
import org.reactome.web.elv.client.details.view.DetailsView;
import org.reactome.web.elv.client.manager.messages.MessageObject;
import org.reactome.web.elv.client.manager.messages.MessageType;
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
    public void onDataRequired(final DatabaseObject databaseObject) {
        final long dbId = databaseObject.getDbId();
        String url = "/ReactomeRESTfulAPI/RESTfulWS/detailedView/DatabaseObject/" + dbId;
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    try {
                        JSONObject json = JSONParser.parseStrict(response.getText()).isObject();
                        DatabaseObject databaseObject = ModelFactory.getDatabaseObject(json);
                        DataRequiredListener.getDataRequiredListener().setRequiredData(dbId, databaseObject);
                        eventBus.fireELVEvent(ELVEventType.DETAILED_VIEW_LOADED, databaseObject);
                    }catch (Exception ex){
                       //ModelFactoryException, NullPointerException, IllegalArgumentException, JSONException
                       MessageObject msgObj = new MessageObject("The received object for 'DbId=" + databaseObject.getDbId()
                                + "' is empty or faulty and could not be parsed.\n" +
                                "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                        eventBus.fireELVEvent(ELVEventType.INTERANL_MESSAGE, msgObj);
                        Console.error(getClass() + " ERROR: " + ex.getMessage());
                    }

                }

                @Override
                public void onError(Request request, Throwable exception) {
                    if(!GWT.isScript()){
                        Console.error(getClass() + " ERROR: " + exception.getMessage());
                    }

                    MessageObject msgObj = new MessageObject("The request for 'DbId=" + databaseObject.getDbId()
                            + "' received an error instead of a valid response.\n" +
                            "ERROR: " + exception.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                    eventBus.fireELVEvent(ELVEventType.INTERANL_MESSAGE, msgObj);
                }
            });
        }
        catch (RequestException ex) {
            MessageObject msgObj = new MessageObject("The requested data for 'DbId=" + databaseObject.getDbId()
                    + "' could not be received.\n" +
                    "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
            eventBus.fireELVEvent(ELVEventType.INTERANL_MESSAGE, msgObj);
            Console.error(getClass() + " ERROR: " + ex.getMessage());
        }
    }

    @Override
    public void onPathRequired(final Event event) {
    /* TODO Testing
    but never used because DataRequiredListener is used by DetailsPanel and there ancestorsRequired is unused */
        final long dbId = event.getDbId();
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
                        DataRequiredListener.getDataRequiredListener().setRequiredAncestors(dbId, ancestors);
                    }catch (Exception ex){
                        //ModelFactoryException, NullPointerException, IllegalArgumentException, JSONException
                        MessageObject msgObj = new MessageObject("The received object for 'DbId=" + event.getDbId()
                                + "' is empty or faulty and could not be parsed into a path or hierarchy.\n" +
                                "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                        eventBus.fireELVEvent(ELVEventType.INTERANL_MESSAGE, msgObj);
                        Console.error(getClass() + " ERROR: " + ex.getMessage());
                    }

                }

                @Override
                public void onError(Request request, Throwable exception) {
                    if(!GWT.isScript()){
                        Console.error(getClass() + " ERROR: " + exception.getMessage());
                    }

                    MessageObject msgObj = new MessageObject("The PathRequired request for 'DbId=" + event.getDbId()
                            + "' received an error instead of a valid response.\n" +
                            "ERROR: " + exception.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                    eventBus.fireELVEvent(ELVEventType.INTERANL_MESSAGE, msgObj);
                }
            });
        } catch (RequestException ex) {
            MessageObject msgObj = new MessageObject("The requested data for 'DbId=" + event.getDbId()
                    + "' could not be received.\n" +
                    "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
            eventBus.fireELVEvent(ELVEventType.INTERANL_MESSAGE, msgObj);
            Console.error(getClass() + " ERROR: " + ex.getMessage());
        }
    }

    @Override
    public void onMoleculeDataRequired(final Molecule molecule){
        String url = "/ReactomeRESTfulAPI/RESTfulWS/queryById/ReferenceEntity/" + molecule.getDbId();
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    try{
                        JSONObject json  = JSONParser.parseStrict(response.getText()).isObject();
                        Molecule molecule = new Molecule(ModelFactory.getDatabaseObject(json).getSchemaClass(), json);

                        DataRequiredListener.getDataRequiredListener().setRequiredMoleculeData(molecule);
                    }catch (Exception ex){
                        //ModelFactoryException, NullPointerException, IllegalArgumentException, JSONException
                        MessageObject msgObj = new MessageObject("The received object for Molecule '" + molecule.getDisplayName()
                                + "' is empty or faulty and could not be parsed.\n" +
                                "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                        eventBus.fireELVEvent(ELVEventType.INTERANL_MESSAGE, msgObj);
                        Console.error(getClass() + " ERROR: " + ex.getMessage());
                        DataRequiredListener.getDataRequiredListener().setRequiredMoleculeData(molecule);
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    if(!GWT.isScript()){
                        Console.error(getClass() + " ERROR: " + exception.getMessage());
                    }

                    MessageObject msgObj = new MessageObject("The request for Molecule '" + molecule.getDisplayName()
                            + "' received an error instead of a valid response.\n" +
                            "ERROR: " + exception.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                    eventBus.fireELVEvent(ELVEventType.INTERANL_MESSAGE, msgObj);
                    DataRequiredListener.getDataRequiredListener().setRequiredMoleculeData(molecule);
                }
            });
        }catch (RequestException ex) {
            MessageObject msgObj = new MessageObject("The requested detailed data for Molecule \n'" + molecule.getDisplayName()
                    + "' could not be received.\n" +
                    "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
            eventBus.fireELVEvent(ELVEventType.INTERANL_MESSAGE, msgObj);
            Console.error(getClass() + " ERROR: " + ex.getMessage());
            DataRequiredListener.getDataRequiredListener().setRequiredMoleculeData(molecule);
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
                    try{
                        JSONArray list = JSONParser.parseStrict(response.getText()).isArray();
                        List<LiteratureReference> references = new ArrayList<LiteratureReference>();
                        for(int i=0; i<list.size(); ++i){
                            JSONObject object = list.get(i).isObject();
                            references.add((LiteratureReference) ModelFactory.getDatabaseObject(object));
                        }
                        DataRequiredListener.getDataRequiredListener().setRequiredReferences(dbId, references);
                    }catch (Exception ex){
                        //ModelFactoryException, NullPointerException, IllegalArgumentException, JSONException
                        MessageObject msgObj = new MessageObject("The received References object for 'DbId=" + dbId
                                + "' is empty or faulty and could not be parsed.\n" +
                                "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                        eventBus.fireELVEvent(ELVEventType.INTERANL_MESSAGE, msgObj);
                        Console.error(getClass() + " ERROR: " + ex.getMessage());
                        DataRequiredListener.getDataRequiredListener().setRequiredReferences(dbId, new ArrayList<LiteratureReference>());
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    if(!GWT.isScript()){
                        Console.error(getClass() + " ERROR: " + exception.getMessage());
                    }

                    MessageObject msgObj = new MessageObject("The request for 'DbId=" + dbId
                            + "' received an error instead of a valid response.\n" +
                            "ERROR: " + exception.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                    eventBus.fireELVEvent(ELVEventType.INTERANL_MESSAGE, msgObj);
                    DataRequiredListener.getDataRequiredListener().setRequiredReferences(dbId, new ArrayList<LiteratureReference>());
                }
            });
        }
        catch (RequestException ex) {
            MessageObject msgObj = new MessageObject("The requested references data for 'DbId=" + dbId
                    + "' could not be received.\n" +
                    "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
            eventBus.fireELVEvent(ELVEventType.INTERANL_MESSAGE, msgObj);
            Console.error(getClass() + " ERROR: " + ex.getMessage());
            DataRequiredListener.getDataRequiredListener().setRequiredReferences(dbId, new ArrayList<LiteratureReference>());
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
