package org.reactome.web.elv.client.details.tabs.analysis.view.widgets.results.columns;

import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.i18n.client.NumberFormat;
import org.reactome.web.elv.client.common.analysis.model.PathwaySummary;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class EntitiesPValueColumn extends AbstractColumn<Number> {

    public EntitiesPValueColumn() {
        super(new NumberCell(NumberFormat.getFormat("#.##E0")), "Entities", "pValue");
    }

    @Override
    public Number getValue(PathwaySummary object) {
        if(object==null) return null;
        return object.getEntities().getpValue();
    }

}
