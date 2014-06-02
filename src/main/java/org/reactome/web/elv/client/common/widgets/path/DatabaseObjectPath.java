package org.reactome.web.elv.client.common.widgets.path;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.model.Path;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DatabaseObjectPath extends Composite {

    public DatabaseObjectPath(Path path) {
        initialize(path);
    }

    private void initialize(Path path){
        HorizontalPanel container = new HorizontalPanel();
        container.setSpacing(5);
        container.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        boolean addArrows = false;
        for (DatabaseObject databaseObject : path.getPath()) {
            if(addArrows)
                container.add(new HTMLPanel(">>"));
            else
                addArrows = true;
            Label label = new Label(databaseObject.getDisplayName());
            label.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
            container.add(label);
        }
        initWidget(container);
    }
}
