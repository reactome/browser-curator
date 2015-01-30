package org.reactome.web.elv.client.details.tabs.analysis.view.widgets.results.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.elv.client.details.tabs.analysis.view.widgets.results.events.PathwayHoveredResetEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface PathwayHoveredResetHandler extends EventHandler {

    void onPathwayHoveredReset(PathwayHoveredResetEvent event);

}
