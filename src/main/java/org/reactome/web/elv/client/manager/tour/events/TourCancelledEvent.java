package org.reactome.web.elv.client.manager.tour.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.elv.client.manager.tour.handlers.TourCancelledEventHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class TourCancelledEvent extends GwtEvent<TourCancelledEventHandler> {
    public static Type<TourCancelledEventHandler> TYPE = new GwtEvent.Type<TourCancelledEventHandler>();

    @Override
    public Type<TourCancelledEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(TourCancelledEventHandler handler) {
        handler.onTourCancelled(this);
    }
}
