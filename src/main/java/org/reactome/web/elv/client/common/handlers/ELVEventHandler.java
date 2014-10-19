package org.reactome.web.elv.client.common.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.elv.client.common.events.ELVEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface ELVEventHandler<T> extends EventHandler {

    void onEventFired(ELVEvent<T> event);

}
