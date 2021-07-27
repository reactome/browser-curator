package org.reactome.web.pwp.client.details.tabs.expression;

import org.reactome.web.pwp.client.common.model.classes.DatabaseObject;
import org.reactome.web.pwp.client.common.model.classes.Event;
import org.reactome.web.pwp.client.common.model.classes.Pathway;
import org.reactome.web.pwp.client.common.model.classes.ReferenceSequence;
import org.reactome.web.pwp.client.details.tabs.DetailsTab;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface ExpressionTab {

    interface Presenter extends DetailsTab.Presenter {
        void getReferenceSequences(DatabaseObject databaseObject);
    }

    interface Display extends DetailsTab.Display<Presenter> {
        void showDetails(DatabaseObject databaseObject);
        void showProteins(DatabaseObject databaseObject);
        void showReferenceSequences(DatabaseObject databaseObject, List<ReferenceSequence> referenceSequenceList);
        void showEventWithDiagram(Event eventWithDiagram);
    }
}
