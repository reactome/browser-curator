package org.reactome.web.elv.client.manager.tour.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.elv.client.manager.tour.handlers.TourStepFinishedEventHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class TourStepFinishedEvent extends GwtEvent<TourStepFinishedEventHandler> {
    public static Type<TourStepFinishedEventHandler> TYPE = new GwtEvent.Type<TourStepFinishedEventHandler>();

    public TourStepFinishedEvent() {
    }

    @Override
    public Type<TourStepFinishedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(TourStepFinishedEventHandler handler) {
        handler.onTourStepFinished(this);
    }
}
