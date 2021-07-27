package org.reactome.web.pwp.client.hierarchy.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.common.CommonImages;
import org.reactome.web.pwp.client.common.model.classes.*;
import org.reactome.web.pwp.client.common.model.util.Path;
import org.reactome.web.pwp.client.hierarchy.HierarchyDisplay;
import org.reactome.web.pwp.client.hierarchy.events.HierarchyItemDoubleClickedEvent;
import org.reactome.web.pwp.client.hierarchy.events.HierarchyItemMouseOutEvent;
import org.reactome.web.pwp.client.hierarchy.events.HierarchyItemMouseOverEvent;
import org.reactome.web.pwp.client.hierarchy.handlers.HierarchyItemDoubleClickedHandler;
import org.reactome.web.pwp.client.hierarchy.handlers.HierarchyItemMouseOutHandler;
import org.reactome.web.pwp.client.hierarchy.handlers.HierarchyItemMouseOverHandler;
import org.reactome.web.pwp.client.manager.state.token.Token;


/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class HierarchyItem extends TreeItem implements HasHandlers, MouseOverHandler, MouseOutHandler, DoubleClickHandler {

    private HandlerManager handlerManager = new HandlerManager(this);

    private Image icon;
    private boolean childrenLoaded = false;
    private FlowPanel textContainer;
    private InlineLabel analysisData;

    public HierarchyItem(Species species, Event event, boolean ehld) {
        super();
        setUserObject(event);
        init(species, event, ehld);
        initHandlers();
    }

    public HandlerRegistration addHierarchyItemDoubleClickedHandler(HierarchyItemDoubleClickedHandler handler){
        return handlerManager.addHandler(HierarchyItemDoubleClickedEvent.TYPE, handler);
    }

    public HandlerRegistration addHierarchyItemMouseOverHandler(HierarchyItemMouseOverHandler handler){
        return handlerManager.addHandler(HierarchyItemMouseOverEvent.TYPE, handler);
    }

    public HandlerRegistration addHierarchyItemMouseOutHandler(HierarchyItemMouseOutHandler handler){
        return handlerManager.addHandler(HierarchyItemMouseOutEvent.TYPE, handler);
    }

    private void init(Species species, Event event, boolean ehld){
        FlowPanel itemContent = new FlowPanel();
        itemContent.setStyleName(RESOURCES.getCSS().hierarchyItem());

        //A regular icon corresponding to the type of event has to be created first
        this.icon = new Image(event.getImageResource());
        if (ehld) {
            setEHLD();
        } else {
            this.icon.setTitle(event.getSchemaClass().name);
        }

        itemContent.add(this.icon);

        ImageResource status = event.getStatusIcon();
        if(status!=null){
            Image statusIcon = new Image(status);
            statusIcon.setTitle(event.getReleaseStatus().name());
            itemContent.add(statusIcon);
        }

        ImageResource inferred = event.getInferredIcon();
        if(inferred!=null){
            Image inferredIcon = new Image(inferred);
            if(species.getDbId().equals(Token.DEFAULT_SPECIES_ID)){
                inferredIcon.setTitle("Inferred from a non-human event");
            }else{
                inferredIcon.setTitle("Inferred from human event");
            }
            itemContent.add(inferredIcon);
        }

        ImageResource disease = event.getDiseaseIcon();
        if(event.isInDisease()){
            Image diseaseIcon = new Image(disease);
            diseaseIcon.setTitle("Is a disease");
            itemContent.add(diseaseIcon);
        }

        textContainer = new FlowPanel();
        textContainer.setStyleName(RESOURCES.getCSS().hierarchyTextContainer());

        InlineLabel label = new InlineLabel(event.getDisplayName());
        label.setTitle(event.getDisplayName());
        textContainer.add(label);

        analysisData = new InlineLabel("");
        Style ads = analysisData.getElement().getStyle();
        ads.setFontWeight(Style.FontWeight.BOLD);
        ads.setMarginLeft(6, Unit.PX);
        ads.setFontSize(11, Unit.PX);
        textContainer.add(analysisData);

        itemContent.add(textContainer);

        setWidget(itemContent);

        if(event instanceof Pathway || event instanceof CellLineagePath){
            FlowPanel loaderMsg = new FlowPanel();
            loaderMsg.add(new Image(CommonImages.INSTANCE.loader()));
            InlineLabel loadingLabel = new InlineLabel("Loading...");
            loadingLabel.getElement().getStyle().setMarginLeft(5, Unit.PX);
            loaderMsg.add(loadingLabel);
            addItem(loaderMsg); // Add a place holder so that this item can be opened.
        }
    }

    private void initHandlers(){
        Widget widget = getWidget();
        widget.sinkEvents(com.google.gwt.user.client.Event.ONMOUSEOVER);
        widget.sinkEvents(com.google.gwt.user.client.Event.ONMOUSEOUT);
        widget.sinkEvents(com.google.gwt.user.client.Event.ONDBLCLICK);
        widget.addHandler(this, MouseOverEvent.getType());
        widget.addHandler(this, MouseOutEvent.getType());
        widget.addHandler(this, DoubleClickEvent.getType());
    }

    public void clearAnalysisData(){
        analysisData.setText("");
        textContainer.removeStyleName(RESOURCES.getCSS().hierarchyItemHit());
        for (int i = 0; i < getChildCount(); i++) {
            TreeItem child = getChild(i);
            if(child instanceof HierarchyItem){
                ((HierarchyItem) child).clearAnalysisData();
            }
        }
    }

    public void clearHighlight(){
        textContainer.removeStyleName(RESOURCES.getCSS().hierarchyItemSelected());
        textContainer.removeStyleName(RESOURCES.getCSS().hierarchyItemHighlighted());
    }


    public boolean isChildrenLoaded() {
        return childrenLoaded;
    }

    public Event getEvent() {
        return (Event) getUserObject();
    }

    public HierarchyItem getParentWithDiagram(){
        if(this.hasDiagram()){
            return this;
        }else{
            HierarchyItem parent = (HierarchyItem) this.getParentItem();
            if(parent!=null){
                return parent.getParentWithDiagram();
            }
            return null;
        }
    }

    public boolean hasDiagram(){
        DatabaseObject databaseObject = getEvent();
        return (databaseObject instanceof Pathway && ((Pathway) databaseObject).getHasDiagram()) ||
               (databaseObject instanceof CellLineagePath && ((CellLineagePath) databaseObject).getHasDiagram());
    }

    public void highlightPath(){
        if(isSelected()){
            textContainer.addStyleName(RESOURCES.getCSS().hierarchyItemSelected());
        }else{
            textContainer.addStyleName(RESOURCES.getCSS().hierarchyItemHighlighted());
        }
        if(getParentItem()!=null){
            ((HierarchyItem) getParentItem()).highlightPath();
        }
    }

    public void setEHLD(){
        this.icon.setResource(HierarchyDisplay.RESOURCES.ehldPathway());
        this.icon.setTitle("Pathway/CellLineagePath with an enhanced diagram");
    }

    public void setChildrenLoaded(boolean childrenLoaded) {
        this.childrenLoaded = childrenLoaded;
    }

    public Path getPath(){
        if(getParentItem()==null) {
            return new Path();
        }else{
            return ((HierarchyItem) getParentItem()).getPathToItem();
        }
    }

    private Path getPathToItem(){
        if(getParentItem()==null){
            return new Path(this.getEvent());
        }else{
            Path path = ((HierarchyItem) this.getParentItem()).getPathToItem();
            if(this.getEvent() instanceof Pathway || this.getEvent() instanceof CellLineagePath){
                path.add(this.getEvent());
            }
            return path;
        }
    }

    public void highlightHitEvent(){
        textContainer.addStyleName(RESOURCES.getCSS().hierarchyItemHit());
    }

    @Override
    public void onDoubleClick(DoubleClickEvent event) {
        fireEvent(new HierarchyItemDoubleClickedEvent(this));
    }

    @Override
    public void onMouseOver(MouseOverEvent event) {
        fireEvent(new HierarchyItemMouseOverEvent(this));
    }

    @Override
    public void onMouseOut(MouseOutEvent event) {
        fireEvent(new HierarchyItemMouseOutEvent());
    }


    @Override
    public void fireEvent(GwtEvent<?> event) {
        handlerManager.fireEvent(event);
    }

    public static final Resources RESOURCES;
    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    /**
     * A ClientBundle of resources used by this widget.
     */
    public interface Resources extends ClientBundle {
        /**
         * The styles used in this widget.
         */
        @Source(ResourceCSS.CSS)
        ResourceCSS getCSS();
    }

    /**
     * Styles used by this widget.
     */
    @CssResource.ImportedWithPrefix("pwp-HierarchyItem")
    public interface ResourceCSS extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "org/reactome/web/pwp/client/hierarchy/widget/HierarchyItem.css";

        String hierarchyItem();

        String hierarchyTextContainer();

        String hierarchyItemSelected();

        String hierarchyItemHighlighted();

        String hierarchyItemHit();
    }

}