package org.reactome.web.elv.client.details.tabs.analysis.view.widgets.results.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.elv.client.details.tabs.analysis.view.widgets.results.events.PathwaySelectedEvent;
import org.reactome.web.elv.client.details.tabs.analysis.view.widgets.results.events.ResultPathwaySelectedEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface PathwaySelectedHandler extends EventHandler {

    void onPathwaySelected(PathwaySelectedEvent event);

}
