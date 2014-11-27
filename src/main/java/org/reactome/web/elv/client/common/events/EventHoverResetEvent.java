package org.reactome.web.elv.client.common.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.elv.client.common.handlers.EventHoverResetHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class EventHoverResetEvent extends GwtEvent<EventHoverResetHandler> {
    public static Type<EventHoverResetHandler> TYPE = new Type<EventHoverResetHandler>();

    @Override
    public Type<EventHoverResetHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(EventHoverResetHandler handler) {
        handler.onEventHoveredReset();
    }

}
