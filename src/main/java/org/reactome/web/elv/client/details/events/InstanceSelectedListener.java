package org.reactome.web.elv.client.details.events;

import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Event;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.common.model.Path;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class InstanceSelectedListener {

    private static InstanceSelectedListener instanceSelectedListener;
    private InstanceSelectedHandler handler;

    protected InstanceSelectedListener(){}

    public static InstanceSelectedListener getInstanceSelectedListener(){
        if(instanceSelectedListener==null)
            instanceSelectedListener = new InstanceSelectedListener();
        return instanceSelectedListener;
    }

    public void eventSelected(Path path, Pathway pathway, Event event){
        this.handler.eventSelected(path, pathway, event);
    }

    public void instanceSelected(DatabaseObject databaseObject){
        this.handler.instanceSelected(databaseObject);
    }

    public void setInstanceSelectedHandler(InstanceSelectedHandler handler) {
        this.handler = handler;
    }
}