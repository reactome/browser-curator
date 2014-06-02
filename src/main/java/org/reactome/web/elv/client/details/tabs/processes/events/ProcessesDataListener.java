package org.reactome.web.elv.client.details.tabs.processes.events;

import org.reactome.web.elv.client.common.data.model.EntityWithAccessionedSequence;
import org.reactome.web.elv.client.common.data.model.Event;
import org.reactome.web.elv.client.common.data.model.PhysicalEntity;
import org.reactome.web.elv.client.details.tabs.processes.model.widgets.container.*;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ProcessesDataListener {

    private static ProcessesDataListener processesDataListener;
    private ProcessesDataHandler handler;

    protected ProcessesDataListener() {
    }

    public static ProcessesDataListener getProcessesDataListener(){
        if(processesDataListener==null){
            processesDataListener = new ProcessesDataListener();
        }
        return processesDataListener;
    }

    public void onComplexesRequired(ComplexesContainer container, PhysicalEntity physicalEntity){
        this.handler.onComplexesRequired(container, physicalEntity);
    }

    public void onEntitySetRequired(EntitySetContainer container, PhysicalEntity physicalEntity){
        this.handler.onEntitySetRequired(container, physicalEntity);
    }

    public void onOtherFormsOfEWASRequired(OtherFormsForEwasContainer container, EntityWithAccessionedSequence ewas){
        this.handler.onOtherFormsOfEWASRequired(container, ewas);
    }

    public void onPathwaysForEntitiesRequired(InvolvedPathwaysContainer container, PhysicalEntity physicalEntity) {
        this.handler.onPathwaysForEntitiesRequired(container, physicalEntity);
    }

    public void onPathwaysForEventsRequired(InvolvedPathwaysContainer container, Event event) {
        this.handler.onPathwaysForEventsRequired(container, event);
    }

    public void onReactionsWhereInputRequired(ReactionsAsInputContainer container, PhysicalEntity physicalEntity){
        this.handler.onReactionsWhereInputRequired(container, physicalEntity);
    }

    public void onReactionsWhereOutputRequired(ReactionsAsOutputContainer container, PhysicalEntity physicalEntity){
        this.handler.onReactionsWhereOutputRequired(container, physicalEntity);
    }

    public void setHandler(ProcessesDataHandler handler) {
        this.handler = handler;
    }
}
