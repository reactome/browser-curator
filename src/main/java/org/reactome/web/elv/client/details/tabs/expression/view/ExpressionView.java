package org.reactome.web.elv.client.details.tabs.expression.view;

import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.ReferenceSequence;
import org.reactome.web.elv.client.details.tabs.DetailsTabView;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface ExpressionView  extends DetailsTabView<ExpressionView.Presenter>{

    public interface Presenter extends DetailsTabView.Presenter {
        void getDetailedView(DatabaseObject databaseObject, Long respId);
        void getProteinAccessions(DatabaseObject databaseObject, Long respId);
    }

    void setDetailedView(DatabaseObject databaseObject, Long respId);
    void setProteinAccessions(List<ReferenceSequence> referenceSequenceList, Long respId);
}
