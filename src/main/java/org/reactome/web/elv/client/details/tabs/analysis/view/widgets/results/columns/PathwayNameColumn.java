package org.reactome.web.elv.client.details.tabs.analysis.view.widgets.results.columns;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style;
import org.reactome.web.elv.client.common.analysis.model.PathwaySummary;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class PathwayNameColumn extends AbstractColumn<String> {

    public PathwayNameColumn() {
        super(new TextCell(), Style.TextAlign.LEFT, "", "Pathway name");
        setDataStoreName(COLUMN_NAME_TITLE);
        setWidth(400);
        setHorizontalAlignment(ALIGN_LEFT);
    }

    @Override
    public String getValue(PathwaySummary object) {
        return object!=null?object.getName():"";
    }

}