package org.reactome.web.elv.client.details.tabs.overview.model.widgets;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.elv.client.common.data.model.GenomeEncodedEntity;
import org.reactome.web.elv.client.details.tabs.overview.model.widgets.factory.PropertyType;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class GenomeEncodedEntityTable extends PhysicalEntityTable {
    @SuppressWarnings({"FieldCanBeLocal", "UnusedDeclaration"})
    private GenomeEncodedEntity genomeEncodedEntity;

    public GenomeEncodedEntityTable(GenomeEncodedEntity genomeEncodedEntity) {
        super(genomeEncodedEntity);
        this.genomeEncodedEntity = genomeEncodedEntity;
    }

    @Override
    protected Widget getTableRow(PropertyType propertyType) {
//        String title = propertyType.getTitle();
        switch (propertyType){
            default:
                return super.getTableRow(propertyType);
        }
    }
}
