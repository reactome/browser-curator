package org.reactome.web.pwp.client.manager.title;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import org.reactome.web.pwp.client.common.events.BrowserReadyEvent;
import org.reactome.web.pwp.client.common.events.StateChangedEvent;
import org.reactome.web.pwp.client.common.handlers.BrowserReadyHandler;
import org.reactome.web.pwp.client.common.handlers.StateChangedHandler;
import org.reactome.web.pwp.client.common.model.classes.DatabaseObject;
import org.reactome.web.pwp.client.common.model.classes.Event;
import org.reactome.web.pwp.client.common.model.classes.Species;
import org.reactome.web.pwp.client.common.module.BrowserModule;
import org.reactome.web.pwp.client.details.tabs.DetailsTabType;
import org.reactome.web.pwp.client.manager.state.State;
import org.reactome.web.pwp.client.manager.state.token.Token;
import org.reactome.web.pwp.client.manager.title.event.TitleChangedEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class TitleManager implements BrowserModule.Manager, BrowserReadyHandler, StateChangedHandler {
    private EventBus eventBus;

    private String initialTitle = "";

    public TitleManager(EventBus eventBus) {
        this.eventBus = eventBus;
        this.eventBus.addHandler(BrowserReadyEvent.TYPE, this);
        this.eventBus.addHandler(StateChangedEvent.TYPE, this);
    }

    @Override
    public void onBrowserReady(BrowserReadyEvent event) {
        this.initialTitle = Window.getTitle();
    }

    @Override
    public void onStateChanged(StateChangedEvent stateChangedEvent) {
        State state = stateChangedEvent.getState();
        StringBuilder sb = new StringBuilder("PB | ");
        Event eventWithDiagram = state.getEventWithDiagram();
        DatabaseObject databaseObject = state.getSelected();
        if (eventWithDiagram == null && databaseObject == null) {
            sb = new StringBuilder(this.initialTitle);
        }else if (eventWithDiagram != null && databaseObject == null) {
            sb.append(eventWithDiagram.getDisplayName());
        } else {
            sb.append(databaseObject.getDisplayName());
        }
        if(stateChangedEvent.getState().getFlag()!=null){
            sb.append(" (").append(stateChangedEvent.getState().getFlag()).append(")");
        }
        Species species = state.getSpecies();
        if (species != null && !species.getDbId().equals(Token.DEFAULT_SPECIES_ID)) {
            sb.append(" [").append(species.getDisplayName()).append("]");
        }
        DetailsTabType detailsTabType = state.getDetailsTab();
        if (!detailsTabType.equals(DetailsTabType.getDefault())) {
            sb.append(" - ").append(detailsTabType.getTitle());
        }
        Window.setTitle(sb.toString());
        this.eventBus.fireEventFromSource(new TitleChangedEvent(sb.toString()), this);
    }
}