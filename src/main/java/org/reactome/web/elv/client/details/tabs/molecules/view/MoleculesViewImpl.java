package org.reactome.web.elv.client.details.tabs.molecules.view;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.elv.client.common.ReactomeImages;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.details.model.DetailsTabType;
import org.reactome.web.elv.client.details.tabs.molecules.model.MoleculesPanel;
import org.reactome.web.elv.client.details.tabs.molecules.model.data.Result;
import org.reactome.web.elv.client.details.tabs.molecules.presenter.LRUCache;

/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
public class MoleculesViewImpl implements MoleculesView/*, MoleculesLoadedHandler */{
    //private static final String PREFIX = "\t\t[MoleculesView] -> ";
    private final DetailsTabType TYPE = DetailsTabType.PARTICIPATING_MOLECULES;
    MoleculesView.Presenter presenter;

    private HTMLPanel title;
    private DockLayoutPanel tab;

    //private Map<Long, MoleculesPanel> panelsLoaded = new HashMap<Long, MoleculesPanel>();
    //private Map<Long, MoleculesPanel> panelsLoadedForPathways = new HashMap<Long, MoleculesPanel>();
    //Aren't Caches better? As they store only the least recently used data?
    private class IdPair{
        private Long pathwayId;
        private Long toShowId;

        private IdPair(Long pathwayId, Long toShowId) {
            this.pathwayId = pathwayId;
            this.toShowId = toShowId;
        }
    }
    private LRUCache<IdPair, MoleculesPanel> panelsLoaded = new LRUCache<IdPair, MoleculesPanel>();
    private LRUCache<Long, MoleculesPanel> panelsLoadedForPathways = new LRUCache<Long, MoleculesPanel>();

    private MoleculesPanel currentPanel;
    private DatabaseObject toShow;
    private DatabaseObject pathwayDiagram;

    public MoleculesViewImpl() {
        this.title = new HTMLPanel(TYPE.getTitle());
        this.tab = new DockLayoutPanel(Style.Unit.EM);

        setInitialState();
    }

    @Override
    public Widget asWidget() {
        return this.tab;
    }

    @Override
    public DetailsTabType getDetailTabType() {
        return TYPE;
    }

    @Override
    public HTMLPanel getTitle() {
        return this.title;
    }


    /**
     * Refreshing the title of MoleculesTab if number of loaded molecules has changed.
     * @param loadedMolecules Number of loaded molecules.
     */
    @Override
    public void refreshTitle(Integer loadedMolecules){
        String aux;
        if(loadedMolecules == null){
            aux = "";
        }else if(loadedMolecules==0){
            aux = " (0)";
        }else{
            aux = " (" + loadedMolecules + ")";
        }
        this.title.getElement().setInnerHTML(TYPE.getTitle() + aux);
    }

    @Override
    public void setInitialState() {
        this.currentPanel = null;
        this.title.getElement().setInnerHTML(TYPE.getTitle());
        this.tab.clear();

        this.tab.add(new HTMLPanel("Displays all molecule types involved in the selected event. These can either be " +
                "proteins, chemical compounds or other types. You can either view the list or download molecule types " +
                "with various fields and in various formats."));
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showInstanceDetails(Pathway pathway, DatabaseObject databaseObject) {
        boolean exists = this.showInstanceDetailsIfExists(pathway, databaseObject);//needs to be called first!
        this.pathwayDiagram = pathway;
        if(this.getPathwayDetailsIfExist(pathway)){
            this.presenter.updateMoleculesData();
        }else if(!exists){
            this.setInitialState();
            showWaitingMessage();
            toShow = databaseObject!=null?databaseObject:pathway;
            if(toShow==null) return;
            //this.currentPanel = new MoleculesPanel(null);
            //currentPanel.addMoleculesLoadedHandler(this);
            this.presenter.getMoleculesData();
//            this.showMoleculesPanel(this.currentPanel);
        }

        if(currentPanel != null){
            this.refreshTitle(this.currentPanel.getNumberOfLoadedMolecules());
        }
        //Needed for Subpathways:
        this.panelsLoaded.put(new IdPair(toShow.getDbId(), pathwayDiagram.getDbId()), this.currentPanel);
        this.panelsLoadedForPathways.put(pathway.getDbId(), this.currentPanel);
    }

    @Override
    public boolean showInstanceDetailsIfExists(Pathway pathway, DatabaseObject databaseObject) {
//        if(toShow != null && toShow.getDbId() != null && currentPanel != null){
//            this.panelsLoaded.put(toShow.getDbId(), currentPanel);
//        }
        this.pathwayDiagram = pathway;
        toShow = databaseObject != null ? databaseObject : pathway;
        IdPair dbIds = new IdPair(pathwayDiagram.getDbId(), toShow.getDbId());

        if (this.panelsLoaded.containsKey(dbIds)) {
            this.currentPanel = this.panelsLoaded.get(dbIds);
            showMoleculesPanel(this.currentPanel);
            this.tab.clear();
            this.tab.add(currentPanel);
            this.refreshTitle(this.currentPanel.getNumberOfLoadedMolecules());
            return true;
        } else if(panelsLoadedForPathways.containsKey(pathwayDiagram.getDbId())) {
            this.currentPanel = this.panelsLoadedForPathways.get(pathwayDiagram.getDbId());
            this.refreshTitle(this.currentPanel.getNumberOfLoadedMolecules());
            return false;
        } else {
            this.refreshTitle(null);
            return false;
        }
    }

    private boolean getPathwayDetailsIfExist(Pathway pathway){
        if(this.panelsLoadedForPathways.containsKey(pathway.getDbId())){
            this.currentPanel = this.panelsLoadedForPathways.get(pathway.getDbId());
            return true;
        }
        return false;
    }

    @Override
    public void setMoleculesData(Result result) {
        this.currentPanel = new MoleculesPanel(result, this.toShow);
        //this.panelsLoaded.put(result, this.currentPanel);
        showMoleculesPanel(this.currentPanel);
    }

    @Override
    public void updateMoleculesData(Result result) {
        this.currentPanel.update(result);
        this.tab.clear(); //in case a different result is currently shown
        this.tab.add(currentPanel);

        this.panelsLoaded.put(new IdPair(pathwayDiagram.getDbId(), toShow.getDbId()), this.currentPanel);
        this.panelsLoadedForPathways.put(this.pathwayDiagram.getDbId(), this.currentPanel);
    }

    @SuppressWarnings("UnusedDeclaration")
    private void showErrorMessage(String message){
        HorizontalPanel panel = new HorizontalPanel();
        Image loader = new Image(ReactomeImages.INSTANCE.exclamation());
        panel.add(loader);

        Label label = new Label(message);
        label.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        panel.add(label);

        this.tab.clear();
        this.tab.add(panel);
    }

    private void showWaitingMessage(){
        this.currentPanel = null;

        HorizontalPanel message = new HorizontalPanel();
        Image loader = new Image(ReactomeImages.INSTANCE.loader());
        message.add(loader);

        Label label = new Label("Loading data for participating molecules, please wait...");
        label.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        message.add(label);

        this.tab.clear();
        this.tab.add(message);
    }

    private void showMoleculesPanel(MoleculesPanel panel) {
        this.tab.clear();
        this.tab.add(panel);
        this.refreshTitle(panel.getNumberOfLoadedMolecules());

        this.panelsLoaded.put(new IdPair(pathwayDiagram.getDbId(), toShow.getDbId()), this.currentPanel);
        this.panelsLoadedForPathways.put(this.pathwayDiagram.getDbId(), this.currentPanel);
    }

}
