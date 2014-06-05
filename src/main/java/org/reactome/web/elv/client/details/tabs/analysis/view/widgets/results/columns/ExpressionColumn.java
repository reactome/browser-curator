package org.reactome.web.elv.client.details.tabs.analysis.view.widgets.results.columns;

import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.i18n.client.NumberFormat;
import org.reactome.web.elv.client.common.analysis.model.PathwaySummary;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ExpressionColumn extends AbstractColumn<Number> {

    private static final String explanation = "The average expression value for identifiers that match the pathway for the selected molecular type for the column named ";
    private Integer index;

    public ExpressionColumn(Integer index, String title) {
        super(new NumberCell(NumberFormat.getDecimalFormat()), "Expression", title, explanation + title);
        this.index = index;
        setWidth(100);
    }

    @Override
    public Number getValue(PathwaySummary object) {
        if(object==null) return null;
        return object.getEntities().getExp().get(index);
    }
}
