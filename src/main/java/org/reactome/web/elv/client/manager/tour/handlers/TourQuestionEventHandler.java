package org.reactome.web.elv.client.manager.tour.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.elv.client.manager.tour.events.TourQuestionEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface TourQuestionEventHandler extends EventHandler {

    public void onTourDialogOptionSelected(TourQuestionEvent event);

}
