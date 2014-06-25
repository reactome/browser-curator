package org.reactome.web.elv.client.manager.state;

import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import org.reactome.web.elv.client.common.data.factory.ModelFactory;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Event;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.common.model.Path;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class StableIdentifierLoader implements PathRetriever.PathHandler {

    public interface StableIdentifierLoadedHandler {
        public void onStableIdentifierLoaded(AdvancedState advancedState);
    }

    private StableIdentifierLoadedHandler handler;
    private AdvancedState advancedState = new AdvancedState();

    public StableIdentifierLoader(String stableIdentifier, StableIdentifierLoadedHandler handler){
        this.handler = handler;
        String url = "/ReactomeRESTfulAPI/RESTfulWS/detailedView/DatabaseObject/" + stableIdentifier;
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    String text = response.getText();
                    JSONObject json = JSONParser.parseStrict(text).isObject();
                    DatabaseObject databaseObject = ModelFactory.getDatabaseObject(json);
                    if(databaseObject instanceof Event){
                        loadData((Event) databaseObject);
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    //TODO
                }
            });
        } catch (RequestException ex) {
            //TODO
        }
    }

    private void loadData(Event event){
        advancedState.setSpecies(event.getSpecies().get(0));
        advancedState.setInstance(event);
        if(event instanceof Pathway){
            Pathway pathway = (Pathway) event;
            if(pathway.getHasDiagram()){
                advancedState.setPathway(pathway);
                handler.onStableIdentifierLoaded(advancedState);
            }else{
                advancedState.setInstance(event);
                PathRetriever.retrieveAncestors(event, this);
            }
        }else{
            PathRetriever.retrieveAncestors(event, this);
        }
    }

    @Override
    public void onPathsRetrieved(List<Path> paths) {
        for (Path path : paths) {
            Pathway pathway = path.getLastPathwayWithDiagram();
            if(pathway!=null){
                advancedState.setPathway(pathway);
                handler.onStableIdentifierLoaded(advancedState);
                return;
            }
        }
    }
}
