package org.reactome.web.elv.client.details.tabs.analysis.view.widgets.results.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.elv.client.details.tabs.analysis.view.widgets.results.events.PathwayHoveredEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface PathwayHoveredHandler extends EventHandler {

    void onPathwayHovered(PathwayHoveredEvent event);

}
