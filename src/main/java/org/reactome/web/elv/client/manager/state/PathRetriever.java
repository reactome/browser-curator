package org.reactome.web.elv.client.manager.state;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import org.reactome.web.elv.client.common.data.model.Event;
import org.reactome.web.elv.client.common.model.Ancestors;
import org.reactome.web.elv.client.common.model.Path;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@Deprecated
public class PathRetriever {
    public interface PathHandler extends EventHandler {
        void onPathRetrieved(Path path);
    }

    private PathHandler handler;

    public PathRetriever(PathHandler handler) {
        this.handler = handler;
    }

    /**
     * Checks the given ancestors list to get the first path without orphan pathways
     * @param list ancestors containing multiple paths through a given pathway
     * @return the first path without orphan pathways
     */
    private Path getPathWithoutOrphanPathways(List<Path> list){
        for (Path path : list) {
            if(path.rootHasDiagram()){
                return path;
            }
        }
        return new Path();
    }

    public void retrieveAncestors(final Event event){
        String url = "/ReactomeRESTfulAPI/RESTfulWS/queryEventAncestors/" + event.getDbId();
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    JSONArray list = JSONParser.parseStrict(response.getText()).isArray();
                    Ancestors ancestors = new Ancestors(list);
                    Path path = getPathWithoutOrphanPathways(ancestors.getPathsContaining(event));
                    handler.onPathRetrieved(path);
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    //ToDo
                }
            });
        }
        catch (RequestException ex) {
            //ToDo
        }
    }
}
