package org.reactome.web.pwp.client.common.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.pwp.client.common.events.DiagramOpenedEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface DiagramOpenedHandler extends EventHandler {
    void onDiagramOpened(DiagramOpenedEvent event);
}
