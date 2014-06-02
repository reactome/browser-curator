package org.reactome.web.elv.client.details.tabs.overview.model.widgets;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.elv.client.common.data.model.Complex;
import org.reactome.web.elv.client.details.tabs.overview.model.widgets.factory.TableRowFactory;
import org.reactome.web.elv.client.details.tabs.overview.model.widgets.factory.PropertyType;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ComplexTable extends PhysicalEntityTable {
    private Complex complex;
    
    public ComplexTable(Complex complex) {
        super(complex);
        this.complex = complex;
    }

    @Override
    protected Widget getTableRow(PropertyType propertyType) {
        String title = propertyType.getTitle();
        switch (propertyType){
            case HAS_COMPONENT:
                return TableRowFactory.getPhysicalEntityRow(title, this.complex.getHasComponent());
            case PRODUCED_BY:
                return TableRowFactory.getEventRow(title, this.complex.getProducedByEvent());
            case CONSUMED_BY:
                return TableRowFactory.getEventRow(title, this.complex.getConsumedByEvent());
            case ENTITY_ON_OTHER_CELL:
                return TableRowFactory.getPhysicalEntityRow(title, this.complex.getEntityOnOthercell());
            default:
                return super.getTableRow(propertyType);
        }
    }
}
