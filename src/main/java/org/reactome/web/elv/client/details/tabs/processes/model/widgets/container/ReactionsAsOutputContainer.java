package org.reactome.web.elv.client.details.tabs.processes.model.widgets.container;

import org.reactome.web.elv.client.common.data.model.PhysicalEntity;
import org.reactome.web.elv.client.common.data.model.ReactionLikeEvent;
import org.reactome.web.elv.client.details.model.widgets.EventPanel;
import org.reactome.web.elv.client.details.tabs.processes.events.ProcessesDataListener;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ReactionsAsOutputContainer extends ProcessesContainer {

    public ReactionsAsOutputContainer(PhysicalEntity physicalEntity) {
        this.showWaitingMessage();
        ProcessesDataListener.getProcessesDataListener().onReactionsWhereOutputRequired(this, physicalEntity);
    }

    public void onReactionsRetrieved(List<ReactionLikeEvent> reactionList) {
        this.clear();
        for (ReactionLikeEvent reactionLikeEvent : reactionList) {
            this.add(new EventPanel(reactionLikeEvent));
        }
        this.cleanUp();
    }
}
