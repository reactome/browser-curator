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
     * Refreshing the title of MoleculesTab if number of loaded/highlighted molecules has changed.
     * @param highlightedMolecules Number of highlighted molecules.
     * @param loadedMolecules Number of loaded molecules.
     */
    @Override
    public void refreshTitle(Integer highlightedMolecules, Integer loadedMolecules){
        String aux = null;
        if(loadedMolecules == null){
            aux = "";
        }else if(loadedMolecules==0){
            aux = " (0)";
        }else if(highlightedMolecules > 0){
            if(highlightedMolecules.equals(loadedMolecules)){
                aux = " (" + loadedMolecules + ")";
            }else{
                aux = " (" + highlightedMolecules + "/" + loadedMolecules + ")";
            }
        }

        if(aux != null){
            this.title.getElement().setInnerHTML(TYPE.getTitle() + aux);
        }
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
        toShow = databaseObject != null ? databaseObject : pathway;
        pathwayDiagram = pathway;

        boolean exists = this.showInstanceDetailsIfExists((Pathway) pathwayDiagram, toShow);//needs to be called first

        if (!exists){
            showWaitingMessage();
            if(toShow==null) return;
            this.presenter.getMoleculesData();
        }else{
            this.presenter.updateMoleculesData();
        }
    }

    @Override
    public boolean showInstanceDetailsIfExists(Pathway pathway, DatabaseObject databaseObject) {
        toShow = databaseObject != null ? databaseObject : pathway;
        pathwayDiagram = pathway;

        presenter.getMoleculeNumbers(pathwayDiagram, toShow);

        if (this.getPathwayDetailsIfExist(pathway)) {
//            presenter.getMoleculeNumbers(pathwayDiagram, toShow);
            return true;
        } else {
//            this.refreshTitle(null, null);
            return false;
        }
//        return this.getPathwayDetailsIfExist(pathway);
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
        this.currentPanel = new MoleculesPanel(result, this.toShow, this.presenter);
        showMoleculesPanel(this.currentPanel);
    }

    @Override
    public void updateMoleculesData(Result result) {
        this.currentPanel.update(result);
        showMoleculesPanel(currentPanel);
    }

    private void showMoleculesPanel(MoleculesPanel panel) {
        this.tab.clear(); //in case a different result is currently shown
        this.tab.add(panel);

        this.refreshTitle(panel.getNumberOfHighlightedMolecules(), panel.getNumberOfLoadedMolecules());

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

}
