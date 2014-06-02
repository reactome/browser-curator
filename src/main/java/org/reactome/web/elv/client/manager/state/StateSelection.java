package org.reactome.web.elv.client.manager.state;

import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Event;
import org.reactome.web.elv.client.common.data.model.Pathway;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class StateSelection {

    private List<Event> path;
    private Pathway pathway;
    private DatabaseObject databaseObject;

    public StateSelection(List<Event> path, Pathway diagram, DatabaseObject databaseObject) {
        this.path = path;
        this.pathway = diagram;
        this.databaseObject = databaseObject;
        //The diagram does not appear in the path variable URL but internally has to be taken into account
        if(!this.path.contains(diagram)){
            this.path.add(diagram);
        }
    }

    public List<Event> getPath() {
        return path;
    }

    public Pathway getPathway() {
        return pathway;
    }

    public DatabaseObject getDatabaseObject() {
        return databaseObject;
    }
}
