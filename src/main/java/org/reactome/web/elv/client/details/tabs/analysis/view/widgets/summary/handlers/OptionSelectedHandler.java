package org.reactome.web.elv.client.details.tabs.analysis.view.widgets.summary.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.elv.client.details.tabs.analysis.view.widgets.summary.events.OptionSelectedEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface OptionSelectedHandler extends EventHandler {

    public void onOptionSelected(OptionSelectedEvent event);

}
