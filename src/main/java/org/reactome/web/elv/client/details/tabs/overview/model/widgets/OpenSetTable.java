package org.reactome.web.elv.client.details.tabs.overview.model.widgets;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.elv.client.common.data.model.OpenSet;
import org.reactome.web.elv.client.details.tabs.overview.model.widgets.factory.TableRowFactory;
import org.reactome.web.elv.client.details.tabs.overview.model.widgets.factory.PropertyType;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class OpenSetTable extends EntitySetTable {
    private OpenSet openSet;

    public OpenSetTable(OpenSet openSet) {
        super(openSet);
        this.openSet = openSet;
    }

    @Override
    protected Widget getTableRow(PropertyType propertyType) {
        String title = propertyType.getTitle();
        switch (propertyType){
            case REFERENCE_ENTITY:
                return TableRowFactory.getReferenceEntityRow(title, this.openSet.getReferenceEntity());
            default:
                return super.getTableRow(propertyType);
        }
    }
}
