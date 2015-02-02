package org.reactome.web.elv.client.manager.title.event;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.elv.client.manager.title.handler.TitleChangedHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class TitleChangedEvent extends GwtEvent<TitleChangedHandler> {
    public static Type<TitleChangedHandler> TYPE = new Type<TitleChangedHandler>();

    private String title;

    public TitleChangedEvent(String title) {
        this.title = title;
    }

    @Override
    public Type<TitleChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(TitleChangedHandler handler) {
        handler.onTitleChanged(this);
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "TitleChangedEvent{" +
                "title='" + title + '\'' +
                '}';
    }
}
