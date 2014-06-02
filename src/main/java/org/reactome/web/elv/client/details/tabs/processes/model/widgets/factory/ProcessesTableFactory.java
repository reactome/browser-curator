package org.reactome.web.elv.client.details.tabs.processes.model.widgets.factory;


import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Event;
import org.reactome.web.elv.client.common.data.model.PhysicalEntity;
import org.reactome.web.elv.client.details.tabs.processes.model.widgets.table.DefaultTable;
import org.reactome.web.elv.client.details.tabs.processes.model.widgets.table.EventTable;
import org.reactome.web.elv.client.details.tabs.processes.model.widgets.table.PhysicalEntityTable;
import org.reactome.web.elv.client.details.tabs.processes.model.widgets.table.ProcessesTable;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public abstract class ProcessesTableFactory {

    public static ProcessesTable getOverviewTable(DatabaseObject databaseObject){
        ProcessesTable aux = _getOverviewTable(databaseObject);
        aux.initialize();
        return aux;
    }

    private static ProcessesTable _getOverviewTable(DatabaseObject databaseObject){
        /*********************** EVENTS ***********************/
        if(databaseObject instanceof Event){
            return new EventTable((Event) databaseObject);
        }

        /********************** ENTITIES **********************/
        if(databaseObject instanceof PhysicalEntity){
            return new PhysicalEntityTable((PhysicalEntity) databaseObject);
        }

        /********************** DEFAULT **********************/
        return new DefaultTable(databaseObject);
    }
}
