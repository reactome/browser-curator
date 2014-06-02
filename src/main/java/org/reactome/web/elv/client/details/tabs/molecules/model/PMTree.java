package org.reactome.web.elv.client.details.tabs.molecules.model;

import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class PMTree extends Tree implements OpenHandler<TreeItem> {

    public PMTree(JSONObject json) {
        JSONArray participatingMolecules = json.get("participatingMolecules").isArray();
        for(int i=0; i<participatingMolecules.size(); i++){
            JSONObject mType = participatingMolecules.get(i).isObject();
            addItem(new PMItem(mType, PMItem.LEVEL.MOLECULE_TYPE));
        }

        addOpenHandler(this);
    }

    @Override
    public void onOpen(OpenEvent<TreeItem> treeItemOpenEvent) {
        final PMItem item = (PMItem) treeItemOpenEvent.getTarget();

        //Timer used because the loading icon doesn't appear by default
        Timer t = new Timer() {
            @Override
            public void run() {
                item.expand();
            }
        };
        t.schedule(100);
    }
}
