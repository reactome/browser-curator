package org.reactome.web.elv.client.common.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.elv.client.common.events.EventHoverEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface EventHoverHandler extends EventHandler {

    void onEventHovered(EventHoverEvent e);

}
