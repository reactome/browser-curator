package org.reactome.web.elv.client.details.tabs.analysis.view.widgets.found.columns;

import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.i18n.client.NumberFormat;
import org.reactome.web.elv.client.common.analysis.model.PathwayIdentifier;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ExpressionColumn extends AbstractColumn<Number> {

    private static final String explanation = "The submitted expression value associated with the identifier for the column named ";
    private Integer index;

    public ExpressionColumn(Integer index, String title) {
        super(new NumberCell(NumberFormat.getDecimalFormat()), "Expression", title, explanation + title);
        this.index = index;
        setWidth(100);
    }

    @Override
    public Number getValue(PathwayIdentifier object) {
        if(object==null) return null;
        return object.getExp().get(index);
    }
}
