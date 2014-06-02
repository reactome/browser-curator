package org.reactome.web.elv.client.details.tabs.processes.model.widgets.container;

import org.reactome.web.elv.client.common.data.model.AbstractModifiedResidue;
import org.reactome.web.elv.client.common.data.model.EntityWithAccessionedSequence;
import org.reactome.web.elv.client.details.model.widgets.AbstractModifiedResiduePanel;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class AbstractModifiedResiduesContainer extends ProcessesContainer {

    public AbstractModifiedResiduesContainer(EntityWithAccessionedSequence physicalEntity) {
        for (AbstractModifiedResidue modifiedResidue : physicalEntity.getHasModifiedResidue()) {
            this.add(new AbstractModifiedResiduePanel(modifiedResidue));
        }
    }
}
