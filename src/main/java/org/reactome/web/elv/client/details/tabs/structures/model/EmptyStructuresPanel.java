package org.reactome.web.elv.client.details.tabs.structures.model;

import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class EmptyStructuresPanel extends StructuresPanel<Object> {

    @Override
    public void add(Object element) {
        //Nothing here
    }

    @Override
    public void setEmpty() {
        this.container.clear();
        this.container.add(new HTMLPanel("Object does not contains structures associated"));
    }
}
