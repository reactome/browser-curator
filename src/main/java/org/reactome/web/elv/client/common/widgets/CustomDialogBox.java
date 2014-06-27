package org.reactome.web.elv.client.common.widgets;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public abstract class CustomDialogBox {

    public static DialogBox alertBox(final String header, final String content) {
        final DialogBox box = new DialogBox();
        VerticalPanel panel = new VerticalPanel();
        panel.getElement().getStyle().setPadding(10, Style.Unit.PX);
        panel.getElement().getStyle().setPaddingBottom(0, Style.Unit.PX);

        box.setText(header);
        for (String s : content.split("\n")) {
            panel.add(new Label(s));
        }
        final Button buttonClose = new Button("Close",new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                box.hide();
            }
        });
        buttonClose.getElement().getStyle().setMarginTop(10, Style.Unit.PX);
        buttonClose.setWidth("90px");
        panel.add(buttonClose);
        panel.setCellHorizontalAlignment(buttonClose, HasAlignment.ALIGN_RIGHT);
        box.add(panel);
        box.getElement().getStyle().setZIndex(1000);
        return box;
    }
}
