package org.reactome.web.elv.client.details.tabs.analysis.view.widgets.results.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.elv.client.details.tabs.analysis.view.widgets.results.handlers.PathwaySelectedHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class PathwaySelectedEvent extends GwtEvent<PathwaySelectedHandler> {
    public static Type<PathwaySelectedHandler> TYPE = new GwtEvent.Type<PathwaySelectedHandler>();

    private String identifier;

    public PathwaySelectedEvent(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public Type<PathwaySelectedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PathwaySelectedHandler handler) {
        handler.onPathwaySelected(this);
    }
}
