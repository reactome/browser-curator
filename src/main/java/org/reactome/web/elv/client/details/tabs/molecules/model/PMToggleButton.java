package org.reactome.web.elv.client.details.tabs.molecules.model;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.ToggleButton;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class PMToggleButton extends ToggleButton {
    private static final int LEFT_MARGIN = 5;
    private static final int RIGHT_MARGIN = 5;

    private String key;

    public PMToggleButton(String upText) {
        super(upText);
        init(upText);
    }

    public PMToggleButton(String upText, Integer num) {
        super(upText + " (" + num + ")");
        init(upText);
    }

    public PMToggleButton(String upText, ClickHandler handler) {
        super(upText, handler);
        init(upText);
    }

    private void init(String key){
        this.key = key;
        getElement().getStyle().setMarginLeft(LEFT_MARGIN, Style.Unit.PX);
        getElement().getStyle().setMarginRight(RIGHT_MARGIN, Style.Unit.PX);
    }

    public String getKey() {
        return key;
    }

}
