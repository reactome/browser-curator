package org.reactome.web.elv.client.manager.state;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import org.reactome.web.elv.client.common.data.model.Event;
import org.reactome.web.elv.client.common.model.Ancestors;
import org.reactome.web.elv.client.common.model.Path;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */

public abstract class PathRetriever {
    public interface PathHandler extends EventHandler {
        void onPathsRetrieved(List<Path> paths);
    }

    /**
     * Checks the given ancestors list to get the first path without orphan pathways
     * @param list ancestors containing multiple paths through a given pathway
     * @return the first path without orphan pathways
     */
    private static List<Path> getPathsWithoutOrphanPathways(List<Path> list){
        List<Path> rtn = new LinkedList<Path>();
        for (Path path : list) {
            if(path.rootHasDiagram()){
                rtn.add(path);
            }
        }
        return rtn;
    }

    public static void retrieveAncestors(final Event event, final PathHandler handler){
        String url = "/ReactomeRESTfulAPI/RESTfulWS/queryEventAncestors/" + event.getDbId();
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    try {
                        JSONArray list = JSONParser.parseStrict(response.getText()).isArray();
                        Ancestors ancestors = new Ancestors(list);
                        List<Path> paths = getPathsWithoutOrphanPathways(ancestors.getPathsContaining(event));
                        handler.onPathsRetrieved(paths);
                    }catch (Exception ex){
                        //ToDo: Look into new Error Handling
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    //ToDo: Look into new Error Handling
                }
            });
        }
        catch (RequestException ex) {
            //ToDo: Look into new Error Handling
        }
    }
}
