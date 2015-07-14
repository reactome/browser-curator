package org.reactome.web.pwp.client.common.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.pwp.client.common.events.BrowserReadyEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface BrowserReadyHandler extends EventHandler {
    void onBrowserReady(BrowserReadyEvent event);
}
