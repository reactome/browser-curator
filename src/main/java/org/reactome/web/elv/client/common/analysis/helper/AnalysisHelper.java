package org.reactome.web.elv.client.common.analysis.helper;

import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import org.reactome.web.elv.client.common.analysis.factory.AnalysisModelException;
import org.reactome.web.elv.client.common.analysis.factory.AnalysisModelFactory;
import org.reactome.web.elv.client.common.analysis.model.ResourceSummary;
import org.reactome.web.elv.client.common.utils.Console;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public abstract class AnalysisHelper {

    public static final String URL_PREFIX = "/AnalysisService";

    private static Map<String, List<ResourceSummary>> resourceSummaryMap = new HashMap<String, List<ResourceSummary>>();

    public interface ResourceChosenHandler {
        void onResourceChosen(String resource);
    }

    public static void chooseResource(final String token, final ResourceChosenHandler handler){
        if(resourceSummaryMap.containsKey(token)){
            chooseResource(resourceSummaryMap.get(token), handler);
            return;
        }

        String url = URL_PREFIX + "/token/" + token + "/resources";
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    try {
                        List<ResourceSummary> list = new LinkedList<ResourceSummary>();
                        JSONArray aux = JSONParser.parseStrict(response.getText()).isArray();
                        for (int i = 0; i < aux.size(); i++) {
                            JSONObject resource = aux.get(i).isObject();
                            list.add(AnalysisModelFactory.getModelObject(ResourceSummary.class, resource.toString()));
                        }
                        resourceSummaryMap.put(token, list);
                        chooseResource(list, handler);
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

    private static void chooseResource(List<ResourceSummary> resources, ResourceChosenHandler handler){
        if(handler!=null){
            ResourceSummary resource = resources.size()==2 ? resources.get(1) : resources.get(0);
            handler.onResourceChosen(resource.getResource());
        }
    }
}
