package org.reactome.web.elv.client.details.tabs.analysis.view.widgets.results.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.common.data.model.Species;
import org.reactome.web.elv.client.details.tabs.analysis.view.widgets.results.handlers.PathwaySelectedHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class PathwaySelectedEvent extends GwtEvent<PathwaySelectedHandler> {
    public static Type<PathwaySelectedHandler> TYPE = new GwtEvent.Type<PathwaySelectedHandler>();

    private Long species;
    private Long diagram;
    private Long pathway;

    public PathwaySelectedEvent(Long species, Long diagram, Long pathway) {
        this.species = species;
        this.diagram = diagram;
        this.pathway = pathway;
    }

    public Long getSpecies() {
        return species;
    }

    public Long getDiagram() {
        return diagram;
    }

    public Long getPathway() {
        return pathway;
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
