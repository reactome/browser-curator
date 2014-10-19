package org.reactome.web.elv.client.hierarchy.model;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.CustomTree;
import org.reactome.web.elv.client.common.analysis.model.PathwaySummary;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Event;
import org.reactome.web.elv.client.common.data.model.ReactionLikeEvent;
import org.reactome.web.elv.client.common.data.model.Species;
import org.reactome.web.elv.client.common.utils.Console;
import org.reactome.web.elv.client.common.utils.MapSet;
import org.reactome.web.elv.client.hierarchy.events.HierarchyItemMouseOutEvent;
import org.reactome.web.elv.client.hierarchy.events.HierarchyItemMouseOverEvent;
import org.reactome.web.elv.client.hierarchy.handlers.HierarchyItemMouseOutHandler;
import org.reactome.web.elv.client.hierarchy.handlers.HierarchyItemMouseOverHandler;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class HierarchyTree extends CustomTree implements HierarchyItemMouseOverHandler, HierarchyItemMouseOutHandler {

    private MapSet<Long, HierarchyItem> treeItems;
    private Species species;

    public HierarchyTree(Species species) {
        super();
        this.treeItems = new MapSet<Long, HierarchyItem>();
        this.species = species;
    }


    public HandlerRegistration addHierarchyItemMouseOverHandler(HierarchyItemMouseOverHandler handler){
        return addHandler(handler, HierarchyItemMouseOverEvent.TYPE);
    }

    public HandlerRegistration addHierarchyItemMouseOutHandler(HierarchyItemMouseOutHandler handler){
        return addHandler(handler, HierarchyItemMouseOutEvent.TYPE);
    }

    public void clearAnalysisData(){
        for (int i = 0; i < getItemCount(); i++) {
            HierarchyItem child = (HierarchyItem) getItem(i);
            child.clearAnalysisData();
        }
    }

    public void highlightHitReactions(Set<Long> reactionsHit){
        for (Long reaction : reactionsHit) {
            Set<HierarchyItem> items = treeItems.getElements(reaction);
            if(items!=null){
                for (HierarchyItem item : items) {
                    item.highlightHitEvent();
                }
            }
        }
    }

    public void showAnalysisData(List<PathwaySummary> pathwaySummaries){
        for (PathwaySummary pathwaySummary :pathwaySummaries) {
            Set<HierarchyItem> items = treeItems.getElements(pathwaySummary.getDbId());
            if(items!=null){
                for (HierarchyItem item : items) {
                    item.showAnalysisData(pathwaySummary);
                }
            }
        }
    }

    public HierarchyItem getHierarchyItemByDatabaseObject(List<Long> path, DatabaseObject databaseObject) throws Exception{
        // path CAN be null IMPORTANT!
        if(path==null){
            path = new LinkedList<Long>();
        }
        //Only seek for events in the hierarchy ;)
        if(databaseObject!=null && databaseObject instanceof Event){
            if(!path.contains(databaseObject.getDbId())){
                path.add(databaseObject.getDbId());
            }
        }
        HierarchyItem item = null;
        for (Long dbId : path) {
            item = this.getChild(item, dbId);
        }
        if(item==null){
            Console.error(getClass() + " -> " + databaseObject + " not found in " + path);
            throw new NullPointerException(getClass() + " could not find " + databaseObject + " in " + path);
        }
        return item;
    }

    public Set<Long> getHierarchyEventIds(){
        return this.treeItems.keySet();
    }

    public Set<Long> getHierarchyPathwaysWithReactionsLoaded(){
        Set<Long> rtn = new HashSet<Long>();
        for (Long eventId : this.treeItems.keySet()) {
            for (HierarchyItem item : this.treeItems.getElements(eventId)) {
                if(item.getEvent() instanceof ReactionLikeEvent){
                    rtn.add(((HierarchyItem) item.getParentItem()).getEvent().getDbId());
                }
            }
        }
        return rtn;
    }

    public Set<HierarchyItem> getHierarchyItems(){
        Set<HierarchyItem> rtn = new HashSet<HierarchyItem>();
        for (Long id : this.treeItems.keySet()) {
            rtn.addAll(this.treeItems.getElements(id));
        }
        return rtn;
    }

    public void loadPathwayChildren(List<Long> path, DatabaseObject databaseObject, List<Event> children) throws Exception{
        HierarchyItem item = null;
        if(databaseObject != null){
            item = getHierarchyItemByDatabaseObject(path, databaseObject);
        }
        if(item != null){
            item.removeItems();
            item.setChildrenLoaded(true);
        }
        for (Event child : children) {
            HierarchyItem hi = new HierarchyItem(species, child);
            hi.addHierarchyItemMouseOverHandler(this);
            hi.addHierarchyItemMouseOutHandler(this);
            this.treeItems.add(child.getDbId(), hi);
            if(item==null){
                addItem(hi);
            }else{
                item.addItem(hi);
            }
        }
    }

    private HierarchyItem getChild(HierarchyItem node, Long dbId){
        if(node==null){
            for (int i = 0; i < this.getItemCount(); i++) {
                HierarchyItem item = (HierarchyItem) this.getItem(i);
                if(item.getEvent().getDbId().equals(dbId)) return item;
            }
            return null;
        }else{
            if(!node.isChildrenLoaded()) return node;
            for (int i = 0; i < node.getChildCount(); i++) {
                HierarchyItem item = (HierarchyItem) node.getChild(i);
                if(item.getEvent().getDbId().equals(dbId)) return item;
            }
            return null;
        }
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