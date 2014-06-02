package org.reactome.web.elv.client.manager.tour.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.elv.client.manager.tour.events.TourStepFinishedEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface TourStepFinishedEventHandler extends EventHandler {

    public void onTourStepFinished(TourStepFinishedEvent event);

}
