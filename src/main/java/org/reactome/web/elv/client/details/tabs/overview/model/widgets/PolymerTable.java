package org.reactome.web.elv.client.details.tabs.overview.model.widgets;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.elv.client.common.data.model.Polymer;
import org.reactome.web.elv.client.details.tabs.overview.model.widgets.factory.TableRowFactory;
import org.reactome.web.elv.client.details.tabs.overview.model.widgets.factory.PropertyType;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class PolymerTable extends PhysicalEntityTable {
    private Polymer polymer;

    public PolymerTable(Polymer polymer) {
        super(polymer);
        this.polymer = polymer;
    }

    @Override
    protected Widget getTableRow(PropertyType propertyType) {
        String title = propertyType.getTitle();
        switch (propertyType){
            case MAX_UNITS:
                return TableRowFactory.getTextPanelRow(title, this.polymer.getMaxUnitCount());
            case MIN_UNITS:
                return TableRowFactory.getTextPanelRow(title, this.polymer.getMinUnitCount());
            case REPEATED_UNITS:
                return TableRowFactory.getPhysicalEntityRow(title, this.polymer.getRepeatedUnits());
            default:
                return super.getTableRow(propertyType);
        }
    }
}
