package org.reactome.web.elv.client.hierarchy.model;

import com.google.gwt.user.client.ui.CustomTree;
import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.elv.client.common.analysis.model.PathwaySummary;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Event;
import org.reactome.web.elv.client.common.data.model.ReactionLikeEvent;
import org.reactome.web.elv.client.common.data.model.Species;
import org.reactome.web.elv.client.common.utils.Console;
import org.reactome.web.elv.client.common.utils.MapSet;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class HierarchyTree extends CustomTree {

    private MapSet<Long, HierarchyItem> treeItems;
    private Species species;

    public HierarchyTree(Species species) {
        super();
        this.treeItems = new MapSet<Long, HierarchyItem>();
        this.species = species;
    }

    public void clearAnalysisData(){
        for (int i = 0; i < getItemCount(); i++) {
            HierarchyItem child = (HierarchyItem) getItem(i);
            child.clearAnalysisData();
        }
    }

    public void showAnalysisData(List<PathwaySummary> pathwaySummaries){
        for (PathwaySummary pathwaySummary :pathwaySummaries) {
            for (HierarchyItem hierarchyItem : treeItems.getElements(pathwaySummary.getDbId())) {
                hierarchyItem.showAnalysisData(pathwaySummary);
            }
        }
    }

    public HierarchyItem getHierarchyItemByDatabaseObject(List<Long> path, DatabaseObject databaseObject){
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
        }
        return item;
    }

    public Set<Long> getHierarchyEventIds(){
        return this.treeItems.keySet();
    }

    public Set<Long> getHierarchyPathwaysWithReactions(){
        Set<Long> rtn = new HashSet<Long>();
        for (Long id : this.treeItems.keySet()) {
            for (HierarchyItem item : this.treeItems.getElements(id)) {
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

    public void loadPathwayChildren(List<Long> path, DatabaseObject databaseObject, List<Event> children){
        HierarchyItem item=null;
        if(databaseObject!=null){
            item = getHierarchyItemByDatabaseObject(path, databaseObject);
        }
        if(item!=null){
            item.removeItems();
            item.setChildrenLoaded(true);
        }
        for (Event child : children) {
            HierarchyItem hi = new HierarchyItem(species, child);
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
}