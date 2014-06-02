package org.reactome.web.elv.client.details.tabs.analysis.view.widgets.results.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.elv.client.common.analysis.model.PathwaySummary;
import org.reactome.web.elv.client.details.tabs.analysis.view.widgets.results.handlers.ResultPathwaySelectedHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ResultPathwaySelectedEvent extends GwtEvent<ResultPathwaySelectedHandler> {
    public static Type<ResultPathwaySelectedHandler> TYPE = new GwtEvent.Type<ResultPathwaySelectedHandler>();

    PathwaySummary pathwaySummary;

    public ResultPathwaySelectedEvent(PathwaySummary pathwaySummary) {
        this.pathwaySummary = pathwaySummary;
    }

    @Override
    public Type<ResultPathwaySelectedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ResultPathwaySelectedHandler handler) {
        handler.onPathwayFoundEntitiesSelected(this);
    }

    public PathwaySummary getPathwaySummary() {
        return pathwaySummary;
    }
}
