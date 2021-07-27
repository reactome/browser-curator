package org.reactome.web.pwp.client.common.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.pwp.client.common.events.DiagramOpenRequestEvent;
/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface DiagramOpenRequestHandler extends EventHandler {

    void onDiagramOpenRequest(DiagramOpenRequestEvent event);

}
