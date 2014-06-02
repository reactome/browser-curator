package org.reactome.web.elv.client.center.content.analysis.handler;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.elv.client.center.content.analysis.event.AnalysisErrorEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface AnalysisErrorEventHandler extends EventHandler {

    public void onAnalysisError(AnalysisErrorEvent event);

}
