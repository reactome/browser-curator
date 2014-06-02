package org.reactome.web.elv.client.details.tabs.processes.model.widgets.container;

import org.reactome.web.elv.client.common.data.model.Event;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.common.data.model.PhysicalEntity;
import org.reactome.web.elv.client.details.model.widgets.EventPanel;
import org.reactome.web.elv.client.details.tabs.processes.events.ProcessesDataListener;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class InvolvedPathwaysContainer extends ProcessesContainer {

    public InvolvedPathwaysContainer(PhysicalEntity physicalEntity) {
        this.showWaitingMessage();
        ProcessesDataListener.getProcessesDataListener().onPathwaysForEntitiesRequired(this, physicalEntity);
    }

    public InvolvedPathwaysContainer(Event event) {
        this.showWaitingMessage();
        ProcessesDataListener.getProcessesDataListener().onPathwaysForEventsRequired(this, event);
    }

    public void onPathwaysRetrieved(List<Pathway> pathwayList) {
        this.clear();
        for (Pathway pathway : pathwayList) {
            this.add(new EventPanel(pathway));
        }
        this.cleanUp();
    }
}
