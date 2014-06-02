package org.reactome.web.elv.client.details.tabs.analysis.view.widgets.notfound;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.view.client.ProvidesKey;
import org.reactome.web.elv.client.common.analysis.model.IdentifierSummary;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class NotFoundTable extends DataGrid<IdentifierSummary> {
    public final static Integer PAGE_SIZE = 40;

    public NotFoundTable() {
        super(PAGE_SIZE, new ProvidesKey<IdentifierSummary>() {
            @Override
            public Object getKey(IdentifierSummary item) {
                return item == null ? null : item.getId();
            }
        });

        this.addColumn(new Column<IdentifierSummary, String>(new TextCell()) {
            @Override
            public String getValue(IdentifierSummary object) {
                return object.getId();
            }
        }, "Not found identifiers");

        this.setAutoHeaderRefreshDisabled(true);
        this.setWidth("100%");
        this.setVisible(true);
    }
}
