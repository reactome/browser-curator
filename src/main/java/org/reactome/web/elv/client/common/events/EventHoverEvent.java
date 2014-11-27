package org.reactome.web.elv.client.common.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.elv.client.common.data.model.Event;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.common.handlers.EventHoverHandler;
import org.reactome.web.elv.client.common.model.Path;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class EventHoverEvent extends GwtEvent<EventHoverHandler> {
    public static Type<EventHoverHandler> TYPE = new Type<EventHoverHandler>();

    private Path path;
    private Pathway pathway;
    private Event event;

    public EventHoverEvent(Pathway pathway) {
        this.pathway = pathway;
    }

    public EventHoverEvent(Path path, Pathway pathway, Event event) {
        this.path = path;
        this.pathway = pathway;
        this.event = event;
    }

    @Override
    public Type<EventHoverHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(EventHoverHandler handler) {
        handler.onEventHovered(this);
    }


    public Path getPath() {
        return path;
    }

    public Pathway getPathway() {
        return pathway;
    }

    public Event getEvent() {
        return event;
    }
}
