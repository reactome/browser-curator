package org.reactome.web.elv.client.details.tabs.molecules.model.widget;

import com.google.gwt.user.client.ui.SimplePanel;

/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
public class QuantityBar extends SimplePanel {
    private SimplePanel innerPanel;

    public QuantityBar(int quantity, int total) {
        innerPanel = new SimplePanel();
        innerPanel.setHeight("99%");
        innerPanel.setWidth(((quantity*100)/total) + "%");
        this.add(innerPanel);
        innerPanel.addStyleName("quantity-colourBar");
    }
}
