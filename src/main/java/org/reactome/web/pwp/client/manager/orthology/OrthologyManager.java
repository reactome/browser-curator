package org.reactome.web.pwp.client.manager.orthology;

import com.google.gwt.event.shared.EventBus;
import org.reactome.web.pwp.client.common.events.ErrorMessageEvent;
import org.reactome.web.pwp.client.common.events.SpeciesSelectedEvent;
import org.reactome.web.pwp.client.common.events.StateChangedEvent;
import org.reactome.web.pwp.client.common.handlers.SpeciesSelectedHandler;
import org.reactome.web.pwp.client.common.handlers.StateChangedHandler;
import org.reactome.web.pwp.client.common.model.classes.DatabaseObject;
import org.reactome.web.pwp.client.common.model.classes.Event;
import org.reactome.web.pwp.client.common.model.client.RESTFulClient;
import org.reactome.web.pwp.client.common.model.handlers.MapLoadedHandler;
import org.reactome.web.pwp.client.common.model.util.Path;
import org.reactome.web.pwp.client.common.module.BrowserModule;
import org.reactome.web.pwp.client.manager.state.State;

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
        this.desiredState.setEventWithDiagram(null);
        this.desiredState.setSelected(null);
        this.desiredState.setPath(null);
        this.desiredState.setSpecies(event.getSpecies()); //Keep this after the rest have been set to null;

        List<DatabaseObject> list = new LinkedList<>();
        if (this.currentState.getEventWithDiagram() != null) {
            list.add(this.currentState.getEventWithDiagram());
            if (this.currentState.getSelected() != null) {
                list.add(this.currentState.getSelected());
            }
            if (this.currentState.getPath() != null && !this.currentState.getPath().isEmpty()) {
                list.addAll(this.currentState.getPath().asList());
            }
        }
        if (!list.isEmpty()) {
            RESTFulClient.getOrthologous(list, event.getSpecies(), new MapLoadedHandler<Long, DatabaseObject>() {
                @Override
                public void onMapLoaded(Map<Long, DatabaseObject> map) {
                    onOrthologousRetrieved(map);

                }

                @Override
                public void onMapError(Throwable ex) {
                    eventBus.fireEventFromSource(new ErrorMessageEvent(ex.getMessage()), this);
                }
            });
        } else {
            this.eventBus.fireEventFromSource(new StateChangedEvent(this.desiredState), this);
        }
    }

    @Override
    public void onStateChanged(StateChangedEvent event) {
        this.currentState = event.getState();
    }

    private void onOrthologousRetrieved(Map<Long, DatabaseObject> map) {
        Event eventWithDiagram = this.currentState.getEventWithDiagram();
        if (eventWithDiagram != null) { //Not really needed because we have check it above
            if (map.containsKey(eventWithDiagram.getDbId())) {
                this.desiredState.setEventWithDiagram((Event) map.get(eventWithDiagram.getDbId()));

                DatabaseObject selected = this.currentState.getSelected();
                if (selected != null) {
                    if (map.containsKey(selected.getDbId())) {
                        this.desiredState.setSelected(map.get(selected.getDbId()));
                    }
                }

                if (currentState.getPath() != null) {
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
}
