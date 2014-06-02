package org.reactome.web.elv.client.details.tabs.overview.model.widgets;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.elv.client.common.data.model.EntitySet;
import org.reactome.web.elv.client.details.tabs.overview.model.widgets.factory.PropertyType;
import org.reactome.web.elv.client.details.tabs.overview.model.widgets.factory.TableRowFactory;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class EntitySetTable extends PhysicalEntityTable{
    private EntitySet entitySet;

    public EntitySetTable(EntitySet entitySet) {
        super(entitySet);
        this.entitySet = entitySet;
    }

    @Override
    protected Widget getTableRow(PropertyType propertyType) {
        String title = propertyType.getTitle();
        switch (propertyType){
            case HAS_MEMBER:
                return TableRowFactory.getPhysicalEntityRow(title, this.entitySet.getHasMember());
            default:
                return super.getTableRow(propertyType);
        }
    }
}
