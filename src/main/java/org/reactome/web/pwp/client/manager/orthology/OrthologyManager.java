package org.reactome.web.pwp.client.manager.orthology;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import org.reactome.web.pwp.client.common.events.ErrorMessageEvent;
import org.reactome.web.pwp.client.common.events.SpeciesSelectedEvent;
import org.reactome.web.pwp.client.common.events.StateChangedEvent;
import org.reactome.web.pwp.client.common.handlers.SpeciesSelectedHandler;
import org.reactome.web.pwp.client.common.handlers.StateChangedHandler;
import org.reactome.web.pwp.client.common.module.BrowserModule;
import org.reactome.web.pwp.client.manager.state.State;
import org.reactome.web.pwp.model.classes.DatabaseObject;
import org.reactome.web.pwp.model.classes.Event;
import org.reactome.web.pwp.model.classes.Pathway;
import org.reactome.web.pwp.model.classes.Species;
import org.reactome.web.pwp.model.factory.DatabaseObjectFactory;
import org.reactome.web.pwp.model.util.Path;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Takes advantage of the orthologous method implemented on the server side
 *
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class OrthologyManager implements BrowserModule.Manager, StateChangedHandler, SpeciesSelectedHandler {

    private EventBus eventBus;

    private State currentState;
    private State desiredState;

    public OrthologyManager(EventBus eventBus) {
        this.eventBus = eventBus;

        this.eventBus.addHandler(SpeciesSelectedEvent.TYPE, this);
        this.eventBus.addHandler(StateChangedEvent.TYPE, this);
    }

    @Override
    public void onSpeciesSelected(SpeciesSelectedEvent event) {
        this.desiredState = new State(this.currentState);
        this.desiredState.setPathway(null);
        this.desiredState.setSelected(null);
        this.desiredState.setPath(null);
        this.desiredState.setSpecies(event.getSpecies()); //Keep this after the rest have been set to null;

        List<DatabaseObject> list = new LinkedList<>();
        if(this.currentState.getPathway()!=null) {
            list.add(this.currentState.getPathway());
            if(this.currentState.getSelected()!=null){
                list.add(this.currentState.getSelected());
            }
            if(this.currentState.getPath()!=null && !this.currentState.getPath().isEmpty()){
                list.addAll(this.currentState.getPath().asList());
            }
        }
        if(!list.isEmpty()){
            retrieveOrthologous(list, event.getSpecies());
        }else{
            this.eventBus.fireEventFromSource(new StateChangedEvent(this.desiredState), this);
        }
    }

    @Override
    public void onStateChanged(StateChangedEvent event) {
        this.currentState = event.getState();
    }

    private void onOrthologousRetrieved(Map<Long, DatabaseObject> map){
        Pathway pathway = this.currentState.getPathway();
        if(pathway!=null){ //Not really needed because we have check it above
            if(map.containsKey(pathway.getDbId())){
                this.desiredState.setPathway((Pathway) map.get(pathway.getDbId()));

                DatabaseObject selected = this.currentState.getSelected();
                if(selected!=null) {
                    if (map.containsKey(selected.getDbId())) {
                        this.desiredState.setSelected(map.get(selected.getDbId()));
                    }
                }

                if(currentState.getPath()!=null) {
                    Path orthPath = new Path();
                    for (Event event : currentState.getPath()) {
                        if (map.containsKey(event.getDbId())) {
                            orthPath.add((Event) map.get(event.getDbId()));
                        } else {
                            break;
                        }
                    }
                    if (!orthPath.isEmpty()) {
                        this.desiredState.setPath(orthPath);
                    }
                }
            }
        }
        this.eventBus.fireEventFromSource(new StateChangedEvent(this.desiredState), this);
    }

    private void retrieveOrthologous(List<DatabaseObject> list, Species species) {
        String url = "/ReactomeRESTfulAPI/RESTfulWS/orthologous/Species/" + species.getDbId();
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
                            onOrthologousRetrieved(map);
                            break;
                        default:
                            onOrthologousRetrieved(new HashMap<Long, DatabaseObject>());
                            eventBus.fireEventFromSource(new ErrorMessageEvent("Server error while retrieving orthology list: " + response.getStatusText()), this);
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    onOrthologousRetrieved(new HashMap<Long, DatabaseObject>());
                    eventBus.fireEventFromSource(new ErrorMessageEvent("Orthology list not available", exception), this);
                }
            });
        } catch (RequestException e) {
            onOrthologousRetrieved(new HashMap<Long, DatabaseObject>());
            eventBus.fireEventFromSource(new ErrorMessageEvent("Orthology list can not be retrieved", e), this);
        }
    }
}
