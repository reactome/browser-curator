package org.reactome.web.elv.client.details.tabs.expression.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import org.reactome.web.elv.client.common.Controller;
import org.reactome.web.elv.client.common.EventBus;
import org.reactome.web.elv.client.common.data.factory.ModelFactory;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.common.data.model.ReferenceSequence;
import org.reactome.web.elv.client.common.events.ELVEventType;
import org.reactome.web.elv.client.common.utils.Console;
import org.reactome.web.elv.client.details.tabs.DetailsTabView;
import org.reactome.web.elv.client.details.tabs.expression.view.ExpressionView;
import org.reactome.web.elv.client.manager.messages.MessageObject;
import org.reactome.web.elv.client.manager.messages.MessageType;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ExpressionPresenter extends Controller implements ExpressionView.Presenter {
    private ExpressionView view;

    public ExpressionPresenter(EventBus eventBus, ExpressionView view) {
        super(eventBus);
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void getDetailedView(final DatabaseObject databaseObject, final Long respId) {
        String url = "/ReactomeRESTfulAPI/RESTfulWS/detailedView/DatabaseObject/" + databaseObject.getDbId();
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    try{
                        String text = response.getText();
                        JSONObject json = JSONParser.parseStrict(text).isObject();
                        DatabaseObject databaseObject = ModelFactory.getDatabaseObject(json);
                        view.setDetailedView(databaseObject, respId);
                    }catch (Exception ex){
                        //ModelFactoryException, NullPointerException, IllegalArgumentException, JSONException
                        MessageObject msgObj = new MessageObject("The received object containing data for the Expression of \n'"
                                + databaseObject.getDisplayName() + "' is empty or faulty and could not be parsed.\n" +
                                "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                        eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
                        Console.error(getClass() + " ERROR: " + ex.getMessage());
                        view.setProteinAccessions(new LinkedList<ReferenceSequence>(), respId);
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    if(!GWT.isScript()){
                        Console.error(getClass() + " ERROR: " + exception.getMessage());
                    }

                    MessageObject msgObj = new MessageObject("The request for '" + databaseObject.getDisplayName() +
                            "' in the Expression Tab received an error instead of a valid response.\n" +
                            "ERROR: " + exception.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                    eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
                    view.setProteinAccessions(new LinkedList<ReferenceSequence>(), respId);
                }
            });
        } catch (RequestException ex) {
            view.setInitialState();
            MessageObject msgObj = new MessageObject("The required data for the Expression of\n'" +
                     databaseObject.getDisplayName() + "' could not be received.\n" +
                    "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
            eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
            Console.error(getClass() + " ERROR: " + ex.getMessage());
        }
    }

    @Override
    public void getProteinAccessions(final DatabaseObject databaseObject, final Long respId) {
        String url = "/ReactomeRESTfulAPI/RESTfulWS/referenceEntity/" + databaseObject.getDbId();
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    try {
                        JSONArray jsonArray = JSONParser.parseStrict(response.getText()).isArray();
                        List<ReferenceSequence> referenceSequenceList = new LinkedList<ReferenceSequence>();
                        for(int i=0; i<jsonArray.size(); i++){
                            JSONObject json = jsonArray.get(i).isObject();
                            DatabaseObject databaseObject = ModelFactory.getDatabaseObject(json);
                            if(databaseObject instanceof ReferenceSequence){
                                referenceSequenceList.add((ReferenceSequence) databaseObject);
                            }
                        }
                        //if the list is empty, the view will show the proper message, otherwise the objects need
                        //to be filled in order to get required info
                        if(referenceSequenceList.isEmpty()){
                            view.setProteinAccessions(referenceSequenceList, respId);
                        }else{
                            fillReferenceSequenceObjects(referenceSequenceList, respId);
                        }
                    }catch (Exception ex){
                        //ModelFactoryException, NullPointerException, IllegalArgumentException, JSONException
                        MessageObject msgObj = new MessageObject("The received object containing data for the Expression of \n'"
                                + databaseObject.getDisplayName() + "' is empty or faulty and could not be parsed.\n" +
                                "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                        eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
                        Console.error(getClass() + " ERROR: " + ex.getMessage());
                        view.setProteinAccessions(new LinkedList<ReferenceSequence>(), respId);
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    if(!GWT.isScript()){
                        Console.error(getClass() + " ERROR: " + exception.getMessage());
                    }
                    MessageObject msgObj = new MessageObject("The request for '" + databaseObject.getDisplayName() +
                            "' in the Expression Tab received an error instead of a valid response.\n" +
                            "ERROR: " + exception.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                    eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
                    view.setProteinAccessions(new LinkedList<ReferenceSequence>(), respId);
                }
            });
        } catch (RequestException ex) {
            view.setProteinAccessions(new LinkedList<ReferenceSequence>(), respId);
            MessageObject msgObj = new MessageObject("The required data for the Expression of\n'" +
                    databaseObject.getDisplayName() + "' could not be received.\n" +
                    "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
            eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
            Console.error(getClass() + " ERROR: " + ex.getMessage());
        }
    }

    private void fillReferenceSequenceObjects(List<ReferenceSequence> referenceSequenceList, final Long respId){
        String url = "/ReactomeRESTfulAPI/RESTfulWS/queryByIds/ReferenceSequence";
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            StringBuilder sb = new StringBuilder("ID=");
            for (ReferenceSequence referenceSequence : referenceSequenceList) {
                sb.append(referenceSequence.getDbId());
                sb.append(",");
            }
            requestBuilder.sendRequest(sb.toString(), new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    try{
                        JSONArray jsonArray = JSONParser.parseStrict(response.getText()).isArray();
                        List<ReferenceSequence> referenceSequenceList = new LinkedList<ReferenceSequence>();
                        for(int i=0; i<jsonArray.size(); i++){
                            JSONObject json = jsonArray.get(i).isObject();
                            referenceSequenceList.add((ReferenceSequence) ModelFactory.getDatabaseObject(json));
                        }
                        view.setProteinAccessions(referenceSequenceList, respId);
                    }catch (Exception ex){
                        //ModelFactoryException, NullPointerException, IllegalArgumentException, JSONException
                        MessageObject msgObj = new MessageObject("The received object containing data for the Expression" +
                                " Tab is empty or faulty and could not be parsed.\n" +
                                "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                        eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
                        Console.error(getClass() + " ERROR: " + ex.getMessage());
                        view.setProteinAccessions(new LinkedList<ReferenceSequence>(), respId);
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    if(!GWT.isScript()){
                        Console.error(getClass() + " ERROR: " + exception.getMessage());
                    }

                    MessageObject msgObj = new MessageObject("The request received an error instead of a valid response.\n" +
                            "ERROR: " + exception.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                    eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
                    view.setProteinAccessions(new LinkedList<ReferenceSequence>(), respId);
                }
            });
        } catch (RequestException ex) {
            view.setProteinAccessions(new LinkedList<ReferenceSequence>(), respId);
            MessageObject msgObj = new MessageObject("The required data for the Expression could not be received.\n" +
                    "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
            eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
            Console.error(getClass() + " ERROR: " + ex.getMessage());
        }
    }

    @Override
    public DetailsTabView getView() {
        return this.view;
    }

    @Override
    public void setInstancesInitialState() {
        this.view.setInitialState();
    }

    @Override
    public void showInstanceDetails(Pathway pathway, DatabaseObject databaseObject) {
        this.view.showInstanceDetails(pathway, databaseObject);
    }

    @Override
    public void showInstanceDetailsIfExists(Pathway pathway, DatabaseObject databaseObject) {
        this.view.showInstanceDetailsIfExists(pathway, databaseObject);
    }
}
