package org.reactome.web.elv.client.details.tabs.analysis.view.widgets.results.columns;

import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.i18n.client.NumberFormat;
import org.reactome.web.elv.client.common.analysis.model.PathwaySummary;
import org.reactome.web.elv.client.details.tabs.analysis.view.widgets.common.cells.CustomNumberCell;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ReactionsRatioColumn extends AbstractColumn<Number> {

    public ReactionsRatioColumn() {
        super(new CustomNumberCell(CssColor.make("blue"), NumberFormat.getDecimalFormat()), "Reactions", "ratio");
        setWidth(85);
    }

    @Override
    public Number getValue(PathwaySummary object) {
        if(object==null) return null;
        return object.getReactions().getRatio();
    }
}
