package org.reactome.web.elv.client.details.tabs.structures.presenter;

import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import org.reactome.web.elv.client.common.Controller;
import org.reactome.web.elv.client.common.EventBus;
import org.reactome.web.elv.client.common.data.factory.ModelFactory;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.common.data.model.PhysicalEntity;
import org.reactome.web.elv.client.common.data.model.ReferenceSequence;
import org.reactome.web.elv.client.details.tabs.DetailsTabView;
import org.reactome.web.elv.client.details.tabs.structures.view.StructuresView;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class StructuresPresenter extends Controller implements StructuresView.Presenter  {
    private StructuresView view;

    public StructuresPresenter(EventBus eventBus, StructuresView view) {
        super(eventBus);
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void getProteinAccessions(PhysicalEntity physicalEntity, final Long respId) {
        String url = "/ReactomeRESTfulAPI/RESTfulWS/referenceEntity/" + physicalEntity.getDbId();
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    JSONArray jsonArray = JSONParser.parseStrict(response.getText()).isArray();
                    List<ReferenceSequence> referenceSequenceList = new LinkedList<ReferenceSequence>();
                    for(int i=0; i<jsonArray.size(); i++){
                        JSONObject json = jsonArray.get(i).isObject();
                        DatabaseObject databaseObject = ModelFactory.getDatabaseObject(json);
                        if(databaseObject instanceof ReferenceSequence){
                            referenceSequenceList.add((ReferenceSequence) ModelFactory.getDatabaseObject(json));
                        }
                    }
                    //if the list is empty, the view will show the proper message, otherwise the objects need
                    //to be filled in order to get required info
                    if(referenceSequenceList.isEmpty()){
                        view.setProteinAccessions(referenceSequenceList, respId);
                    }else{
                        fillReferenceSequenceObjects(referenceSequenceList, respId);
                    }
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
                    JSONArray jsonArray = JSONParser.parseStrict(response.getText()).isArray();
                    List<ReferenceSequence> referenceSequenceList = new LinkedList<ReferenceSequence>();
                    for(int i=0; i<jsonArray.size(); i++){
                        JSONObject json = jsonArray.get(i).isObject();
                        referenceSequenceList.add((ReferenceSequence) ModelFactory.getDatabaseObject(json));
                    }
                    view.setProteinAccessions(referenceSequenceList, respId);
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
