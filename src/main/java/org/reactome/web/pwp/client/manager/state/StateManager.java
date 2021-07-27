package org.reactome.web.pwp.client.manager.state;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.History;
import org.reactome.web.pwp.client.common.Selection;
import org.reactome.web.pwp.client.common.events.*;
import org.reactome.web.pwp.client.common.handlers.DatabaseObjectSelectedHandler;
import org.reactome.web.pwp.client.common.handlers.DetailsTabChangedHandler;
import org.reactome.web.pwp.client.common.handlers.StateChangedHandler;
import org.reactome.web.pwp.client.common.handlers.ToolSelectedHandler;
import org.reactome.web.pwp.client.common.model.classes.*;
import org.reactome.web.pwp.client.common.model.util.Path;
import org.reactome.web.pwp.client.common.module.BrowserModule;
import org.reactome.web.pwp.client.manager.state.token.Token;
import org.reactome.web.pwp.client.manager.state.token.TokenMalformedException;
import org.reactome.web.pwp.client.manager.title.TitleManager;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class StateManager implements BrowserModule.Manager, ValueChangeHandler<String>,
        State.StateLoadedHandler, StateChangedHandler, DatabaseObjectSelectedHandler, DetailsTabChangedHandler,
        ToolSelectedHandler {

    private EventBus eventBus;

    private State currentState;

    public StateManager(EventBus eventBus) {
        this.eventBus = eventBus;
        new TitleManager(eventBus);
        History.addValueChangeHandler(this);

        //The orthology manager can also fire state changed events
        this.eventBus.addHandler(StateChangedEvent.TYPE, this);
        this.eventBus.addHandler(DatabaseObjectSelectedEvent.TYPE, this);
        this.eventBus.addHandler(DetailsTabChangedEvent.TYPE, this);
        this.eventBus.addHandler(ToolSelectedEvent.TYPE, this);
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        String value = event.getValue();
        try {
            Token token = new Token(value);
            if (token.isWellFormed()) {
                new State(token, this);
            } else {
                this.tokenError(value);
            }
        } catch (TokenMalformedException e) {
            this.tokenError(value);
        }
    }

    @Override
    public void onDatabaseObjectSelected(DatabaseObjectSelectedEvent event) {
        Selection newSelection = event.getSelection();
        Selection currentSelection = new Selection(currentState);
        if (!currentSelection.equals(newSelection)) {
            Event diagram = newSelection.getDiagram();
            DatabaseObject selected = newSelection.getDatabaseObject();
            Path path = newSelection.getPath();

            //The following code assumes that PE or Events of different species
            //are selected when showing them, so it is highly recommended to do
            //some further checking and detect when an odd case happens
            if (diagram == null) {
                if (selected == null) {
                    currentState.setEventWithDiagram(null);
                } else {
                    Species species = null;
                    if(selected instanceof PhysicalEntity){
                        PhysicalEntity pe = (PhysicalEntity) selected;
                        if(pe.getSpecies()!=null && !pe.getSpecies().isEmpty()) {
                            species = pe.getSpecies().get(pe.getSpecies().size()-1);
                        }
                    }else if(selected instanceof Event){
                        Event e = (Event) selected;
                        if(e.getSpecies()!=null && !e.getSpecies().isEmpty()) {
                            species = e.getSpecies().get(e.getSpecies().size()-1);
                        }
                    }
                    if (species!=null && !species.equals(currentState.getSpecies())) {
                        eventBus.fireEventFromSource(new SpeciesSelectedEvent(species), this);
                        return;
                    }
                    currentState.setSelected(selected);
                }
            } else {
                currentState.setEventWithDiagram(diagram);
                currentState.setPath(path);
            }

            currentState.setSelected(newSelection.getDatabaseObject());
            currentState.setPath(newSelection.getPath());

            currentState.doConsistencyCheck(state -> {
                currentState = state;
                eventBus.fireEventFromSource(new StateChangedEvent(currentState), StateManager.this);
                History.newItem(currentState.toString(), false);
            });
        }
    }

    @Override
    public void onDetailsTabChanged(DetailsTabChangedEvent event) {
        State desiredState = new State(this.currentState);
        desiredState.setDetailsTab(event.getDetailsTab());
        this.eventBus.fireEventFromSource(new StateChangedEvent(desiredState), this);
    }

    @Override
    public void onStateChanged(StateChangedEvent event) {
        State state = event.getState();
        if (!this.currentState.equals(state)) {
            this.currentState = state;
            History.newItem(state.toString(), false);
        }
    }

    @Override
    public void onStateLoaded(final State state) {
        if (state == null) {
            this.eventBus.fireEventFromSource(new ErrorMessageEvent("The data in the URL can not be fit into a state"), this);
        } else if (!state.equals(this.currentState)) {
            currentState = state;
            eventBus.fireEventFromSource(new StateChangedEvent(state), StateManager.this);
            History.newItem(state.toString(), false);
        }
    }

    @Override
    public void onToolSelected(ToolSelectedEvent event) {
        State desiredState = new State(this.currentState);
        desiredState.setTool(event.getTool());
        this.eventBus.fireEventFromSource(new StateChangedEvent(desiredState), this);
    }

    private void tokenError(String token) {
        eventBus.fireEventFromSource(new TokenErrorEvent(token), this);
        try {
            new State(new Token(""), this); //Forcing to load the initial state at least
        } catch (TokenMalformedException e) {/*Nothing here*/}
    }
}
