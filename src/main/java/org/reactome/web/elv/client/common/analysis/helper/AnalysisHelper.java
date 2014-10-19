package org.reactome.web.elv.client.common.analysis.helper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import org.reactome.web.elv.client.common.analysis.factory.AnalysisModelException;
import org.reactome.web.elv.client.common.analysis.factory.AnalysisModelFactory;
import org.reactome.web.elv.client.common.analysis.model.AnalysisResult;
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

    public interface TokenAvailabilityHandler {
        void onTokenAvailabilityChecked(boolean available, String message);
    }

    public static void checkTokenAvailability(final String token, final TokenAvailabilityHandler handler){
        String url = URL_PREFIX + "/token/" + token + "?pageSize=0&page=1";
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()){
                        case Response.SC_OK:
                            try {
                                AnalysisResult result = AnalysisModelFactory.getModelObject(AnalysisResult.class, response.getText());
                                resourceSummaryMap.put(token, result.getResourceSummary());
                                handler.onTokenAvailabilityChecked(true, null);
                            } catch (AnalysisModelException e) {
                                //ToDo: Look into new Error Handling
                                if(!GWT.isProdMode() && GWT.isClient()) Console.error(e.getMessage());
                            }
                            break;
                        case Response.SC_GONE:
                            handler.onTokenAvailabilityChecked(false, "Your result may have been deleted due to a new content release.\n" +
                                                                      "Please submit your data again to obtain results from the latest version of our database");
                            break;
                        default:
                            handler.onTokenAvailabilityChecked(false, "There is no result associated with the provided token (in the url) from a previous analysis");
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    //ToDo: Look into new Error Handling
                    if(!GWT.isProdMode() && GWT.isClient()) Console.error(exception.getMessage());
                }
            });
        }catch (RequestException ex) {
            //ToDo: Look into new Error Handling
            if(!GWT.isProdMode() && GWT.isClient()) Console.error(ex.getMessage());
        }
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
                    switch (response.getStatusCode()){
                        case Response.SC_OK:
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
                                //ToDo: Look into new Error Handling
                                if(!GWT.isProdMode() && GWT.isClient()) Console.error(e.getMessage());
                            }
                            break;
                        case Response.SC_GONE:
                            if(!GWT.isProdMode() && GWT.isClient()){
                                Console.error("Your result may have been deleted due to a new content release. " +
                                            "Please submit your data again to obtain results for the latest version of our database");
                            }
                            break;
                        default:
                            if(!GWT.isProdMode() && GWT.isClient()) Console.error(response.getStatusText());
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    if(!GWT.isProdMode() && GWT.isClient()) Console.error(exception.getMessage());
                    //ToDo: Look into new Error Handling
                }
            });
        }catch (RequestException ex) {
            if(!GWT.isProdMode() && GWT.isClient()) Console.error(ex.getMessage());
            //ToDo: Look into new Error Handling
        }
    }

    private static void chooseResource(List<ResourceSummary> resources, ResourceChosenHandler handler){
        if(handler!=null){
            ResourceSummary resource = resources.size()==2 ? resources.get(1) : resources.get(0);
            handler.onResourceChosen(resource.getResource());
        }
    }
}
