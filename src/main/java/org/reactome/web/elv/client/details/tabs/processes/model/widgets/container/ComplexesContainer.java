package org.reactome.web.elv.client.details.tabs.processes.model.widgets.container;

import org.reactome.web.elv.client.common.data.model.Complex;
import org.reactome.web.elv.client.common.data.model.PhysicalEntity;
import org.reactome.web.elv.client.details.model.widgets.PhysicalEntityPanel;
import org.reactome.web.elv.client.details.tabs.processes.events.ProcessesDataListener;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ComplexesContainer extends ProcessesContainer {

    public ComplexesContainer(PhysicalEntity physicalEntity) {
        this.showWaitingMessage();
        ProcessesDataListener.getProcessesDataListener().onComplexesRequired(this, physicalEntity);
    }

    public void onComplexesRetrieved(List<Complex> complexList){
        this.clear();
        for (Complex complex : complexList) {
            this.add(new PhysicalEntityPanel(complex));
        }
        this.cleanUp();
    }
}
