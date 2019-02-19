package org.reactome.web.pwp.client.common.model.util;


import org.reactome.web.pwp.client.common.model.classes.DatabaseObject;
import org.reactome.web.pwp.client.common.model.classes.Event;
import org.reactome.web.pwp.client.common.model.classes.Pathway;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class Path implements Iterable<Event> {

    private List<Event> path;

    public Path() {
        this.path = new LinkedList<>();
    }

    public Path(Event... event) {
        this();
        Collections.addAll(this.path, event);
    }

    public Path(List<Event> path) {
        this.path = path;
    }

    Path(Event databaseObject){
        this();
        this.add(databaseObject);
    }

    public void add(Event databaseObject){
        this.path.add(databaseObject);
    }

    public List<Event> asList() {
        return path;
    }

    public List<Long> getPathDbIds(){
        List<Long> identifiers = new LinkedList<>();
        for (DatabaseObject object : this.path) {
            identifiers.add(object.getDbId());
        }
        return identifiers;
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

    @Override
    public Iterator<Event> iterator() {
        return this.path.iterator();
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

    public org.reactome.web.pwp.client.common.model.util.Path getSubPath(int length){
        org.reactome.web.pwp.client.common.model.util.Path temp = new org.reactome.web.pwp.client.common.model.util.Path();
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        org.reactome.web.pwp.client.common.model.util.Path path1 = (org.reactome.web.pwp.client.common.model.util.Path) o;

        return !(path != null ? !path.equals(path1.path) : path1.path != null);

    }

    @Override
    public int hashCode() {
        return path != null ? path.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Path{" +
                "path=" + path +
                '}';
    }
}
