package org.reactome.web.pwp.client.common.model.client;

import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import org.reactome.web.pwp.client.common.model.classes.*;
import org.reactome.web.pwp.client.common.model.client.handlers.*;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectFactory;
import org.reactome.web.pwp.client.common.model.handlers.DatabaseObjectsLoadedHandler;
import org.reactome.web.pwp.client.common.model.handlers.MapLoadedHandler;
import org.reactome.web.pwp.client.common.model.util.Ancestors;

import java.util.*;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class RESTFulClient {

    public static String SERVER = ""; //Here "http://reactome.org" can be set to use CORS
    public static String CONTENT_SERVICE_PATH = "/ReactomeRESTfulAPICurator/RESTfulWS/";

    public static void getAncestors(Event event, final AncestorsCreatedHandler handler) {
        getAncestors(event.getDbId(), handler);
    }

    public static void getAncestors(Long dbId, final AncestorsCreatedHandler handler) {
        String url = SERVER + CONTENT_SERVICE_PATH + "queryEventAncestors/" + dbId;
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()) {
                        case Response.SC_OK:
                            Ancestors ancestors;
                            try {
                                JSONArray list = JSONParser.parseStrict(response.getText()).isArray();
                                ancestors = new Ancestors(list);
                                //For the time being the events in the ancestors ARE NOT cached
                            } catch (Exception ex) {
                                String msg = "The received data to expand the path down to 'dbId=" + dbId + "' is empty or faulty and could not be parsed. ERROR: " + ex.getMessage();
                                handler.onAncestorsError(new Exception(msg));
                                return;
                            }
                            handler.onAncestorsLoaded(ancestors);
                            break;
                        default:
                            String msg = "Server error while retrieving ancestors event " + dbId + ": " + response.getStatusText();
                            handler.onAncestorsError(new Exception(msg));
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    String msg = "The ancestors-request for '" + dbId + "' received an error instead of a valid response. ERROR: " + exception.getMessage();
                    handler.onAncestorsError(new Exception(msg));
                }
            });
        } catch (RequestException ex) {
            String msg = "The ancestors for '" + dbId + "' could not be received. ERROR: " + ex.getMessage();
            handler.onAncestorsError(new Exception(msg));
        }
    }

    public static void getFrontPageItems(Species species, DatabaseObjectsLoadedHandler<Event> handler) {
        String speciesName = species.getDisplayName().replaceAll(" ", "+");
        String url = SERVER + CONTENT_SERVICE_PATH + "frontPageItems/" + speciesName;
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()) {
                        case Response.SC_OK:
                            List<Event> frontPageItems = new ArrayList<>();
                            try {
                                JSONArray list = JSONParser.parseStrict(response.getText()).isArray();
                                for (int i = 0; i < list.size(); ++i) {
                                    Event event = (Event) DatabaseObjectFactory.create(list.get(i).isObject());
                                    frontPageItems.add(event);
                                }
                            } catch (Exception ex) {
                                String msg = "The received data for the front page items of '" + species.getDisplayName() + "' is empty or faulty and could not be parsed. ERROR: " + ex.getMessage();
                                handler.onDatabaseObjectError(new Exception(msg));
                                return;
                            }
                            handler.onDatabaseObjectLoaded(frontPageItems);
                            break;
                        default:
                            String msg = "Server error while retrieving hierarchy top level pathways: " + response.getStatusText();
                            handler.onDatabaseObjectError(new Exception(msg));
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    String msg = "The front-page-items-request for '" + species.getDisplayName() + "' received an error instead of a valid response. ERROR: " + exception.getMessage();
                    handler.onDatabaseObjectError(new Exception(msg));
                }
            });
        } catch (RequestException ex) {
            String msg = "The front page items for '" + species.getDisplayName() + "' could not be received. ERROR: " + ex.getMessage();
            handler.onDatabaseObjectError(new Exception(msg));
        }
    }

    public static void getOrthologous(List<DatabaseObject> list, Species species, MapLoadedHandler<Long, DatabaseObject> handler) {
        String url = SERVER + CONTENT_SERVICE_PATH + "orthologous/Species/" + species.getDbId();
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, url);
        requestBuilder.setHeader("Accept", "application/json");

        StringBuilder sb = new StringBuilder("ID=");
        for (DatabaseObject object : list) {
            sb.append(object.getDbId()).append(",");
        }
        sb.delete(sb.length() - 1, sb.length());
        String post = sb.toString();

        try {
            requestBuilder.sendRequest(post, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()) {
                        case Response.SC_OK:
                            Map<Long, DatabaseObject> map = new HashMap<>();
                            JSONObject object = JSONParser.parseStrict(response.getText()).isObject();
                            for (String key : object.keySet()) {
                                DatabaseObject orth = DatabaseObjectFactory.create(object.get(key).isObject());
                                map.put(Long.valueOf(key), orth);
                            }
                            handler.onMapLoaded(map);
                            break;
                        default:
                            handler.onMapLoaded(new HashMap<>());
                            String msg = "Server error while retrieving orthology list: " + response.getStatusText();
                            handler.onMapError(new Exception(msg));
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    handler.onMapLoaded(new HashMap<>());
                    handler.onMapError(new Exception("Orthology list not available"));
                }
            });
        } catch (RequestException e) {
            handler.onMapLoaded(new HashMap<>());
            handler.onMapError(new Exception("Orthology list not available"));
        }
    }

    public static void getSpeciesList(final DatabaseObjectsLoadedHandler<Species> handler) {
        String url = SERVER + CONTENT_SERVICE_PATH + "speciesList/";
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");

        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()) {
                        case Response.SC_OK:
                            List<Species> speciesList = new LinkedList<>();
                            JSONArray list = JSONParser.parseStrict(response.getText()).isArray();
                            for (int i = 0; i < list.size(); ++i) {
                                JSONObject object = list.get(i).isObject();
                                Species species = (Species) DatabaseObjectFactory.create(object);
                                //Sometimes the RESTFul services retrieves several times the same speciesList
                                //Note a Set does not help because order is important here
                                if (!speciesList.contains(species)) {
                                    speciesList.add(species);
                                }
                            }
                            handler.onDatabaseObjectLoaded(speciesList);
                            break;
                        default:
                            handler.onDatabaseObjectError(new Exception("There was an error while retrieving species list: " + response.getStatusText()));
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    handler.onDatabaseObjectError(new Exception("Species list not available", exception));
                }
            });
        } catch (RequestException e) {
            handler.onDatabaseObjectError(new Exception("Species list can not be retrieved", e));
        }
    }

    public static void loadPublications(final Person person, final LiteratureReferencesLoadedHandler handler) {
        String url = SERVER + CONTENT_SERVICE_PATH + "queryReferences/" + person.getDbId();
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    List<Publication> publications;
                    try {
                        JSONArray list = JSONParser.parseStrict(response.getText()).isArray();
                        publications = new ArrayList<>();
                        for (int i = 0; i < list.size(); ++i) {
                            JSONObject object = list.get(i).isObject();
                            publications.add((Publication) DatabaseObjectFactory.create(object));
                        }
                    } catch (Exception ex) {
                        handler.onLiteratureReferencesError(ex);
                        return;
                    }
                    person.setPublications(publications);
                    handler.onLiteratureReferencesLoaded(person);
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    handler.onLiteratureReferencesError(exception);
                }
            });
        } catch (RequestException ex) {
            handler.onLiteratureReferencesError(ex);
        }
    }

    public static void loadReferenceSequences(final DatabaseObject databaseObject, DatabaseObjectsLoadedHandler<ReferenceSequence> handler) {
        String url = SERVER + CONTENT_SERVICE_PATH + "referenceEntity/" + databaseObject.getDbId();
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()) {
                        case Response.SC_OK:
                            try {
                                JSONArray jsonArray = JSONParser.parseStrict(response.getText()).isArray();
                                List<ReferenceSequence> referenceSequenceList = new LinkedList<>();
                                for (int i = 0; i < jsonArray.size(); i++) {
                                    JSONObject json = jsonArray.get(i).isObject();
                                    DatabaseObject databaseObject = DatabaseObjectFactory.create(json);
                                    if (databaseObject instanceof ReferenceSequence) {
                                        referenceSequenceList.add((ReferenceSequence) databaseObject);
                                    }
                                }
                                handler.onDatabaseObjectLoaded(referenceSequenceList);
                            } catch (Exception ex) {
                                String errorMsg = "The received data for the reference entities of '" + databaseObject.getDisplayName() + "' is empty or faulty and could not be parsed.";
                                handler.onDatabaseObjectError(new Exception(errorMsg));
                            }
                            break;
                        default:
                            String errorMsg = "There was an error processing the request. ERROR: " + response.getStatusText();
                            handler.onDatabaseObjectError(new Exception(errorMsg));
                    }

                }

                @Override
                public void onError(Request request, Throwable exception) {
                    String errorMsg = "The request for '" + databaseObject.getDisplayName() + "' in the Expression Tab received an error instead of a valid response.";
                    handler.onDatabaseObjectError(new Exception(errorMsg));
                }
            });
        } catch (RequestException ex) {
            String errorMsg = "The required data for the Expression of '" + databaseObject.getDisplayName() + "' could not be received";
            handler.onDatabaseObjectError(new Exception(errorMsg));
        }
    }

    public static void loadPathwaysWithDiagramForEntity(PhysicalEntity pe, final PathwaysForEntitiesLoadedHandler handler) {
        String url = SERVER + CONTENT_SERVICE_PATH + "pathwaysWithDiagramForEntity/" + pe.getDbId();
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()) {
                        case Response.SC_OK:
                            List<Pathway> pathways;
                            try {
                                JSONArray list = JSONParser.parseStrict(response.getText()).isArray();
                                pathways = new ArrayList<>();
                                for (int i = 0; i < list.size(); ++i) {
                                    JSONObject object = list.get(i).isObject();
                                    pathways.add((Pathway) DatabaseObjectFactory.create(object));
                                }

                            } catch (Exception e) {
                                handler.onPathwaysForEntitiesError(e);
                                return;
                            }
                            handler.onPathwaysForEntitiesLoaded(pathways);
                            break;
                        default:
                            handler.onPathwaysForEntitiesError(new Exception(response.getStatusText()));
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    handler.onPathwaysForEntitiesError(exception);
                }
            });
        } catch (RequestException e) {
            handler.onPathwaysForEntitiesError(e);
        }
    }

    private static String version;

    public static void getDBName(final DBNameRetrievedHandler handler) {
        String url = SERVER + CONTENT_SERVICE_PATH + "getDBName";
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()) {
                        case Response.SC_OK:
                            String name = response.getText().trim();
                            handler.onDBNameRetrieved(name);
                            break;
                        default:
                            String errorMsg = "Error retrieving the database name. ERROR " + response.getStatusText();
                            handler.onDBNameRetrievedError(new Exception(errorMsg));
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    String errorMsg = "The database name could not be retrieved.";
                    handler.onDBNameRetrievedError(new Exception(errorMsg));
                }
            });
        } catch (RequestException ex) {
            String errorMsg = "The database name could not be retrieved.";
            handler.onDBNameRetrievedError(new Exception(errorMsg));
        }
    }

    public static void getVersion(final VersionRetrievedHandler handler) {
        if (version != null && !version.isEmpty()) {
            handler.onVersionRetrieved(version);
        } else {
            String url = SERVER + CONTENT_SERVICE_PATH + "version";
            RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
            try {
                requestBuilder.sendRequest(null, new RequestCallback() {
                    @Override
                    public void onResponseReceived(Request request, Response response) {
                        switch (response.getStatusCode()) {
                            case Response.SC_OK:
                                version = response.getText().trim();
                                handler.onVersionRetrieved(response.getText());
                                break;
                            default:
                                handler.onVersionRetrievedError(new Exception(response.getStatusText()));
                        }
                    }

                    @Override
                    public void onError(Request request, Throwable exception) {
                        handler.onVersionRetrievedError(exception);
                    }
                });
            } catch (RequestException e) {
                handler.onVersionRetrievedError(e);
            }
        }
    }
}
