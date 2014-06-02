package org.reactome.web.elv.client.details.tabs.processes.model.widgets.table;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.details.model.widgets.TextPanel;
import org.reactome.web.elv.client.details.tabs.processes.model.widgets.factory.PropertyType;
import org.reactome.web.elv.client.details.tabs.processes.model.widgets.factory.TableRowFactory;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DefaultTable extends ProcessesTable {
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
        TextPanel textPanel = new TextPanel("Table no available for " + this.databaseObject.toString());
        return TableRowFactory.getProcessesRow("Database Object", textPanel);
    }
}
