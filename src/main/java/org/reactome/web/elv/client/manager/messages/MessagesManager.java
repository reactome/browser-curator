package org.reactome.web.elv.client.manager.messages;

import com.google.gwt.user.client.Window;
import org.reactome.web.elv.client.center.content.analysis.event.AnalysisErrorEvent;
import org.reactome.web.elv.client.common.Controller;
import org.reactome.web.elv.client.common.EventBus;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class MessagesManager extends Controller {

    public MessagesManager(EventBus eventBus) {
        super(eventBus);
    }

    @Override
    public void onStateManagerWrongStateReached(String token) {
        Window.alert(token + " is not build properly. Please check it complies with the format");
    }

    @Override
    public void onAnalysisError(AnalysisErrorEvent event) {
        Window.alert(event.getErrorType().getMessage());
    }
}
