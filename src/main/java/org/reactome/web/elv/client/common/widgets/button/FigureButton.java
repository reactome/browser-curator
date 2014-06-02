package org.reactome.web.elv.client.common.widgets.button;

import com.google.gwt.dom.client.Style;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import org.reactome.web.elv.client.common.data.model.Figure;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class FigureButton extends Button {
    private Figure figure;

    public FigureButton(ImageResource imageResource, Figure figure) {
        super(FigureButton.getHTML(imageResource));
        setWidth("90px");
        this.figure = figure;
    }

    public Figure getFigure() {
        return figure;
    }

    private static String getHTML(ImageResource imageResource){
        FlowPanel panel = new FlowPanel();

        Image image = new Image(imageResource.getSafeUri());
        image.setSize("10px","10px");
        image.getElement().getStyle().setMarginRight(5, Style.Unit.PX);
        panel.add(image);

        InlineLabel label = new InlineLabel("Illustration");
        label.getElement().getStyle().setFontSize(11, Style.Unit.PX);
        panel.add(label);

        return panel.toString();
    }
}
