package org.reactome.web.elv.client.details.tabs.analysis.view.widgets.summary.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.elv.client.details.tabs.analysis.view.widgets.summary.events.ResourceChangedEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface ResourceChangedHandler extends EventHandler {

    void onResourceChanged(ResourceChangedEvent event);

}
