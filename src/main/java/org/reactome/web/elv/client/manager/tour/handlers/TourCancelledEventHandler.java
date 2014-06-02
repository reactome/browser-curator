package org.reactome.web.elv.client.manager.tour.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.elv.client.manager.tour.events.TourCancelledEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface TourCancelledEventHandler extends EventHandler {

    public void onTourCancelled(TourCancelledEvent event);

}
