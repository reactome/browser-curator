package org.reactome.web.elv.client.details.tabs.overview.model.widgets;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.elv.client.common.data.model.CandidateSet;
import org.reactome.web.elv.client.details.tabs.overview.model.widgets.factory.PropertyType;
import org.reactome.web.elv.client.details.tabs.overview.model.widgets.factory.TableRowFactory;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class CandidateSetTable extends EntitySetTable {
    private CandidateSet candidateSet;

    public CandidateSetTable(CandidateSet candidateSet) {
        super(candidateSet);
        this.candidateSet = candidateSet;
    }

    @Override
    protected Widget getTableRow(PropertyType propertyType) {
        String title = propertyType.getTitle();
        switch (propertyType){
            case HAS_CANDIDATE:
                return TableRowFactory.getPhysicalEntityRow(title, this.candidateSet.getHasCandidate());
            default:
                return super.getTableRow(propertyType);
        }
    }
}
