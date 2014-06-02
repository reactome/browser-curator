package org.reactome.web.elv.client.center.content.diagram.model;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.elv.client.common.ReactomeImages;
import org.reactome.web.elv.client.common.data.model.Figure;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class FigureContainer extends AbsolutePanel implements RequiresResize {
    private Button goToDiagram;
    private ScrollPanel figurePanel;

    public FigureContainer() {
        this.setStyleName("elv-Diagram-Figure");
        this.figurePanel = new ScrollPanel();
        this.add(figurePanel);
        this.goToDiagram = new Button(getButtonHTML());
        this.add(goToDiagram);
        this.setButtonPosition();
    }

    private static String getButtonHTML(){
        FlowPanel panel = new FlowPanel();
        panel.setWidth("140px");
        Image image = new Image(ReactomeImages.INSTANCE.back());
        image.getElement().getStyle().setMarginRight(10, Style.Unit.PX);
        panel.add(image);
        panel.add(new InlineLabel("Return to Diagram"));
        return panel.toString();
    }

    @Override
    public void onResize() {
        this.figurePanel.setWidth("100%");
        this.figurePanel.setHeight("100%");
        this.setButtonPosition();
    }

    private void setButtonPosition(){
        Element e = this.getElement().getParentElement();
        if(this.goToDiagram==null || e==null) return;
        int dw = this.goToDiagram.getOffsetWidth() + (this.figurePanel.getMaximumVerticalScrollPosition()>0 ? 30 : 20);
        int dh = this.goToDiagram.getOffsetHeight() + (this.figurePanel.getMaximumHorizontalScrollPosition()>0 ? 30 : 20);
        this.setWidgetPosition(this.goToDiagram, e.getOffsetWidth() - dw, e.getOffsetHeight() - dh);
    }

    public void setFigure(Figure figure){
        String url = figure.getDisplayName();
        this.figurePanel.clear();
        Image image = new Image(url);
        image.getElement().getStyle().setMarginBottom(50, Style.Unit.PX);
        this.figurePanel.add(image);
    }

    public void setGoToDiagramClickHandler(ClickHandler handler){
        this.goToDiagram.addClickHandler(handler);
    }
}
