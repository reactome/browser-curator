package org.reactome.web.elv.client.common.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface ELVEventHandler<T> extends EventHandler {

    void onEventThrown(ELVEvent<T> event);

}
