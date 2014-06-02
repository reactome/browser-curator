package org.reactome.web.elv.client.details.tabs.analysis.view.widgets.results.columns;

import com.google.gwt.canvas.dom.client.CssColor;
import org.reactome.web.elv.client.common.analysis.model.PathwaySummary;
import org.reactome.web.elv.client.details.tabs.analysis.view.widgets.common.cells.CustomNumberCell;


/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ReactionsFoundColumn extends AbstractColumn<Number> {

    public ReactionsFoundColumn() {
        super(new CustomNumberCell(CssColor.make("blue")), "Reactions", "found");
        setWidth(85);
    }

    @Override
    public Integer getValue(PathwaySummary object) {
        if (object == null) return null;
        return object.getReactions().getFound();
    }
}
