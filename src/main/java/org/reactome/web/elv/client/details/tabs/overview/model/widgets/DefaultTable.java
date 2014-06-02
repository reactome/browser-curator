package org.reactome.web.elv.client.details.tabs.overview.model.widgets;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.details.model.widgets.TextPanel;
import org.reactome.web.elv.client.details.tabs.overview.model.widgets.factory.TableRowFactory;
import org.reactome.web.elv.client.details.tabs.overview.model.widgets.factory.PropertyType;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DefaultTable extends OverviewTable{
    private DatabaseObject databaseObject;

    public DefaultTable(DatabaseObject databaseObject) {
        this.databaseObject = databaseObject;
    }

    @Override
    public void initialize() {
        initWidget(getTableRow(null));
    }

    @Override
    protected Widget getTableRow(PropertyType propertyType) {
        TextPanel textPanel = new TextPanel("Table no available for " + databaseObject.toString());
        return TableRowFactory.getOverviewRow("Database Object", textPanel);
    }
}