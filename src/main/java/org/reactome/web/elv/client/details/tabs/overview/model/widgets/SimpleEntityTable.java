package org.reactome.web.elv.client.details.tabs.overview.model.widgets;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.elv.client.common.data.model.SimpleEntity;
import org.reactome.web.elv.client.details.tabs.overview.model.widgets.factory.TableRowFactory;
import org.reactome.web.elv.client.details.tabs.overview.model.widgets.factory.PropertyType;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class SimpleEntityTable extends PhysicalEntityTable {
    private SimpleEntity entity;
    
    public SimpleEntityTable(SimpleEntity entity) {
        super(entity);
        this.entity = entity;
    }

    @Override
    protected Widget getTableRow(PropertyType propertyType) {
        String title = propertyType.getTitle();
        switch (propertyType){
//            case REFERENCE_ENTITY:
//                //return TableRowFactory.getReferenceEntityRow(title, entity.getReferenceEntity)));
//                return null;
            case CELLULAR_COMPARTMENT:
                return TableRowFactory.getGOCellularComponentRow(title, entity.getCompartment());
            case DEDUCED_FROM:
                return TableRowFactory.getPhysicalEntityRow(title, entity.getInferredFrom());
            case DEDUCED_ON:
                return TableRowFactory.getPhysicalEntityRow(title, entity.getInferredTo());
            default:
                return super.getTableRow(propertyType);
        }
    }
}