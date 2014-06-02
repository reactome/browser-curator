package org.reactome.web.elv.client.details.tabs.overview.model.widgets;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.elv.client.common.data.model.DatabaseIdentifier;
import org.reactome.web.elv.client.common.data.model.EntityWithAccessionedSequence;
import org.reactome.web.elv.client.common.data.model.OpenSet;
import org.reactome.web.elv.client.common.data.model.PhysicalEntity;
import org.reactome.web.elv.client.details.tabs.overview.model.widgets.factory.PropertyType;
import org.reactome.web.elv.client.details.tabs.overview.model.widgets.factory.TableRowFactory;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class PhysicalEntityTable extends OverviewTable {
    PhysicalEntity physicalEntity;

    public PhysicalEntityTable(PhysicalEntity physicalEntity) {
        this.physicalEntity = physicalEntity;
    }

    @Override
    protected Widget getTableRow(PropertyType propertyType) {
        String title = propertyType.getTitle();
        switch (propertyType){
            case STABLE_IDENTIFIER:
                return TableRowFactory.getStableIdentifierRow(title, this.physicalEntity.getStableIdentifier());
            case CELLULAR_COMPARTMENT:
                return TableRowFactory.getGOCellularComponentRow(title, this.physicalEntity.getCompartment());
            case DEDUCED_FROM:
                return TableRowFactory.getPhysicalEntityRow(title, this.physicalEntity.getInferredFrom());
            case DEDUCED_ON:
                return TableRowFactory.getPhysicalEntityRow(title, this.physicalEntity.getInferredTo());
            case REFERENCES:
                return TableRowFactory.getLiteratureReferencesRow(title, this.physicalEntity.getLiteratureReference());
            case CROSS_REFERENCES:
                List<DatabaseIdentifier> xrefs = this.physicalEntity.getCrossReference();
                if(this.physicalEntity instanceof EntityWithAccessionedSequence){
                    xrefs.addAll(((EntityWithAccessionedSequence) this.physicalEntity).getReferenceEntity().getCrossReference());
                }else if(this.physicalEntity instanceof OpenSet){
                    xrefs.addAll(((OpenSet) this.physicalEntity).getReferenceEntity().getCrossReference());
                }
                return TableRowFactory.getDatabaseIdentifierRow(title, xrefs);
            default:
                return null;
        }
    }
}
