package org.reactome.web.elv.client.common.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ELVEvent<T> extends GwtEvent<ELVEventHandler> {

    private T eventAttachedObject;

    private ELVEventType elvEventType;

    public static Type<ELVEventHandler> TYPE = new Type<ELVEventHandler>();

    public ELVEvent(ELVEventType elvEventType, T eventAttachedObject){
		this.eventAttachedObject = eventAttachedObject;
        this.elvEventType = elvEventType;
	}

    @Override
    public Type<ELVEventHandler> getAssociatedType() {
        return TYPE;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void dispatch(ELVEventHandler elvEventHandler) {
        elvEventHandler.onEventThrown(this);
    }

    public T getEventAttachedObject(){
        return eventAttachedObject;
    }

    public ELVEventType getELVEventType(){
        return elvEventType;
    }
}
