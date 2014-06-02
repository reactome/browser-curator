package org.reactome.web.elv.client.details.tabs.molecules.presenter;

import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import org.reactome.web.elv.client.common.Controller;
import org.reactome.web.elv.client.common.EventBus;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.common.events.ELVEventType;
import org.reactome.web.elv.client.common.model.Pair;
import org.reactome.web.elv.client.details.tabs.DetailsTabView;
import org.reactome.web.elv.client.details.tabs.molecules.view.MoleculesView;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class MoleculePresenter extends Controller implements MoleculesView.Presenter { //}, RequestCallback {

    private MoleculesView view;

    private DatabaseObject currentDatabaseObject;

    public MoleculePresenter(EventBus eventBus, MoleculesView view) {
        super(eventBus);
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void downloadFormatedData(List<String> types, List<String> fields, String format) {
        String url = "/ReactomeRESTfulAPI/RESTfulWS/participatingMolecules/download/" + currentDatabaseObject.getDbId();
        JSONObject json = getParameters(types, fields, format);
        view.submitForm(url, json);
        eventBus.fireELVEvent(ELVEventType.MOLECULES_DATA_DOWNLOADED, null);
    }

    @Override
    public void getFormatedData(List<String> types, List<String> fields, String format) {
        String url = "/ReactomeRESTfulAPI/RESTfulWS/participatingMolecules/export/" + currentDatabaseObject.getDbId();
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, url);
        requestBuilder.setHeader("Content-Type", "application/x-www-form-urlencoded");
        //requestBuilder.setHeader("Content-Type", "text/plain");
        requestBuilder.setHeader("Accept", "text/plain");

        JSONObject json = getParameters(types, fields, format);
        try {
            eventBus.fireELVEvent(ELVEventType.MOLECULES_DATA_QUERIED, new Pair<DatabaseObject, JSONObject>(currentDatabaseObject, json));
            requestBuilder.sendRequest("params="+json.toString(), new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    view.showExportResults(response.getText());
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    eventBus.fireELVEvent(ELVEventType.MOLECULES_DATA_QUERY_ERROR, exception.getMessage());
                }
            });
        }
        catch (RequestException ex) {
            eventBus.fireELVEvent(ELVEventType.MOLECULES_DATA_QUERY_ERROR, ex.getMessage());
        }
    }

    @Override
    public void getParticipatingMolecules() {
        String url = "/ReactomeRESTfulAPI/RESTfulWS/participatingMolecules/" + currentDatabaseObject.getDbId();
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    String text = response.getText();
                    JSONObject json = JSONParser.parseStrict(text).isObject();
                    eventBus.fireELVEvent(ELVEventType.MOLECULES_INSTANCE_LOADED, new Pair<DatabaseObject, JSONObject>(currentDatabaseObject, json));
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    eventBus.fireELVEvent(ELVEventType.MOLECULES_INSTANCE_LOAD_ERROR, exception.getMessage());
                }
            });
        }
        catch (RequestException ex) {
            eventBus.fireELVEvent(ELVEventType.MOLECULES_INSTANCE_LOAD_ERROR, ex.getMessage());
        }
    }

    @Override
    public DetailsTabView getView() {
        return this.view;
    }

    @Override
    public void onMoleculesInstanceLoaded(DatabaseObject databaseObject, JSONObject json) {
        view.setParticipatingMolecules(databaseObject, json);
    }

    @Override
    public void setInstancesInitialState() {
        //Nothing here
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

    private JSONObject getParameters(List<String> types, List<String> fields, String format){
        JSONObject json = new JSONObject();

        JSONArray typesAux = new JSONArray();
        for(int i=0; i<types.size(); ++i){
            typesAux.set(i, new JSONString(types.get(i)));
        }
        json.put("types", typesAux);

        JSONArray fieldsAux = new JSONArray();
        for(int i=0; i<fields.size(); ++i){
            fieldsAux.set(i, new JSONString(fields.get(i)));
        }
        json.put("fields", fieldsAux);

        json.put("format", new JSONString(format));

        return json;
    }
}
