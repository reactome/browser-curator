package org.reactome.web.elv.client.manager.ga;

import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Figure;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class GAManagerState {

    private DatabaseObject lastDatabaseObjectExpanded = null;
    private DatabaseObject lastDatabaseObjectSelected = null;
    private Figure lastFigureVisited = null;

    private int diagramKeyToggled = 0;

    public GAManagerState() {
    }

    public DatabaseObject getLastDatabaseObjectExpanded() {
        return lastDatabaseObjectExpanded;
    }

    public DatabaseObject getLastDatabaseObjectSelected() {
        return lastDatabaseObjectSelected;
    }

    public Figure getLastFigureVisited() {
        return lastFigureVisited;
    }

    //Toggled twice at the beginning that we do not want to track
    public boolean isDiagramKeyToggledValid(){
        return ++this.diagramKeyToggled > 2;
    }

    public void setLastFigureVisited(Figure lastFigureVisited) {
        this.lastFigureVisited = lastFigureVisited;
    }

    public void setLastDatabaseObjectExpanded(DatabaseObject lastDatabaseObjectExpanded) {
        this.lastDatabaseObjectExpanded = lastDatabaseObjectExpanded;
    }

    public void setLastDatabaseObjectSelected(DatabaseObject lastDatabaseObjectSelected) {
        this.lastDatabaseObjectSelected = lastDatabaseObjectSelected;
    }
}
