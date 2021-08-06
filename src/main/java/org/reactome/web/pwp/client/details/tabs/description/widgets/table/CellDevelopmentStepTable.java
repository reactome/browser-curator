package org.reactome.web.pwp.client.details.tabs.description.widgets.table;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.pwp.client.common.model.classes.CellDevelopmentStep;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.PropertyType;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.TableRowFactory;

/**
 * @author Joel Weiser (joel.weiser@oicr.on.ca)
 */
public class CellDevelopmentStepTable extends ReactionLikeEventTable {
    private CellDevelopmentStep cellDevelopmentStep;

    public CellDevelopmentStepTable(CellDevelopmentStep cellDevelopmentStep) {
        super(cellDevelopmentStep);
        this.cellDevelopmentStep = cellDevelopmentStep;
    }

    @Override
    protected Widget getTableRow(PropertyType propertyType) {
        String title = propertyType.getTitle();
        switch (propertyType){
            case TISSUE:
                return TableRowFactory.getExternalOntologyRow(title, this.cellDevelopmentStep.getTissue());
            default:
                return super.getTableRow(propertyType);
        }
    }
}

