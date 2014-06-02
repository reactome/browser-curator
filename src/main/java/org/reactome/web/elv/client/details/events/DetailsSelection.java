package org.reactome.web.elv.client.details.events;

import org.reactome.web.elv.client.common.data.model.Event;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.common.model.Path;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DetailsSelection {

    private Path path;
    private Pathway diagram;
    private Event event;

    public DetailsSelection(Path path, Pathway diagram, Event event) {
        this.path = path;
        this.diagram = diagram;
        this.event = event;
    }

    public Path getPath() {
        return path;
    }

    public Pathway getDiagram() {
        return diagram;
    }

    public Event getEvent() {
        return event;
    }
}
