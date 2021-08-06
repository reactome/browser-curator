package org.reactome.web.pwp.client.details.tabs.description.widgets.table;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.pwp.client.common.model.classes.CellLineagePath;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.PropertyType;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.TableRowFactory;

/**
 * @author Joel Weiser (joel.weiser@oicr.on.ca)
 */
public class CellLineagePathTable extends EventTable {
    private CellLineagePath cellLineagePath;

    public CellLineagePathTable(CellLineagePath cellLineagePath) {
        super(cellLineagePath);
        this.cellLineagePath = cellLineagePath;
    }

    @Override
    protected Widget getTableRow(PropertyType propertyType) {
        String title = propertyType.getTitle();
        switch (propertyType){
            case TISSUE:
                return TableRowFactory.getExternalOntologyRow(title, this.cellLineagePath.getTissue());
            default:
                return super.getTableRow(propertyType);
        }
    }
}
