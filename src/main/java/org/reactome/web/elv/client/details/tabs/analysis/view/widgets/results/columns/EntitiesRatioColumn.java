package org.reactome.web.elv.client.details.tabs.analysis.view.widgets.results.columns;

import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.i18n.client.NumberFormat;
import org.reactome.web.elv.client.common.analysis.model.PathwaySummary;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class EntitiesRatioColumn extends AbstractColumn<Number> {

    public EntitiesRatioColumn() {
        super(new NumberCell(NumberFormat.getDecimalFormat()), "Entities", "ratio");
        setWidth(65);
    }

    @Override
    public Number getValue(PathwaySummary object) {
        if (object == null) return null;
        return object.getEntities().getRatio();
    }
}
