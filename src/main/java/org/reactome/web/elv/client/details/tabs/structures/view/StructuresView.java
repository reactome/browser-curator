package org.reactome.web.elv.client.details.tabs.structures.view;

import org.reactome.web.elv.client.common.data.model.PhysicalEntity;
import org.reactome.web.elv.client.common.data.model.ReferenceSequence;
import org.reactome.web.elv.client.details.tabs.DetailsTabView;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface StructuresView extends DetailsTabView<StructuresView.Presenter> {

    public interface Presenter extends DetailsTabView.Presenter{
        void getProteinAccessions(PhysicalEntity physicalEntity, Long respId);
    }

    void refreshTitle(Integer loadedStructures, Integer proteinAccessions);
    void setProteinAccessions(List<ReferenceSequence> referenceSequenceList, Long respId);
}
