package org.reactome.web.elv.client.details.tabs.analysis.view.widgets.results.columns;

import com.google.gwt.cell.client.NumberCell;
import org.reactome.web.elv.client.common.analysis.model.PathwaySummary;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class EntitiesTotalColumn extends AbstractColumn<Number> {

    public EntitiesTotalColumn() {
        super(new NumberCell(), "Entities", "Total");
        setWidth(65);
    }

    @Override
    public Number getValue(PathwaySummary object) {
        if (object == null) return null;
        return object.getEntities().getTotal();
    }
}
