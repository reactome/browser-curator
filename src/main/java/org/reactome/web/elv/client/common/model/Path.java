package org.reactome.web.elv.client.common.model;

import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Event;
import org.reactome.web.elv.client.common.data.model.Pathway;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class Path {

    List<Event> path;

    public Path() {
        this.path = new LinkedList<Event>();
    }

    public Path(Event databaseObject){
        this();
        this.add(databaseObject);
    }

    public void add(Event databaseObject){
        this.path.add(databaseObject);
    }

    public List<Long> getPathDbIds(){
        List<Long> identifiers = new LinkedList<Long>();
        for (DatabaseObject object : this.path) {
            identifiers.add(object.getDbId());
        }
        return identifiers;
    }

    public List<Event> getPath() {
        return path;
    }

    public boolean contains(Event event){
        for (Event aux : this.path) {
            if(aux.equals(event)){
                return true;
            }
        }
        return false;
    }

    public boolean contains(List<Event> path){
        if(this.size() < path.size()) return false;

        for (Event id : path) {
            if (!this.contains(id)) {
                return false;
            }
        }
        return true;
    }

    public DatabaseObject get(int index){
        return this.path.get(index);
    }

    /**
     * Returns the pathway where the event is involved
     * @return the pathway where the event is involved
     */
    public Pathway getLastPathway(){
        int pos = this.path.size() - 2;
        return (Pathway) (pos > -1 ? this.path.get(pos) : this.path.get(0));
    }

    public Pathway getLastPathwayWithDiagram(){
        for (int i = this.path.size() - 1 ; i >= 0; i--) {
            Event event = this.path.get(i);
            if(event instanceof Pathway){
                Pathway pathway = (Pathway) event;
                if(pathway.getHasDiagram()) return pathway;
            }
        }
        return null;
    }

    public boolean isEmpty(){
        return this.path.isEmpty();
    }

    public boolean rootHasDiagram(){
        if(!this.path.isEmpty()){
            DatabaseObject databaseObject = this.path.get(0);
            if(databaseObject instanceof Pathway){
                return ((Pathway) databaseObject).getHasDiagram();
            }
        }
        return false;
    }

    public Path getSubPath(int length){
        Path temp = new Path();
        for (int i = 0; i < length; i++) {
            temp.add(this.path.get(i));
        }
        return temp;
    }

    public DatabaseObject remove(int index){
        return this.path.remove(index);
    }

    public Integer size(){
        return this.path.size();
    }

    @Override
    public String toString() {
        return "Path{" +
                "path=" + path +
                '}';
    }
}
