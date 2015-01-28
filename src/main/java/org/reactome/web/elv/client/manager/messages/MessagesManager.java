package org.reactome.web.elv.client.manager.messages;

import org.reactome.web.elv.client.center.content.analysis.event.AnalysisErrorEvent;
import org.reactome.web.elv.client.common.Controller;
import org.reactome.web.elv.client.common.EventBus;
import org.reactome.web.elv.client.common.utils.Console;
import org.reactome.web.elv.client.common.widgets.DialogBoxFactory;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
public class MessagesManager extends Controller {

    public MessagesManager(EventBus eventBus) {
        super(eventBus);
    }

    @Override
    public void onStateManagerWrongStateReached(String token) {
        //ToDo: Check if still in use?
        DialogBoxFactory.alert("URL Token error", token + " is not build properly. Please check it complies with the format");
    }

    @Override
    public void onAnalysisError(AnalysisErrorEvent event) {
        //ToDo: Check if still in use?
        DialogBoxFactory.alert("Analysis", event.getErrorType().getMessage());
    }

    @Override
    public void onInternalMessageSent(MessageObject msgObj){
        switch (msgObj.getMsgType()){
            case INTERNAL_ERROR:
//                DialogBoxFactory.alertMsg(msgObj.getClazz().getSimpleName(), msgObj.getMessage(), new Image(ReactomeImages.INSTANCE.exclamation()));
                Console.error(msgObj.getClazz().getSimpleName() + " -> " + msgObj.getMessage());
                break;
            case INTERNAL_WARNING:
//                DialogBoxFactory.alertMsg(msgObj.getClazz().getSimpleName(), msgObj.getMessage(), new Image(ReactomeImages.INSTANCE.information()));
                Console.warn(msgObj.getClazz().getSimpleName() + " -> " + msgObj.getMessage());
                break;
            case INTERNAL_INFO:
            default:
                Console.info(msgObj.getClazz().getSimpleName() + " -> " + msgObj.getMessage());
        }
    }
}
