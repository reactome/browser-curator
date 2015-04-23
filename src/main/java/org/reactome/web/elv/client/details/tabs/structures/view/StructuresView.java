package org.reactome.web.elv.client.details.tabs.structures.view;

import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.ReferenceEntity;
import org.reactome.web.elv.client.details.tabs.DetailsTabView;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface StructuresView extends DetailsTabView<StructuresView.Presenter> {

    public interface Presenter extends DetailsTabView.Presenter{
        void getReferenceEntities(DatabaseObject databaseObject, Long respId);
    }

    void refreshTitle(Integer loadedStructures, Integer proteinAccessions);
    void setReferenceEntities(List<ReferenceEntity> referenceSequenceList, Long respId);
}
