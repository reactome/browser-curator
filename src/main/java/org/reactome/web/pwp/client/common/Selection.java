package org.reactome.web.pwp.client.common;

import org.reactome.web.pwp.client.common.model.classes.DatabaseObject;
import org.reactome.web.pwp.client.common.model.classes.Event;
import org.reactome.web.pwp.client.common.model.util.Path;
import org.reactome.web.pwp.client.manager.state.State;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class Selection {
    private Event diagram;
    private DatabaseObject databaseObject;
    private Path path;

    public Selection(){
        this.diagram = null;
        this.databaseObject = null;
        this.path = new Path();
    }

    public Selection(State state){
        this.diagram = state.getEventWithDiagram();
        this.databaseObject = state.getSelected();
        this.path = state.getPath();
    }

    public Selection(DatabaseObject databaseObject) {
        this(null, databaseObject, null);
    }

    public Selection(Event diagram, Path path) {
       this(diagram, null, path);
    }

    public Selection(Event diagram, DatabaseObject databaseObject, Path path) {
        this.diagram = diagram;
        this.path = path == null ? new Path() : path;
        this.databaseObject = databaseObject;
    }

    public Event getDiagram() {
        return diagram;
    }

    public DatabaseObject getDatabaseObject() {
        return databaseObject;
    }

    public Path getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Selection selection = (Selection) o;

        return  !(diagram != null ? !diagram.equals(selection.diagram) : selection.diagram != null) &&
                !(databaseObject != null ? !databaseObject.equals(selection.databaseObject) : selection.databaseObject != null) &&
                !(path != null ? !path.equals(selection.path) : selection.path != null);

    }

    @Override
    public int hashCode() {
        int result = diagram != null ? diagram.hashCode() : 0;
        result = 31 * result + (databaseObject != null ? databaseObject.hashCode() : 0);
        result = 31 * result + (path != null ? path.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return  (diagram != null ? "DIAGRAM=" + diagram + ", ": "") +
                "SEL=" + databaseObject +
                (path != null ? ", path=" + path.size() : "");
    }
}
