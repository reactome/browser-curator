package org.reactome.web.elv.client.common;

import com.google.gwt.event.shared.SimpleEventBus;
import org.reactome.web.elv.client.common.events.ELVEvent;
import org.reactome.web.elv.client.common.events.ELVEventType;
import org.reactome.web.elv.client.common.utils.Console;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class EventBus extends SimpleEventBus {

    // In "Dev Mode" the events fired to the event bus will be shown in the console
    // with this string builder we provide a way to nest the string and provide a
    // visual indicator of which events are fired after each other
    private StringBuilder nestedSpace = new StringBuilder();

    public EventBus() {
        if(Console.VERBOSE){
            Console.info(""); //For DEV purposes, shows a line between last and current execution
        }
    }

    @SuppressWarnings("unchecked")
    public void fireELVEvent(ELVEventType eventType, Object obj) {
        if(Console.VERBOSE){
            this.nestedSpace.append(" ");
            Console.info("[EventBus] ->" + this.nestedSpace + eventType);
        }
        fireEvent(new ELVEvent(eventType, obj));
        if(Console.VERBOSE){
            this.nestedSpace.delete(1,2);
        }
    }

    public void fireELVEvent(ELVEventType eventType) {
        fireELVEvent(eventType, null);
    }
}
