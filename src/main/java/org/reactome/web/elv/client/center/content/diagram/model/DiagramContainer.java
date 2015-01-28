package org.reactome.web.elv.client.center.content.diagram.model;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RequiresResize;
import org.reactome.diagram.client.PathwayDiagramPanel;
import org.reactome.web.elv.client.common.ReactomeImages;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Event;
import org.reactome.web.elv.client.common.data.model.Figure;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.common.provider.InstanceTypeIconProvider;
import org.reactome.web.elv.client.common.widgets.button.DiagramButton;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DiagramContainer extends AbsolutePanel implements RequiresResize {
    private PathwayDiagramPanel diagramPanel;
    private HorizontalPanel buttonContainer;
    private ClickHandler clickHandler;

    public DiagramContainer(PathwayDiagramPanel diagramPanel) {
        this.setHeight("100%");
        this.diagramPanel = diagramPanel;
        this.add(diagramPanel);

        this.buttonContainer = new HorizontalPanel();
        this.buttonContainer.getElement().getStyle().setZIndex(3);
        this.add(this.buttonContainer);

        this.setButtonContainerPosition();
    }

    private void addFigureButton(ImageResource imageResource, Figure figure, DatabaseObject databaseObject){
        DiagramButton button = new DiagramButton(imageResource, figure);
        button.setTitle("Illustration for " + databaseObject.getDisplayName());
        if(clickHandler!=null){
            button.addClickHandler(this.clickHandler);
        }
        this.buttonContainer.add(button);
    }

    public void setFigureButtonsClickHandler(ClickHandler handler){
        this.clickHandler = handler;
    }

    public void setSelectedObjects(Pathway pathway, DatabaseObject databaseObject){
        this.buttonContainer.clear();

        if(pathway!=null && !pathway.getFigure().isEmpty()){
            ImageResource imageResource = InstanceTypeIconProvider.getItemIcon(pathway.getSchemaClass());
            for (Figure figure : pathway.getFigure()) {
                this.addFigureButton(imageResource, figure, pathway);
            }
        }
        if(databaseObject!=null && pathway!=databaseObject && databaseObject instanceof Event){
            Event event = (Event) databaseObject;
            ImageResource imageResource = InstanceTypeIconProvider.getItemIcon(event.getSchemaClass());
            for (Figure figure : event.getFigure()) {
                this.addFigureButton(imageResource, figure, event);
            }
        }

        DiagramButton fireworks = new DiagramButton(ReactomeImages.INSTANCE.fireworks(), pathway);
        if(this.clickHandler!=null){
            fireworks.addClickHandler(this.clickHandler);
        }
        this.buttonContainer.add(fireworks);

        setButtonContainerPosition();
    }

    @Override
    public void onResize() {
        Element e = this.getElement().getParentElement();
        if(e==null) return;
        int ow = e.getOffsetWidth();
        int oh = e.getOffsetHeight();
        if(ow>25 && oh>25){
            this.diagramPanel.setSize(ow + 25, oh + 25);
        }
        this.setButtonContainerPosition();
    }

    private void setButtonContainerPosition(){
        Element e = this.getElement().getParentElement();
        if(e==null) return;
        int dw = this.buttonContainer.getOffsetWidth() + 15;
        int dh = this.buttonContainer.getOffsetHeight() + 15;
        this.setWidgetPosition(this.buttonContainer, e.getOffsetWidth() - dw, e.getOffsetHeight() - dh);
    }
}