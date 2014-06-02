package org.reactome.web.elv.client.hierarchy.model;

import com.google.gwt.user.client.ui.*;
import org.reactome.web.elv.client.common.data.model.Species;
import org.reactome.web.elv.client.hierarchy.events.HierarchyTreeSpeciesNotFoundException;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains the loaded events hierarchies.
 * Keeps the state of every tree and improves the web application response time avoiding unnecessary
 *
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class HierarchyContainerPanel extends DeckPanel {
    private int index = 0;

    private TreeLoadingPanel treeLoadingPanel;

    //keep track of the trees indexes in the DeckPanel (because a ScrollPanel wraps them)
    private Map<HierarchyTree, Integer> hierarchyTreeIndex = new HashMap<HierarchyTree, Integer>();
    private Map<Species, HierarchyTree> speciesHierarchyTreeMap = new HashMap<Species, HierarchyTree>();

    public HierarchyContainerPanel() {
        setStyleName("elv-Hierarchy-Content");

        this.treeLoadingPanel = new TreeLoadingPanel();
        add(this.treeLoadingPanel);
        showWidget(index);
    }

    public void addHierarchyTree(Species species, HierarchyTree hierarchyTree){
        int index;
        if(isHierarchyTreeLoaded(species)){
            HierarchyTree aux = this.speciesHierarchyTreeMap.get(species);
            index = this.hierarchyTreeIndex.get(aux);
            //if are NOT the same -> remove the old one and insert the new in the same position
            if(aux!=hierarchyTree){
                remove(index);
                insert(hierarchyTree, index);
            }
        }else{
            index = ++this.index;
            add(new ScrollPanel(hierarchyTree));
        }
        this.hierarchyTreeIndex.put(hierarchyTree, index);
        this.speciesHierarchyTreeMap.put(species, hierarchyTree);
        showWidget(index);
    }

    public boolean isHierarchyTreeLoaded(Species species){
        return this.speciesHierarchyTreeMap.containsKey(species);
    }

    public HierarchyTree getHierarchyTree(Species species) throws HierarchyTreeSpeciesNotFoundException {
        if(this.speciesHierarchyTreeMap.containsKey(species)){
            return this.speciesHierarchyTreeMap.get(species);
        }else{
            throw new HierarchyTreeSpeciesNotFoundException();
        }
    }

    public void showHierarchyTree(Species species) throws HierarchyTreeSpeciesNotFoundException {
        HierarchyTree ht = getHierarchyTree(species);
        int index = this.hierarchyTreeIndex.get(ht);
        showWidget(index);
    }

    public void showLoadingPanel(Species species){
        this.treeLoadingPanel.showLoadingInfo(species);
        this.getWidgetIndex(this.treeLoadingPanel);
        this.showWidget(0);
    }
}
