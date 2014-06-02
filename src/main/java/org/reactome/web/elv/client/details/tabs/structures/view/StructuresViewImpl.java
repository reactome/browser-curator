package org.reactome.web.elv.client.details.tabs.structures.view;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.elv.client.common.data.model.*;
import org.reactome.web.elv.client.details.model.DetailsTabType;
import org.reactome.web.elv.client.details.tabs.structures.events.StructureLoadedEvent;
import org.reactome.web.elv.client.details.tabs.structures.handlers.StructureLoadedHandler;
import org.reactome.web.elv.client.details.tabs.structures.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class StructuresViewImpl implements StructuresView, StructureLoadedHandler {
    private final DetailsTabType TYPE = DetailsTabType.STRUCTURES;
    Presenter presenter;

    private DockLayoutPanel tab;
    private HTMLPanel title;

    private Map<Long, StructuresPanel> panelsLoaded = new HashMap<Long, StructuresPanel>();
    private StructuresPanel currentPanel;

    public StructuresViewImpl() {
        this.title = new HTMLPanel(this.TYPE.getTitle());
        this.tab = new DockLayoutPanel(Style.Unit.PX);
    }

    @Override
    public Widget asWidget() {
        return tab;
    }

    @Override
    public DetailsTabType getDetailTabType() {
        return TYPE;
    }

    @Override
    public HTMLPanel getTitle() {
        return title;
    }

    @Override
    public void onStructureLoaded(StructureLoadedEvent structureLoadedEvent) {
        StructuresPanel panel = (StructuresPanel) structureLoadedEvent.getSource();
        if(this.currentPanel==panel){
            this.refreshTitle(panel.getNumberOfLoadedStructures(), panel.getNumberOfRequiredStructures());
        }
    }

    @Override
    public void refreshTitle(Integer loadedStructures, Integer proteinAccessions){
        String aux;
        if(loadedStructures==0 && proteinAccessions==0){
            aux = " (0)";
        }else{
            aux = " (" + loadedStructures + "/" + proteinAccessions + ")";
        }
        this.title.getElement().setInnerHTML(TYPE.getTitle() + aux);
    }

    @Override
    public void setInitialState() {
        this.currentPanel = null;
        this.title.getElement().setInnerHTML(TYPE.getTitle());
        this.tab.clear();
        this.tab.add(new HTMLPanel("Structures..."));
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setProteinAccessions(List<ReferenceSequence> referenceSequenceList, Long respId) {
        StructuresPanel panel = this.panelsLoaded.get(respId);
        if(panel instanceof PdbStructurePanel){
            PdbStructurePanel aux = (PdbStructurePanel) panel;
            if(referenceSequenceList.isEmpty()){
                aux.setEmpty();
                return;
            }
            for (ReferenceSequence referenceSequence : referenceSequenceList) {
                aux.add(referenceSequence);
            }
        }
    }

    @Override
    public void showInstanceDetails(Pathway pathway, DatabaseObject databaseObject) {
        if(!this.showInstanceDetailsIfExists(pathway, databaseObject)){
            DatabaseObject toShow = databaseObject!=null?databaseObject:pathway;
            if(toShow==null) return;
            if(toShow instanceof SimpleEntity){
                ChEBIStructuresPanel p = new ChEBIStructuresPanel();
                this.currentPanel = p;
                p.addStructureLoadedHandler(this);
                p.add((SimpleEntity) toShow);
            }else if(toShow instanceof PhysicalEntity){
                PdbStructurePanel p = new PdbStructurePanel();
                this.currentPanel = p;
                p.addStructureLoadedHandler(this);
                this.presenter.getProteinAccessions((PhysicalEntity) toShow, toShow.getDbId());
            } else if(toShow instanceof Event) {
                RheaStructuresPanel p = new RheaStructuresPanel();
                this.currentPanel = p;
                p.addStructureLoadedHandler(this);
                p.add((Event) toShow);
            } else {
                this.currentPanel = new EmptyStructuresPanel();
            }
            this.showStructuresPanel(this.currentPanel);
            this.panelsLoaded.put(toShow.getDbId(), this.currentPanel);
        }
    }

    @Override
    public boolean showInstanceDetailsIfExists(Pathway pathway, DatabaseObject databaseObject) {
        DatabaseObject toShow = databaseObject!=null?databaseObject:pathway;
        if(this.panelsLoaded.containsKey(toShow.getDbId())){
            this.currentPanel = this.panelsLoaded.get(toShow.getDbId());
            this.showStructuresPanel(this.currentPanel);
            this.tab.clear();
            this.tab.add(currentPanel);
            this.refreshTitle(this.currentPanel.getNumberOfLoadedStructures(), this.currentPanel.getNumberOfRequiredStructures());
            return true;
        }else {
            this.setInitialState();
            return false;
        }
    }

    private void showStructuresPanel(StructuresPanel structuresPanel){
        this.tab.clear();
        this.tab.add(structuresPanel);
        this.refreshTitle(structuresPanel.getNumberOfLoadedStructures(), structuresPanel.getNumberOfRequiredStructures());
    }
}
