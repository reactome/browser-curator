package org.reactome.web.elv.client.details.tabs.processes.model.widgets.container;

import org.reactome.web.elv.client.common.data.model.*;
import org.reactome.web.elv.client.details.model.widgets.PhysicalEntityPanel;
import org.reactome.web.elv.client.details.tabs.processes.events.ProcessesDataListener;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class OtherFormsForEwasContainer extends ProcessesContainer {

    public OtherFormsForEwasContainer(EntityWithAccessionedSequence ewas) {
        this.showWaitingMessage();
        ProcessesDataListener.getProcessesDataListener().onOtherFormsOfEWASRequired(this, ewas);
    }

    public void onOtherFormsForEWASRetrieved(List<EntityWithAccessionedSequence> ewasList){
        this.clear();
        for (EntityWithAccessionedSequence ewas : ewasList) {
            this.add(new PhysicalEntityPanel(ewas));
        }
        this.cleanUp();
    }
}
