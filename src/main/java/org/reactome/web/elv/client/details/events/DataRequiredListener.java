package org.reactome.web.elv.client.details.events;

import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Event;
import org.reactome.web.elv.client.common.data.model.LiteratureReference;
import org.reactome.web.elv.client.common.model.Ancestors;
import org.reactome.web.elv.client.details.model.widgets.DetailsPanel;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DataRequiredListener {

    private static DataRequiredListener dataRequiredListener;

    private DataRequiredHandler handler;

    //The list of DetailsPanel is because there are events with different ID that contain the same summation ID
    //and when they are in the same overview, we need to keep both target panels when the data comes back
    private Map<Long, List<DetailsPanel>> dataPanelMap = new HashMap<Long, List<DetailsPanel>>();
    private Map<Long, List<DetailsPanel>> refPanelMap = new HashMap<Long, List<DetailsPanel>>();
    private Map<Long, List<DetailsPanel>> pathPanelMap = new HashMap<Long, List<DetailsPanel>>();

    protected DataRequiredListener() {}

    public static DataRequiredListener getDataRequiredListener() {
        if(dataRequiredListener == null)
            dataRequiredListener = new DataRequiredListener();
        return dataRequiredListener;
    }

    private void addDetailsPanel(Map<Long, List<DetailsPanel>> map, DatabaseObject databaseObject, DetailsPanel panel){
        List<DetailsPanel> list;
        if(map.containsKey(databaseObject.getDbId())){
            list = map.get(databaseObject.getDbId());
        }else{
            list = new LinkedList<DetailsPanel>();
            map.put(databaseObject.getDbId(), list);
        }
        list.add(panel);
    }

    private DetailsPanel getDetailsPanel(Map<Long, List<DetailsPanel>> map, Long dbId){
        if(map.containsKey(dbId)){
            List<DetailsPanel> list = map.get(dbId);
            DetailsPanel panel = list.remove(0);
            if(list.isEmpty()){
                map.remove(dbId);
            }
            return panel;
        }else{
            return null;
        }
    }

    public void onDataRequired(DetailsPanel panel, DatabaseObject databaseObject) {
        this.addDetailsPanel(this.dataPanelMap, databaseObject, panel);
        handler.onDataRequired(databaseObject);
    }

    public void onPathRequired(DetailsPanel panel, Event event){
        this.addDetailsPanel(this.pathPanelMap, event, panel);
        handler.onPathRequired(event);
    }

    public void onReferencesRequired(DetailsPanel panel, DatabaseObject databaseObject){
        this.addDetailsPanel(this.refPanelMap, databaseObject, panel);
        handler.onReferencesRequired(databaseObject.getDbId());
    }

    public void setDataRequiredHandler(DataRequiredHandler handler) {
        this.handler = handler;
    }

    public void setRequiredAncestors(Long dbId, Ancestors ancestors){
        DetailsPanel panel = this.getDetailsPanel(this.pathPanelMap, dbId);
        if(panel!=null){
            panel.setReceivedAncestors(ancestors);
        }
    }

    public void setRequiredData(Long dbId, DatabaseObject databaseObject){
        DetailsPanel panel = this.getDetailsPanel(this.dataPanelMap, dbId);
        if(panel!=null){
            panel.setReceivedData(databaseObject);
        }
    }

    public void setRequiredReferences(Long dbId, List<LiteratureReference> literatureReferences){
        DetailsPanel panel = this.getDetailsPanel(this.refPanelMap, dbId);
        if(panel!=null){
            panel.setReceivedReferences(literatureReferences);
        }
    }
}
