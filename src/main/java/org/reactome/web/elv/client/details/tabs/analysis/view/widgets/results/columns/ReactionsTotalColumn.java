package org.reactome.web.elv.client.details.tabs.analysis.view.widgets.results.columns;

import com.google.gwt.dom.client.Style;
import org.reactome.web.elv.client.common.analysis.model.PathwaySummary;
import org.reactome.web.elv.client.details.tabs.analysis.view.widgets.common.cells.CustomNumberCell;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ReactionsTotalColumn extends AbstractColumn<Number> {

    private static final String explanation = "The total number of reactions in the pathway for the selected molecular type";

    public ReactionsTotalColumn() {
        super(new CustomNumberCell(Style.FontStyle.ITALIC), "Reactions", "total", explanation);
        setWidth(85);
    }

    @Override
    public Number getValue(PathwaySummary object) {
        if(object==null) return null;
        return object.getReactions().getTotal();
    }
}
