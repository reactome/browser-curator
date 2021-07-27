package org.reactome.web.pwp.client.hierarchy.widget;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.CustomTree;
import org.reactome.web.pwp.client.common.model.classes.*;
import org.reactome.web.pwp.client.common.model.util.Path;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.common.utils.MapSet;
import org.reactome.web.pwp.client.hierarchy.events.HierarchyItemDoubleClickedEvent;
import org.reactome.web.pwp.client.hierarchy.events.HierarchyItemMouseOutEvent;
import org.reactome.web.pwp.client.hierarchy.events.HierarchyItemMouseOverEvent;
import org.reactome.web.pwp.client.hierarchy.handlers.HierarchyItemDoubleClickedHandler;
import org.reactome.web.pwp.client.hierarchy.handlers.HierarchyItemMouseOutHandler;
import org.reactome.web.pwp.client.hierarchy.handlers.HierarchyItemMouseOverHandler;

import java.util.*;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class HierarchyTree extends CustomTree implements HierarchyItemDoubleClickedHandler, HierarchyItemMouseOverHandler, HierarchyItemMouseOutHandler {

    private Species species;
    private static MapSet<Long, HierarchyItem> treeItems = new MapSet<>();
    private static List<Long> ehlds = new ArrayList<>();

    public HierarchyTree(Species species) {
        super();
        this.species = species;
    }

    public HandlerRegistration addHierarchyItemDoubleClickedHandler(HierarchyItemDoubleClickedHandler handler){
        return addHandler(handler, HierarchyItemDoubleClickedEvent.TYPE);
    }

    public HandlerRegistration addHierarchyItemMouseOverHandler(HierarchyItemMouseOverHandler handler) {
        return addHandler(handler, HierarchyItemMouseOverEvent.TYPE);
    }

    public HandlerRegistration addHierarchyItemMouseOutHandler(HierarchyItemMouseOutHandler handler) {
        return addHandler(handler, HierarchyItemMouseOutEvent.TYPE);
    }

    public void clearAnalysisData() {
        for (int i = 0; i < getItemCount(); i++) {
            HierarchyItem child = (HierarchyItem) getItem(i);
            child.clearAnalysisData();
        }
    }

    public void clearHighlights() {
        for (HierarchyItem item : getHierarchyItems()) {
            item.clearHighlight();
        }
    }

    public void highlightHitReactions(Set<Long> reactionsHit) {
        for (Long reaction : reactionsHit) {
            Set<HierarchyItem> items = treeItems.getElements(reaction);
            if (items != null) {
                for (HierarchyItem item : items) {
                    item.highlightHitEvent();
                }
            }
        }
    }

    public HierarchyItem getHierarchyItemByDatabaseObject(Path path, Event event) {
        if (event == null) {
            Console.error("Really! Asking me for nothing? (getHierarchyItemByDatabaseObject)", this);
            return null;
        } else {
            // path CAN be null IMPORTANT!
            List<Event> pathAux = path != null ? new LinkedList<>(path.asList()) : new LinkedList<Event>();

            //Only seek for events in the specified path ;)
            if (!pathAux.contains(event)) {
                pathAux.add(event);
            }
            HierarchyItem item = null;
            for (Event e : pathAux) {
                item = this.getChild(item, e.getDbId());
            }

            if (item == null) {
                Console.error(event + " not found", this);
            }
            return item;
        }
    }

    public Set<Long> getHierarchyEventIds() {
        return this.treeItems.keySet();
    }

    public Set<Pathway> getHierarchyPathwaysWithReactionsLoaded(){
        Set<Pathway> rtn = new HashSet<>();
        for (Long eventId : this.treeItems.keySet()) {
            for (HierarchyItem item : this.treeItems.getElements(eventId)) {
                if(item.getEvent() instanceof ReactionLikeEvent){
                    HierarchyItem parent = (HierarchyItem) item.getParentItem();
                    rtn.add((Pathway)parent.getEvent());
                }
            }
        }
        return rtn;
    }

    public Set<CellLineagePath> getHierarchyCellLineagePathsWithReactionsLoaded(){
        Set<CellLineagePath> rtn = new HashSet<>();
        for (Long eventId : this.treeItems.keySet()) {
            for (HierarchyItem item : this.treeItems.getElements(eventId)) {
                if(item.getEvent() instanceof ReactionLikeEvent){
                    HierarchyItem parent = (HierarchyItem) item.getParentItem();
                    if (parent.getEvent() instanceof CellLineagePath) {
                        rtn.add((CellLineagePath) parent.getEvent());
                    }
                }
            }
        }
        return rtn;
    }


    public Set<HierarchyItem> getHierarchyItems() {
        Set<HierarchyItem> rtn = new HashSet<>();
        for (Long id : this.treeItems.keySet()) {
            rtn.addAll(this.treeItems.getElements(id));
        }
        return rtn;
    }

    public void loadPathwayChildren(HierarchyItem item, List<Event> children) throws Exception {
        if (item != null) {
            item.removeItems();
            item.setChildrenLoaded(true);
        }
        for (Event child : children) {
            boolean ehld = ehlds.contains(child.getDbId());
            HierarchyItem hi = new HierarchyItem(species, child, ehld);
            hi.addHierarchyItemDoubleClickedHandler(this);
            hi.addHierarchyItemMouseOverHandler(this);
            hi.addHierarchyItemMouseOutHandler(this);
            this.treeItems.add(child.getDbId(), hi);
            if (item == null) {
                addItem(hi);
            } else {
                item.addItem(hi);
            }
        }
    }

    private HierarchyItem getChild(HierarchyItem node, Long dbId) {
        if (node == null) {
            for (int i = 0; i < this.getItemCount(); i++) {
                HierarchyItem item = (HierarchyItem) this.getItem(i);
                if (item.getEvent().getDbId().equals(dbId)) return item;
            }
            return null;
        } else {
            if (!node.isChildrenLoaded()) return node;
            for (int i = 0; i < node.getChildCount(); i++) {
                HierarchyItem item = (HierarchyItem) node.getChild(i);
                if (item.getEvent().getDbId().equals(dbId)) return item;
            }
            return null;
        }
    }

    @Override
    public void onHierarchyItemDoubleClicked(HierarchyItemDoubleClickedEvent e) {
        fireEvent(e);
    }

    @Override
    public void onHierarchyItemHoveredReset() {
        fireEvent(new HierarchyItemMouseOutEvent());
    }

    @Override
    public void onHierarchyItemMouseOver(HierarchyItemMouseOverEvent e) {
        fireEvent(e);
    }
}