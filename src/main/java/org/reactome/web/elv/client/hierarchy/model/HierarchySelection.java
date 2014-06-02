package org.reactome.web.elv.client.hierarchy.model;

import org.reactome.web.elv.client.common.data.model.Event;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.common.model.Path;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class HierarchySelection {

    private Path path;
    private Pathway diagram;
    private Event event;

    public HierarchySelection(Path path, Pathway diagram, Event event) {
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
