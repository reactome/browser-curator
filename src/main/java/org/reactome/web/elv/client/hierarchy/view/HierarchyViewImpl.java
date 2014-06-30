package org.reactome.web.elv.client.hierarchy.view;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.elv.client.common.analysis.model.PathwaySummary;
import org.reactome.web.elv.client.common.data.model.Event;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.common.data.model.Species;
import org.reactome.web.elv.client.common.model.Path;
import org.reactome.web.elv.client.common.utils.Console;
import org.reactome.web.elv.client.common.widgets.DialogBoxFactory;
import org.reactome.web.elv.client.common.widgets.glass.GlassPanel;
import org.reactome.web.elv.client.hierarchy.events.HierarchyTreeSpeciesNotFoundException;
import org.reactome.web.elv.client.hierarchy.model.HierarchyContainerPanel;
import org.reactome.web.elv.client.hierarchy.model.HierarchyItem;
import org.reactome.web.elv.client.hierarchy.model.HierarchyTree;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class HierarchyViewImpl implements IsWidget, HierarchyView, OpenHandler<TreeItem>, SelectionHandler<TreeItem> {

	private Presenter presenter;

    private DockLayoutPanel container;
    private HierarchyTree hierarchyTree;
    private HierarchyContainerPanel hierarchyContainerPanel;

    private HierarchyItem selectedItem;
    private HierarchyItem highlightedItem;

    /**
     * The glass element.
     */
    private GlassPanel glass = null;

    public HierarchyViewImpl() {
        this.container = new DockLayoutPanel(Style.Unit.PX){
            @Override
            public void onResize() {
                super.onResize();
                if(glass!=null){
                    glass.onResize();
                }
            }
        };
        this.container.setStyleName("elv-Hierarchy-Content");

        Label pathwayLabel = new Label("Event Hierarchy:");
        pathwayLabel.setStyleName("label");
        this.container.addNorth(pathwayLabel, 20);

        this.hierarchyContainerPanel = new HierarchyContainerPanel();
        this.container.add(this.hierarchyContainerPanel);
    }

    @Override
    public Widget asWidget() {
        return this.container;
	}

    @Override
    public void clearAnalysisResult() {
        if(this.hierarchyTree!=null){
            this.hierarchyTree.clearAnalysisData();
        }
    }

    private void clearHighlightedPath(){
        resetHighlightedItem();
        resetSelectedItem();
    }

    @Override
    public void expandPathway(Path path, Pathway pathway) {
        if(this.hierarchyTree==null) return;
        HierarchyItem treeItem = this.hierarchyTree.getHierarchyItemByDatabaseObject(path.getPathDbIds(), pathway);
        if(treeItem!=null){
            treeItem.setState(true, false);
            this.onOpen(treeItem);
        }else{
            Console.error(getClass() + " could not find the node for " + pathway);
        }
    }

    @Override
    public Set<Long> getContainedEventIds() {
        if(hierarchyTree==null) return new HashSet<Long>();
        return hierarchyTree.getHierarchyEventIds();
    }

    @Override
    public Set<Long> getHierarchyPathwaysWithReactionsLoaded() {
        if(hierarchyTree==null) return new HashSet<Long>();
        return hierarchyTree.getHierarchyPathwaysWithReactionsLoaded();
    }


    @Override
    public void highlightPath(Path path, Pathway pathway, Event event) {
        clearHighlightedPath();

        if(event==null){
            //Something that is not an event has been selected -> ONLY HIGHLIGHT THE PATH TO THE PATHWAY
            this.highlightedItem = this.hierarchyTree.getHierarchyItemByDatabaseObject(path.getPathDbIds(), pathway);
            this.highlightedItem.setSelected(true); //When going back in history, we need to set it to true
            this.hierarchyTree.setSelectedItem(this.highlightedItem, false);         //select the pathway
            this.highlightedItem.highlightPath();
            return;
        }

        if(!event.equals(pathway) && (event instanceof Pathway) && ((Pathway) event).getHasDiagram()){
            //A SUBPATHWAY has been selected -> SELECT THE Pathway and Highlight the Subpathway
            Path temp = path.getSubPath(path.size()-1);
            this.selectedItem = this.hierarchyTree.getHierarchyItemByDatabaseObject(temp.getPathDbIds(), pathway);
            this.hierarchyTree.setSelectedItem(this.selectedItem, false);

            this.highlightedItem = this.hierarchyTree.getHierarchyItemByDatabaseObject(path.getPathDbIds(), event);
            this.highlightedItem.highlightPath();
            return;
        }

        //An event has been selected
        this.selectedItem = this.hierarchyTree.getHierarchyItemByDatabaseObject(path.getPathDbIds(), event);
        if(this.selectedItem!=null){
            this.hierarchyTree.setSelectedItem(this.selectedItem, false);
            this.selectedItem.highlightPath();
        }else{
            Console.error(getClass() + " highlightPath -> selected item: UNEXPECTED NULL");
        }
    }

    @Override
    public void highlightHitReactions(Set<Long> reactionsHit) {
        if(hierarchyTree!=null){
            hierarchyTree.highlightHitReactions(reactionsHit);
        }
    }

    @Override
    public void loadItemChildren(Species species, Path path, Pathway pathway, List<Event> children) {
        if(pathway==null){
            this.hierarchyTree = new HierarchyTree(species);
            this.hierarchyTree.setScrollOnSelectEnabled(true);
            this.hierarchyTree.setAnimationEnabled(true);
            this.hierarchyTree.addOpenHandler(this);
            this.hierarchyTree.addSelectionHandler(this);
            this.hierarchyContainerPanel.addHierarchyTree(species, this.hierarchyTree);
        }
        if(path==null){
            this.hierarchyTree.loadPathwayChildren(null, pathway, children);
        }else{
            this.hierarchyTree.loadPathwayChildren(path.getPathDbIds(), pathway, children);
        }

        this.presenter.pathwayExpanded(pathway);
    }

    @Override
    public void onOpen(OpenEvent<TreeItem> event) {
        HierarchyItem treeItem = (HierarchyItem) event.getTarget();
        this.onOpen(treeItem);
    }

    private void onOpen(HierarchyItem item){
        Pathway pathway = (Pathway) item.getEvent();
        if (!item.isChildrenLoaded()) {
            Path path = item.getSelectedPath();
            this.presenter.eventChildrenRequired(path, pathway);
        }else{
            this.presenter.pathwayExpanded(pathway);
        }
        item.setState(true, false);
    }

    @Override
    public void onSelection(SelectionEvent<TreeItem> selectionEvent) {
        HierarchyItem item = (HierarchyItem) selectionEvent.getSelectedItem();
        if(item==null) return;

        Path path = item.getSelectedPath(); //.pruneLast();
        HierarchyItem pwd = item.getParentWithDiagram();
        if(pwd!=null){
            Pathway pathway = (Pathway) pwd.getEvent();
            Event event = item.getEvent();
            this.presenter.eventSelected(path, pathway, event);
        }else{
            DialogBoxFactory.alert("Events hierarchy", "Not pathway with diagram found in the path");
        }
    }

    private void resetHighlightedItem(){
        if(this.highlightedItem!=null){
            this.highlightedItem.clearHighlightPath();
            this.highlightedItem = null;
        }
    }

    private void resetSelectedItem(){
        if(this.selectedItem!=null){
            this.selectedItem.setSelected(false);
            this.selectedItem.clearHighlightPath();
            this.selectedItem = null;
        }
    }

    @Override
    public void setInitialState() {
        clearHighlightedPath();
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showHierarchyForSpecies(Species species) {
        try {
            this.hierarchyTree = this.hierarchyContainerPanel.getHierarchyTree(species);
            this.hierarchyContainerPanel.showHierarchyTree(species);
            this.presenter.hierarchyReady();
        } catch (HierarchyTreeSpeciesNotFoundException e) {
            this.hierarchyContainerPanel.showLoadingPanel(species);
            this.presenter.frontPageItemsRequired(species);
        }
    }

    @Override
    public void showAnalysisResult(List<PathwaySummary> pathwaySummaries) {
        this.hierarchyTree.showAnalysisData(pathwaySummaries);
    }

    @Override
    public void tourFadeIn() {
        if(glass==null){
            glass = new GlassPanel(this.container);
        }
        RootPanel.get().add(glass);
    }

    @Override
    public void tourFadeOut() {
        if(glass!=null){
            RootPanel.get().remove(glass);
        }
    }
}
