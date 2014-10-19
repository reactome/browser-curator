package org.reactome.web.elv.client.details.tabs.overview.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import org.reactome.web.elv.client.common.Controller;
import org.reactome.web.elv.client.common.EventBus;
import org.reactome.web.elv.client.common.data.factory.ModelFactory;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.common.events.ELVEventType;
import org.reactome.web.elv.client.common.utils.Console;
import org.reactome.web.elv.client.details.tabs.DetailsTabView;
import org.reactome.web.elv.client.details.tabs.overview.view.OverviewView;
import org.reactome.web.elv.client.manager.messages.MessageObject;
import org.reactome.web.elv.client.manager.messages.MessageType;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class OverviewPresenter extends Controller implements OverviewView.Presenter {
    private OverviewView view;
    private DatabaseObject currentDatabaseObject;

    public OverviewPresenter(EventBus eventBus, OverviewView view) {
        super(eventBus);
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void getOverviewData() {
        String url = "/ReactomeRESTfulAPI/RESTfulWS/detailedView/DatabaseObject/" + currentDatabaseObject.getDbId();
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    try {
                        String text = response.getText();
                        JSONObject json = JSONParser.parseStrict(text).isObject();
                        DatabaseObject databaseObject = ModelFactory.getDatabaseObject(json);
                        view.setOverviewData(databaseObject);
                    }catch (Exception ex){
                        //ModelFactoryException, NullPointerException, IllegalArgumentException, JSONException
                        MessageObject msgObj = new MessageObject("The received object for '" + currentDatabaseObject.getDisplayName()
                                + "' is empty or faulty and could not be parsed.\n" +
                                "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                        eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
                        Console.error(getClass() + " ERROR: " + ex.getMessage());
                        view.setInitialState();
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    if(!GWT.isScript()){
                        Console.error(getClass() + " ERROR: " + exception.getMessage());
                    }

                    MessageObject msgObj = new MessageObject("The request for '" + currentDatabaseObject.getDisplayName()
                            + "' received an error instead of a valid response.\n" +
                            "ERROR: " + exception.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                    eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
                    view.setInitialState();
                }
            });
        } catch (RequestException ex) {
            MessageObject msgObj = new MessageObject("The requested detailed data for\n'" + currentDatabaseObject.getDisplayName()
                    + "' in the Overview could not be received.\n" +
                    "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
            eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
            Console.error(getClass() + " ERROR: " + ex.getMessage());
            view.setInitialState();
        }
    }

    @Override
    public DetailsTabView getView() {
        return this.view;
    }

    @Override
    public void setInstancesInitialState() {
        currentDatabaseObject = null;
        view.setInitialState();
    }

    @Override
    public void showInstanceDetails(Pathway pathway, DatabaseObject databaseObject) {
        currentDatabaseObject = databaseObject!=null?databaseObject:pathway;
        view.showInstanceDetails(pathway, databaseObject);
    }

    @Override
    public void showInstanceDetailsIfExists(Pathway pathway, DatabaseObject databaseObject) {
        this.view.showInstanceDetailsIfExists(pathway, databaseObject);
    }
}
