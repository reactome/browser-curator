package org.reactome.web.elv.client.manager.state;

import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import org.reactome.web.elv.client.common.data.factory.ModelFactory;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Event;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.common.data.model.Species;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class AdvancedStateHelper {
    AdvancedState state;

    public AdvancedStateHelper(AdvancedState state) {
        this.state = state;
    }

    static Map<String, DatabaseObject> map = new HashMap<String, DatabaseObject>();

    private void cache(DatabaseObject databaseObject) {
        map.put(databaseObject.getDbId().toString(), databaseObject);
        map.put(databaseObject.getIdentifier(), databaseObject);
    }

    public void setDatabaseObject(AdvancedStateKey key, DatabaseObject... databaseObjects) {
        switch (key) {
            case SPECIES:
                this.state.setSpecies((Species) databaseObjects[0]);
                break;
            case DIAGRAM:
                this.state.setPathway((Pathway) databaseObjects[0]);
                break;
            case INSTANCE:
                this.state.setInstance(databaseObjects[0]);
                break;
            case PATH:
                List<Event> path = new LinkedList<Event>();
                for (DatabaseObject databaseObject : databaseObjects) {
                    path.add((Event) databaseObject);
                }
                this.state.setPath(path);
                break;
        }
    }

    public void setDatabaseObject(final AdvancedStateKey key, String identifier) {
        if (map.containsKey(identifier)) {
            this.setDatabaseObject(key, map.get(identifier));
        } else {
            String url = "/ReactomeRESTfulAPI/RESTfulWS/detailedView/DatabaseObject/" + identifier;
            RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
            requestBuilder.setHeader("Accept", "application/json");
            try {
                requestBuilder.sendRequest(null, new RequestCallback() {
                    @Override
                    public void onResponseReceived(Request request, Response response) {
                        String text = response.getText();
                        JSONObject json = JSONParser.parseStrict(text).isObject();
                        DatabaseObject databaseObject = ModelFactory.getDatabaseObject(json);
                        setDatabaseObject(key, databaseObject);
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
    }

    private List<Event> getPathFromCache(String... identifier){
        List<Event> path = new LinkedList<Event>();
        for (String id : identifier) {
            try {
                path.add((Pathway) map.get(id));
            }catch (ClassCastException ex){
                //Do not take it into account
            }
        }
        return path;
    }

    public void setPathEvents(final String... identifiers) {
        String url = "/ReactomeRESTfulAPI/RESTfulWS/queryByIds/DatabaseObject";
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, url);
        requestBuilder.setHeader("Content-Type", "text/plain");
        requestBuilder.setHeader("Accept", "application/json");

        StringBuilder post = new StringBuilder("ID=");
        for (String identifier : identifiers) {
            if (!map.containsKey(identifier)) {
                post.append(identifier).append(",");
            }
        }
        if (post.length() == 3) { //WE HAVE ALL THE OBJECTS CACHED
            this.state.setPath(getPathFromCache(identifiers));
        } else { //WE NEED TO QUERY FOR SOME OF THEM
            try {
                post.deleteCharAt(post.length() - 1);
                requestBuilder.sendRequest(post.toString(), new RequestCallback() {
                    @Override
                    public void onResponseReceived(Request request, Response response) {
                        JSONArray list = JSONParser.parseStrict(response.getText()).isArray();
                        for (int i = 0; i < list.size(); ++i) {
                            JSONObject object = list.get(i).isObject();
                            DatabaseObject aux = ModelFactory.getDatabaseObject(object);
                            cache(aux);
                        }
                        state.setPath(getPathFromCache(identifiers));
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
    }
}
