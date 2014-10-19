package org.reactome.web.elv.client.manager.data;

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
import org.reactome.web.elv.client.common.data.model.Species;
import org.reactome.web.elv.client.common.events.ELVEventType;
import org.reactome.web.elv.client.common.model.Pair;
import org.reactome.web.elv.client.common.utils.Console;
import org.reactome.web.elv.client.manager.messages.MessageObject;
import org.reactome.web.elv.client.manager.messages.MessageType;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DataManager extends Controller {

    private Map<Long, DatabaseObject> databaseObjectMap = new HashMap<Long, DatabaseObject>();

    public DataManager(EventBus eventBus) {
        super(eventBus);
    }

    private void cacheDatabaseObjects(DatabaseObject databaseObject){
            this.databaseObjectMap.put(databaseObject.getDbId(), databaseObject);
    }

    private void cacheDatabaseObjects(List<? extends DatabaseObject> list){
        for (DatabaseObject databaseObject : list) {
            cacheDatabaseObjects(databaseObject);
        }
    }

    @Override
    public void onDatabaseObjectDetailedViewRequired(final Long dbId) {
        String url = "/ReactomeRESTfulAPI/RESTfulWS/detailedView/DatabaseObject/" + dbId;
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
                        cacheDatabaseObjects(databaseObject); //Todo: check whether this is better option
                        eventBus.fireELVEvent(ELVEventType.DATA_MANAGER_OBJECT_DETAILED_VIEW_RETRIEVED, databaseObject);
                    }catch (Exception ex){
                        //ModelFactoryException, NullPointerException, IllegalArgumentException, JSONException
                        MessageObject msgObj = new MessageObject("The received object for the required detailed view" +
                                "\n'DbId=" + dbId + "' is empty or faulty and could not be parsed.\n" +
                                "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                        eventBus.fireELVEvent(ELVEventType.INTERANL_MESSAGE, msgObj);
                        Console.error(getClass() + " ERROR: " + ex.getMessage());
                    }
                }
                @Override
                public void onError(Request request, Throwable exception) {
                    /*replaced: eventBus.fireELVEvent(ELVEventType.DATA_MANAGER_LOAD_ERROR, exception.getMessage());*/
                    MessageObject msgObj = new MessageObject("The detailed view request for 'DbId=" + dbId + "'\n" +
                            "received an error instead of a valid response.\n" +
                            "ERROR: " + exception.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                    eventBus.fireELVEvent(ELVEventType.INTERANL_MESSAGE, msgObj);
                    Console.error(getClass() + " ERROR: " + exception.getMessage());
                }
            });
        }catch (RequestException ex) {
            /*replaced: eventBus.fireELVEvent(ELVEventType.DATA_MANAGER_LOAD_ERROR, ex.getMessage());*/
            MessageObject msgObj = new MessageObject("The requested detailed view for 'DbId=" + dbId
                    + "' could not be received.\n" +
                    "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
            eventBus.fireELVEvent(ELVEventType.INTERANL_MESSAGE, msgObj);
            Console.error(getClass() + " ERROR: " + ex.getMessage());
        }
    }

    /**
     * Retrieves the corresponding object if has not been loaded before and fires the delegated event
     * @param dbId the database identifier of the required object
     * @param eventType the event type to fire in the event bus
     */
    @Override
    public void onDatabaseObjectRequired(final Long dbId, final ELVEventType eventType) {
        if(this.databaseObjectMap.containsKey(dbId)){
            eventBus.fireELVEvent(eventType, this.databaseObjectMap.get(dbId));
            return;
        }
        String url = "/ReactomeRESTfulAPI/RESTfulWS/queryById/DatabaseObject/" + dbId;
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    try{
                        JSONObject json = JSONParser.parseStrict(response.getText()).isObject();
                        DatabaseObject databaseObject = ModelFactory.getDatabaseObject(json);
                        cacheDatabaseObjects(databaseObject);
                        eventBus.fireELVEvent(eventType, databaseObject);
                    }catch (Exception ex){
                        //ModelFactoryException, NullPointerException, IllegalArgumentException, JSONException
                        MessageObject msgObj = new MessageObject("The received object containing data for DatabaseObject " +
                                dbId + " is empty or faulty and could not be parsed.\n" +
                                "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                        eventBus.fireELVEvent(ELVEventType.INTERANL_MESSAGE, msgObj);
                        Console.error(getClass() + " ERROR: " + ex.getMessage());
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    /*replaced: eventBus.fireELVEvent(ELVEventType.DATA_MANAGER_LOAD_ERROR, exception.getMessage());*/
                    if(!GWT.isScript()){
                        Console.error(getClass() + " ERROR: " + exception.getMessage());
                    }

                    MessageObject msgObj = new MessageObject("The request for database object " + dbId +
                            " received an error instead of a valid response.\n" +
                            "ERROR: " + exception.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                    eventBus.fireELVEvent(ELVEventType.INTERANL_MESSAGE, msgObj);
                }
            });
        } catch (RequestException ex) {
            /*replaced: eventBus.fireELVEvent(ELVEventType.DATA_MANAGER_LOAD_ERROR, ex.getMessage());*/
            MessageObject msgObj = new MessageObject("The requested database object " + dbId + " could not be received.\n" +
                    "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
            eventBus.fireELVEvent(ELVEventType.INTERANL_MESSAGE, msgObj);
            Console.error(getClass() + " ERROR: " + ex.getMessage());
        }
    }

    /**
     * Retrieves the corresponding objects if have not been loaded before and fires the retrieved event
     * @param pathwayId the database identifier for the diagram to be loaded
     * @param databaseObjectId the database identifier for the object to be selected
     */
    @Override
    public void onStateManagerDatabaseObjectsRequired(final Long pathwayId, final Long databaseObjectId) {
        /*TODO: Look into this
        Only called by Controller but event STATE_MANAGER_DATABASE_OBJECTS_REQUIRED never used else where*/
        if(this.databaseObjectMap.containsKey(pathwayId) && this.databaseObjectMap.containsKey(databaseObjectId)){
            Pathway pathway = (Pathway) this.databaseObjectMap.get(pathwayId);
            DatabaseObject databaseObject = this.databaseObjectMap.get(databaseObjectId);
            Pair<Pathway, DatabaseObject> tuple = new Pair<Pathway, DatabaseObject>(pathway, databaseObject);
            this.eventBus.fireELVEvent(ELVEventType.DATA_MANAGER_OBJECT_TUPLE_RETRIEVED, tuple);
            return;
        }

        String url = "/ReactomeRESTfulAPI/RESTfulWS/queryByIds/DatabaseObject";
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, url);
        requestBuilder.setHeader("Content-Type", "text/plain");
        requestBuilder.setHeader("Accept", "application/json");
        try {
            String post = "ID=" + pathwayId;
            if(databaseObjectId!=null){
                post += "," + databaseObjectId;
            }
            final String finalPost = post;
            requestBuilder.sendRequest(post, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    try{
                        JSONArray list = JSONParser.parseStrict(response.getText()).isArray();

                        Pathway pathway = null;
                        DatabaseObject databaseObject = null;

                        for(int i=0; i<list.size(); ++i){
                            JSONObject object = list.get(i).isObject();
                            DatabaseObject aux = ModelFactory.getDatabaseObject(object);
                            if(aux.getDbId().equals(pathwayId)){
                                pathway = (Pathway) aux;
                            }else if(databaseObjectId!=null && aux.getDbId().equals(databaseObjectId)){
                                databaseObject = aux;
                            }
                        }

                        cacheDatabaseObjects(pathway);
                        if (databaseObject != null) {
                            cacheDatabaseObjects(databaseObject);
                        }

                        Pair<Pathway, DatabaseObject> tuple = new Pair<Pathway, DatabaseObject>(pathway, databaseObject);
                        eventBus.fireELVEvent(ELVEventType.DATA_MANAGER_OBJECT_TUPLE_RETRIEVED, tuple);
                    }catch (Exception ex){
                        //ModelFactoryException, NullPointerException, IllegalArgumentException, JSONException
                        MessageObject msgObj = new MessageObject("The received object containing data for database objects" +
                                finalPost + " is empty or faulty and could not be parsed.\n" +
                                "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                        eventBus.fireELVEvent(ELVEventType.INTERANL_MESSAGE, msgObj);
                        Console.error(getClass() + " ERROR: " + ex.getMessage());
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    /*replaced: eventBus.fireELVEvent(ELVEventType.DATA_MANAGER_LOAD_ERROR, exception.getMessage());*/
                    if(!GWT.isProdMode() && GWT.isClient())
                        Console.error(getClass() + " ERROR: " + exception.getMessage());

                    MessageObject msgObj = new MessageObject("The request for database objects" +
                            " received an error instead of a valid response.\n" +
                            "ERROR: " + exception.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                    eventBus.fireELVEvent(ELVEventType.INTERANL_MESSAGE, msgObj);
                }
            });
        } catch (RequestException ex) {
            /*replaced: eventBus.fireELVEvent(ELVEventType.DATA_MANAGER_LOAD_ERROR, ex.getMessage());*/
            MessageObject msgObj = new MessageObject("The requested database objects could not be received.\n" +
                    "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
            eventBus.fireELVEvent(ELVEventType.INTERANL_MESSAGE, msgObj);
            Console.error(getClass() + " ERROR: " + ex.getMessage());
        }
    }

    @Override
    public void onTopBarSpeciesListRequired() {
        String url = "/ReactomeRESTfulAPI/RESTfulWS/speciesList/";
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        RequestCallback rc = new RequestCallback() {
            @Override
            public void onResponseReceived(Request request, Response response) {
                try{
                    JSONArray list = JSONParser.parseStrict(response.getText()).isArray();
                    List<Species> speciesList = new LinkedList<Species>();
                    for(int i=0; i<list.size(); ++i){
                        JSONObject object = list.get(i).isObject();
                        Species species = new Species(object);
                        //Sometimes the RESTFul services retrieves several times the same species
                        if(!speciesList.contains(species)){
                            speciesList.add(species);
                        }
                    }
                    cacheDatabaseObjects(speciesList);
                    eventBus.fireELVEvent(ELVEventType.DATA_MANAGER_SPECIES_LIST_RETRIEVED, speciesList);
                }catch (Exception ex){
                    //ModelFactoryException, NullPointerException, IllegalArgumentException, JSONException
                    MessageObject msgObj = new MessageObject("The received object containing data for speciesList" +
                            " is empty or faulty and could not be parsed.\n" +
                            "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                    eventBus.fireELVEvent(ELVEventType.INTERANL_MESSAGE, msgObj);
                    if(!GWT.isProdMode() && GWT.isClient())
                        Console.error(getClass() + " ERROR: " + ex.getMessage());
                }
            }

            @Override
            public void onError(Request request, Throwable exception) {
                /*replaced: eventBus.fireELVEvent(ELVEventType.DATA_MANAGER_LOAD_ERROR, exception.getMessage());*/
                if(!GWT.isProdMode() && GWT.isClient())
                    Console.error(getClass() + " ERROR: " + exception.getMessage());

                MessageObject msgObj = new MessageObject("The request for speciesList received an error instead of a valid response.\n" +
                        "ERROR: " + exception.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
                eventBus.fireELVEvent(ELVEventType.INTERANL_MESSAGE, msgObj);
            }
        };
        try {
            requestBuilder.sendRequest(null, rc);
        } catch (RequestException ex) {
            /*replaced: eventBus.fireELVEvent(ELVEventType.DATA_MANAGER_LOAD_ERROR, ex.getMessage());*/
            MessageObject msgObj = new MessageObject("The requested speciesList could not be received.\n" +
                    "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
            eventBus.fireELVEvent(ELVEventType.INTERANL_MESSAGE, msgObj);
            Console.error(getClass() + " ERROR: " + ex.getMessage());
        }
    }
}
