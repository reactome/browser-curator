package org.reactome.web.elv.client.details.tabs.overview.model.widgets;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.elv.client.common.data.model.*;
import org.reactome.web.elv.client.details.tabs.overview.model.widgets.factory.TableRowFactory;
import org.reactome.web.elv.client.details.tabs.overview.model.widgets.factory.PropertyType;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 *
 */
public class PathwayTable extends EventTable {
    private Pathway pathway;
    
    public PathwayTable(Pathway pathway) {
        super(pathway);
        this.pathway = pathway;
    }

    @Override
    protected Widget getTableRow(PropertyType propertyType) {
        String title = propertyType.getTitle();
        switch (propertyType){
            case DOI:
                return TableRowFactory.getTextPanelRow(title, this.pathway.getDoi());
            default:
                return super.getTableRow(propertyType);
        }
    }
}
