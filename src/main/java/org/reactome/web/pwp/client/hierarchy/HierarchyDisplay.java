package org.reactome.web.pwp.client.hierarchy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.common.model.classes.*;
import org.reactome.web.pwp.client.common.model.handlers.DatabaseObjectLoadedHandler;
import org.reactome.web.pwp.client.common.model.util.Path;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.hierarchy.events.HierarchyItemDoubleClickedEvent;
import org.reactome.web.pwp.client.hierarchy.events.HierarchyItemMouseOverEvent;
import org.reactome.web.pwp.client.hierarchy.events.HierarchyTreeSpeciesNotFoundException;
import org.reactome.web.pwp.client.hierarchy.handlers.HierarchyItemDoubleClickedHandler;
import org.reactome.web.pwp.client.hierarchy.handlers.HierarchyItemMouseOutHandler;
import org.reactome.web.pwp.client.hierarchy.handlers.HierarchyItemMouseOverHandler;
import org.reactome.web.pwp.client.hierarchy.widget.HierarchyContainer;
import org.reactome.web.pwp.client.hierarchy.widget.HierarchyItem;
import org.reactome.web.pwp.client.hierarchy.widget.HierarchyTree;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class HierarchyDisplay extends Composite implements OpenHandler<TreeItem>, SelectionHandler<TreeItem>,
        Hierarchy.Display, HierarchyItemMouseOverHandler, HierarchyItemMouseOutHandler, HierarchyItemDoubleClickedHandler {

    private Hierarchy.Presenter presenter;

    private HierarchyTree hierarchyTree;
    private HierarchyContainer hierarchyContainer;

    public HierarchyDisplay() {
        DockLayoutPanel container = new DockLayoutPanel(Style.Unit.PX);

        this.hierarchyContainer = new HierarchyContainer();
        this.hierarchyContainer.addStyleName(RESOURCES.getCSS().hierarchyPanel());

        FlowPanel title = new FlowPanel();
        title.add(new Image(RESOURCES.hierarchyIcon()));
        title.add(new InlineLabel("Event Hierarchy:"));
        title.setStyleName(RESOURCES.getCSS().hierarchyPanelTitle());
        container.addNorth(title, 25);
        container.add(this.hierarchyContainer);
        initWidget(container);

        container.getParent().addStyleName(RESOURCES.getCSS().hierarchyContainer());
    }

    @Override
    public void onHierarchyItemDoubleClicked(HierarchyItemDoubleClickedEvent e) {
        Event event = e.getItem().getEvent();
        if(event instanceof Pathway) {
            this.presenter.openDiagram((Pathway) event);
        } else if (event instanceof CellLineagePath) {
            this.presenter.openDiagram((CellLineagePath) event);
        }
    }

    @Override
    public void onHierarchyItemHoveredReset() {
        this.presenter.eventHoveredReset();
    }

    @Override
    public void onHierarchyItemMouseOver(HierarchyItemMouseOverEvent e) {
        HierarchyItem item = e.getItem();
        if (item == null) return;

        HierarchyItem pwd = item.getParentWithDiagram();
        //Pathway pathway = (Pathway) pwd.getEvent();
        Event event = item.getEvent();
        if (event.equals(pwd.getEvent())) event = null;

        Path path = pwd.getPath();
        if (pwd.getEvent() instanceof Pathway) {
            this.presenter.eventHovered((Pathway) pwd.getEvent(), event, path);
        } else if (pwd.getEvent() instanceof CellLineagePath) {
            this.presenter.eventHovered((CellLineagePath) pwd.getEvent(), event, path);
        }
    }

    @Override
    public void onOpen(OpenEvent<TreeItem> event) {
        HierarchyItem treeItem = (HierarchyItem) event.getTarget();
        this.onOpen(treeItem);
    }

    @Override
    public void onSelection(SelectionEvent<TreeItem> selectionEvent) {
        HierarchyItem item = (HierarchyItem) selectionEvent.getSelectedItem();
        if (item == null) return;

        this.hierarchyTree.clearHighlights();
        item.highlightPath();

        HierarchyItem pwd = item.getParentWithDiagram();
        if (pwd != null) {
            //final Pathway pathway = (Pathway) pwd.getEvent();
            Event aux = item.getEvent();
            final Event event = pwd.getEvent().equals(aux) ? null : aux;
            final Path path = pwd.getPath();
            //This is needed because the State will be checking the species of the pathway and it needs to be ready
            pwd.getEvent().load(new DatabaseObjectLoadedHandler() {
                @Override
                public void onDatabaseObjectLoaded(DatabaseObject databaseObject) {
                    if (databaseObject instanceof Pathway) {
                        presenter.eventSelected((Pathway) databaseObject, event, path);
                    } else if (databaseObject instanceof CellLineagePath) {
                        presenter.eventSelected((CellLineagePath) databaseObject, event, path);
                    }
                }

                @Override
                public void onDatabaseObjectError(Throwable trThrowable) {
                    Console.error("There has been an error retrieving data for " + pwd.getEvent().getDisplayName(), HierarchyDisplay.this);
                }
            });

        } else {
            Console.error("Events hierarchy: No pathway or cell lineage path with diagram found in the path.", this);
        }
    }

    @Override
    public void clearAnalysisResult() {
        if(this.hierarchyTree == null) return;
        for (HierarchyItem item : this.hierarchyTree.getHierarchyItems()) {
            item.clearAnalysisData();
        }
    }

    @Override
    public void expandPathway(Path path, Pathway pathway) {
        expandEvent(path, pathway);
    }

    @Override
    public void expandCellLineagePath(Path path, CellLineagePath cellLineagePath) {
        expandEvent(path, cellLineagePath);
    }

    @Override
    public Set<Pathway> getLoadedPathways() {
        Set<Pathway> pathways = new HashSet<>();
        if(this.hierarchyTree == null) return pathways;
        for (HierarchyItem item : this.hierarchyTree.getHierarchyItems()) {
            if(item.getEvent() instanceof Pathway){
                pathways.add((Pathway) item.getEvent());
            }
        }
        return pathways;
    }

    @Override
    public Set<CellLineagePath> getLoadedCellLineagePaths() {
        Set<CellLineagePath> cellLineagePaths = new HashSet<>();
        if (this.hierarchyTree == null) return cellLineagePaths;
        for (HierarchyItem item : this.hierarchyTree.getHierarchyItems()) {
            if (item.getEvent() instanceof CellLineagePath) {
                cellLineagePaths.add((CellLineagePath) item.getEvent());
            }
        }
        return cellLineagePaths;
    }

    @Override
    public Set<Pathway> getPathwaysWithLoadedReactions() {
        Set<Pathway> pathways = new HashSet<>();
        if(hierarchyTree==null) return pathways;
        return hierarchyTree.getHierarchyPathwaysWithReactionsLoaded();
    }

    @Override
    public Set<CellLineagePath> getCellLineagePathsWithLoadedReactions() {
        Set<CellLineagePath> cellLineagePaths = new HashSet<>();
        if (hierarchyTree == null) return cellLineagePaths;
        return hierarchyTree.getHierarchyCellLineagePathsWithReactionsLoaded();
    }


    @Override
    public void highlightHitReactions(Set<Long> reactionsHit) {
        if(hierarchyTree!=null){
            hierarchyTree.highlightHitReactions(reactionsHit);
        }
    }

    @Override
    public void setPresenter(Hierarchy.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void show(Species species) {
        try {
            this.hierarchyTree = this.hierarchyContainer.getHierarchyTree(species);
            this.hierarchyContainer.showHierarchyTree(species);
            this.presenter.hierarchyChanged(species);
        } catch (HierarchyTreeSpeciesNotFoundException e) {
            this.hierarchyContainer.showLoadingPanel(species);
            this.presenter.retrieveData(species);
        }
    }

    @Override
    public void select(Event event, Path path) {
        this.hierarchyTree.clearHighlights();
        if (event != null) {
            HierarchyItem item = this.hierarchyTree.getHierarchyItemByDatabaseObject(path, event);
            if (item != null) {
                this.hierarchyTree.ensureItemVisible(item);
                item.setSelected(true);
                item.highlightPath();
            } else {
                Console.error("Cannot find " + event);
            }
        }
    }


    @Override
    public void setData(Species species, List<Event> tlps) {
        this.hierarchyTree = new HierarchyTree(species);
        this.hierarchyTree.getElement().getStyle().setProperty("borderRadius","0 15px 0 0");
        this.hierarchyTree.setScrollOnSelectEnabled(true);
        this.hierarchyTree.setAnimationEnabled(true);
        this.hierarchyTree.addHierarchyItemDoubleClickedHandler(this);
        this.hierarchyTree.addHierarchyItemMouseOverHandler(this);
        this.hierarchyTree.addHierarchyItemMouseOutHandler(this);
        this.hierarchyTree.addOpenHandler(this);
        this.hierarchyTree.addSelectionHandler(this);
        this.hierarchyContainer.addHierarchyTree(species, this.hierarchyTree);

        try {
            this.hierarchyTree.loadPathwayChildren(null, tlps);
            this.presenter.hierarchyChanged(species);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void expandEvent(Path path, Event event) {
        if (this.hierarchyTree == null) return;
        HierarchyItem treeItem = this.hierarchyTree.getHierarchyItemByDatabaseObject(path, event);
        if (treeItem != null) {
            treeItem.setState(true, false);
            this.onOpen(treeItem);
        } else {
            Console.error(getClass() + " could not find the node for " + event, this);
        }
    }

    private void onOpen(final HierarchyItem item) {
        if (item.getEvent() instanceof Pathway) {
            onOpen(item, (Pathway) item.getEvent());
        } else if (item.getEvent() instanceof CellLineagePath) {
            onOpen(item, (CellLineagePath) item.getEvent());
        }

        item.setState(true, false);
    }

    private void onOpen(final HierarchyItem item, Pathway pathway) {
        if (!item.isChildrenLoaded()) {
            pathway.load(new DatabaseObjectLoadedHandler() {
                @Override
                public void onDatabaseObjectLoaded(DatabaseObject databaseObject) {
                    Pathway pathway = databaseObject.cast();
                    try {
                        hierarchyTree.loadPathwayChildren(item, pathway.getHasEvent());
                        presenter.eventExpanded(pathway);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onDatabaseObjectError(Throwable trThrowable) {
                    Console.error("Error loading " + pathway, HierarchyDisplay.this);
                }
            });
        } else {
            this.presenter.eventExpanded(pathway);
        }
    }

    private void onOpen(final HierarchyItem item, CellLineagePath cellLineagePath) {
        if (!item.isChildrenLoaded()) {
            cellLineagePath.load(new DatabaseObjectLoadedHandler() {
                @Override
                public void onDatabaseObjectLoaded(DatabaseObject databaseObject) {
                    CellLineagePath cellLineagePath = databaseObject.cast();
                    try {
                        hierarchyTree.loadPathwayChildren(item, cellLineagePath.getHasEvent());
                        presenter.eventExpanded(cellLineagePath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onDatabaseObjectError(Throwable trThrowable) {
                    Console.error("Error loading " + cellLineagePath, HierarchyDisplay.this);
                }
            });
        } else {
            this.presenter.eventExpanded(cellLineagePath);
        }
    }

    public static Resources RESOURCES;

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

        @Source("images/hierarchy.png")
        ImageResource hierarchyIcon();

        @Source("images/EHLDPathway.png")
        ImageResource ehldPathway();
    }

    /**
     * Styles used by this widget.
     */
    @CssResource.ImportedWithPrefix("pwp-Hierarchy")
    public interface ResourceCSS extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "org/reactome/web/pwp/client/hierarchy/Hierarchy.css";

        String hierarchyPanelTitle();

        String hierarchyContainer();

        String hierarchyPanel();
    }
}
