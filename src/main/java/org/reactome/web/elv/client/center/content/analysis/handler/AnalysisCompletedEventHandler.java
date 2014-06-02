package org.reactome.web.elv.client.center.content.analysis.handler;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.elv.client.center.content.analysis.event.AnalysisCompletedEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface AnalysisCompletedEventHandler extends EventHandler {

    void onAnalysisCompleted(AnalysisCompletedEvent event);
}
