package org.reactome.web.elv.client.manager.messages;

import org.reactome.web.elv.client.center.content.analysis.event.AnalysisErrorEvent;
import org.reactome.web.elv.client.common.Controller;
import org.reactome.web.elv.client.common.EventBus;
import org.reactome.web.elv.client.common.widgets.DialogBoxFactory;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class MessagesManager extends Controller {

    public MessagesManager(EventBus eventBus) {
        super(eventBus);
    }

    @Override
    public void onStateManagerWrongStateReached(String token) {
        DialogBoxFactory.alert("URL Token error", token + " is not build properly. Please check it complies with the format");
    }

    @Override
    public void onAnalysisError(AnalysisErrorEvent event) {
        DialogBoxFactory.alert("Analysis", event.getErrorType().getMessage());
    }
}
