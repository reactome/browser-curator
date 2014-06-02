package org.reactome.web.elv.client.details.tabs.overview.model.widgets;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.elv.client.common.data.model.BlackBoxEvent;
import org.reactome.web.elv.client.details.tabs.overview.model.widgets.factory.PropertyType;
import org.reactome.web.elv.client.details.tabs.overview.model.widgets.factory.TableRowFactory;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class BlackBoxEventTable extends ReactionLikeEventTable {
    private BlackBoxEvent blackBoxEvent;

    public BlackBoxEventTable(BlackBoxEvent blackBoxEvent) {
        super(blackBoxEvent);
        this.blackBoxEvent = blackBoxEvent;
    }

    @Override
    public Widget getTableRow(PropertyType propertyType) {
        String title = propertyType.getTitle();
        switch (propertyType){
            case TEMPLATE:
                return TableRowFactory.getEventRow(title, blackBoxEvent.getTemplateEvent());
            default:
                return super.getTableRow(propertyType);
        }
    }
}
