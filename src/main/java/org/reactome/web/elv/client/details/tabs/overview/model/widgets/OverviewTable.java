package org.reactome.web.elv.client.details.tabs.overview.model.widgets;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.elv.client.details.tabs.overview.model.widgets.factory.PropertyType;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public abstract class OverviewTable extends Composite {

    public void initialize(){
        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("99%");

        for (PropertyType propertyType : PropertyType.values()) {
            Widget p = getTableRow(propertyType);
            if( p != null ){
                vp.add(p);
            }
        }
        initWidget(vp);
    }

    abstract protected Widget getTableRow(PropertyType propertyType);
}
