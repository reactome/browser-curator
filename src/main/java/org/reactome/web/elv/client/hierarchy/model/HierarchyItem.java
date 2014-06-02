package org.reactome.web.elv.client.hierarchy.model;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TreeItem;
import org.reactome.web.elv.client.common.ReactomeImages;
import org.reactome.web.elv.client.common.analysis.model.EntityStatistics;
import org.reactome.web.elv.client.common.analysis.model.PathwaySummary;
import org.reactome.web.elv.client.common.data.model.*;
import org.reactome.web.elv.client.common.model.Path;
import org.reactome.web.elv.client.common.provider.InstanceTypeIconProvider;
import org.reactome.web.elv.client.common.utils.Console;
import org.reactome.web.elv.client.manager.state.AdvancedState;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class HierarchyItem extends TreeItem {
    private boolean childrenLoaded = false;
    private FlowPanel text;
    private InlineLabel label;
    private InlineLabel analysisData;

    public HierarchyItem(Species species, Event event) {
        super();
        setUserObject(event);
        init(species, event);
    }

    private void init(Species species, Event event){
        FlowPanel itemContent = new FlowPanel();
        itemContent.getElement().getStyle().setDisplay(Style.Display.INLINE_BLOCK);

        Image icon = new Image(InstanceTypeIconProvider.getItemIcon(event.getSchemaClass()));
        itemContent.add(icon);

        if(event.getReleaseStatus() != EventStatus.REGULAR){
            Image status = new Image(InstanceTypeIconProvider.getItemStatusIcon(event.getReleaseStatus()));
            status.getElement().getStyle().setMarginLeft(2.5, Unit.PX);
            status.setTitle(event.getReleaseStatus().name());
            itemContent.add(status);
        }

        if(event.isInferred()){
            Image inferred = new Image(ReactomeImages.INSTANCE.isInferred());
            inferred.getElement().getStyle().setMarginLeft(2.5, Unit.PX);
            if(species.getDbId().equals(AdvancedState.DEFAULT_SPECIES_ID)){
                inferred.setTitle("Inferred from a non-human event");
            }else{
                inferred.setTitle("Inferred from human event");
            }
            itemContent.add(inferred);
        }

        if(event.isInDisease()){
            Image disease = new Image(ReactomeImages.INSTANCE.isDisease());
            disease.getElement().getStyle().setMarginLeft(2.5, Unit.PX);
            disease.setTitle("Is a disease");
            itemContent.add(disease);
        }

        text = new FlowPanel();
        text.getElement().getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        text.getElement().getStyle().setMarginLeft(5, Unit.PX);
        text.setStyleName("elv-Hierarchy-Item");


        label = new InlineLabel(event.getDisplayName());
        label.getElement().getStyle().setMarginLeft(2, Unit.PX);
        label.setTitle(event.getDisplayName());
        text.add(label);

        analysisData = new InlineLabel("");
        Style ads = analysisData.getElement().getStyle();
        ads.setFontWeight(Style.FontWeight.BOLD);
//        ads.setFontStyle(Style.FontStyle.ITALIC);
        ads.setMarginLeft(6, Unit.PX);
        ads.setFontSize(11, Unit.PX);
        text.add(analysisData);

        itemContent.add(text);

        setWidget(itemContent);

        if(event instanceof Pathway){
            FlowPanel loaderMsg = new FlowPanel();
            loaderMsg.add(new Image(ReactomeImages.INSTANCE.loader()));
            InlineLabel loadingLabel = new InlineLabel("Loading...");
            loadingLabel.getElement().getStyle().setMarginLeft(5, Unit.PX);
            loaderMsg.add(loadingLabel);
            addItem(loaderMsg); // Add a place holder so that this item can be opened.
        }
    }

    public void clearAnalysisData(){
        analysisData.setText("");
        for (int i = 0; i < getChildCount(); i++) {
            TreeItem child = getChild(i);
            if(child instanceof HierarchyItem){
                ((HierarchyItem) child).clearAnalysisData();
            }
        }
    }

    public void clearHighlightPath(){
        text.setStyleName("elv-Hierarchy-Item");
        if(getParentItem()!=null){
            ((HierarchyItem) getParentItem()).clearHighlightPath();
        }
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
        Pathway pathway = (databaseObject instanceof Pathway) ? (Pathway) databaseObject : null;
        return ( pathway==null ) ? false : pathway.getHasDiagram();
    }

    public void highlightPath(){
        if(isSelected()){
            text.setStyleName("elv-Hierarchy-Item-Selected");
        }else{
            text.setStyleName("elv-Hierarchy-Item-Highlighted");
        }
        if(getParentItem()!=null){
            ((HierarchyItem) getParentItem()).highlightPath();
        }
    }

    public void setChildrenLoaded(boolean childrenLoaded) {
        this.childrenLoaded = childrenLoaded;
    }

    public Path getSelectedPath(){
        if(getParentItem()==null){
            return new Path(this.getEvent());
        }else{
            Path path = ((HierarchyItem) this.getParentItem()).getSelectedPath();
            if(this.getEvent() instanceof Pathway){
                path.add(this.getEvent());
            }
            return path;
        }
    }

    public void showAnalysisData(PathwaySummary pathwaySummary){
        StringBuilder sb = new StringBuilder();
        EntityStatistics entityStatistics = pathwaySummary.getEntities();
        String found = NumberFormat.getDecimalFormat().format(entityStatistics.getFound());
        String total = NumberFormat.getDecimalFormat().format(entityStatistics.getTotal());
        sb.append("(").append(found).append("/").append(total).append(") ");
        NumberFormat nf = NumberFormat.getFormat("#.##E0");
//        if(entityStatistics.getFdr()!=null){
        sb.append("FDR: ").append(nf.format(entityStatistics.getFdr()));
//            Console.error("HierarchyItem: showAnalysisData FDR is NULL!");
//        }
        this.analysisData.setText(sb.toString());
    }
}