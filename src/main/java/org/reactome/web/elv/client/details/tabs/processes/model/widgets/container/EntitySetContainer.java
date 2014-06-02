package org.reactome.web.elv.client.details.tabs.processes.model.widgets.container;

import org.reactome.web.elv.client.common.data.model.EntitySet;
import org.reactome.web.elv.client.common.data.model.PhysicalEntity;
import org.reactome.web.elv.client.details.model.widgets.PhysicalEntityPanel;
import org.reactome.web.elv.client.details.tabs.processes.events.ProcessesDataListener;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class EntitySetContainer extends ProcessesContainer {

    public EntitySetContainer(PhysicalEntity physicalEntity) {
        this.showWaitingMessage();
        ProcessesDataListener.getProcessesDataListener().onEntitySetRequired(this, physicalEntity);
    }

    public void onEntitySetsRetrieved(List<EntitySet> entitySetList){
        this.clear();
        for (EntitySet entitySet : entitySetList) {
            this.add(new PhysicalEntityPanel(entitySet));
        }
        this.cleanUp();
    }
}
