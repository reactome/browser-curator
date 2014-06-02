package org.reactome.web.elv.client.details.tabs.molecules.view;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.elv.client.common.ReactomeImages;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.details.model.DetailsTabType;
import org.reactome.web.elv.client.details.tabs.molecules.model.MoleculesPanel;
import org.reactome.web.elv.client.details.tabs.molecules.model.PMDownload;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class MoleculeViewImpl implements MoleculesView{
    private final DetailsTabType TYPE = DetailsTabType.PARTICIPATING_MOLECULES;
    Presenter presenter;

    private DockLayoutPanel tab;
    private HTMLPanel title;

    private Map<DatabaseObject, MoleculesPanel> moleculesPanelLoaded = new HashMap<DatabaseObject, MoleculesPanel>();
    private MoleculesPanel currentPanel;

    public MoleculeViewImpl() {
        title = new HTMLPanel(TYPE.getTitle());
        tab = new DockLayoutPanel(Style.Unit.EM);
//        setInstancesInitialState();
    }

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
    public void setInitialState() {
        title.getElement().setInnerHTML(TYPE.getTitle());
        tab.clear();
        tab.add(new HTMLPanel("Displays all molecule types involved in the selected event. These can be either " +
                "proteins, chemical compounds or other types. You can either view the list or download molecule " +
                "types with various fields and in various formats."));
    }

    @Override
    public void setParticipatingMolecules(DatabaseObject databaseObject, JSONObject json) {
        if(json.get("success").isBoolean().booleanValue()){
            currentPanel = new MoleculesPanel(json);
            currentPanel.getDownload().addViewHandler(new PMDownload.ViewHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    PMDownload d = currentPanel.getDownload();
                    presenter.getFormatedData(d.getSelectedTypes(), d.getSelectedFields(), d.getFormat());
                }
            });
            currentPanel.getDownload().addDownloadHandler(new PMDownload.DownloadHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    PMDownload d = currentPanel.getDownload();
                    presenter.downloadFormatedData(d.getSelectedTypes(), d.getSelectedFields(), d.getFormat());
                }
            });
            moleculesPanelLoaded.put(databaseObject, currentPanel );
            showMoleculesPanel(currentPanel);
        }else{
            showErrorMessage(json.get("errorMessage").isString().stringValue());
        }
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showExportResults(String results) {
        currentPanel.getDownload().showData(results);
    }

    private void showErrorMessage(String message){
        HorizontalPanel panel = new HorizontalPanel();
        Image loader = new Image(ReactomeImages.INSTANCE.exclamation());
        panel.add(loader);

        Label label = new Label(message);
        label.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        panel.add(label);

        tab.clear();
        tab.add(panel);
    }

    @Override
    public void showInstanceDetails(Pathway pathway, DatabaseObject databaseObject) {
        if(!showInstanceDetailsIfExists(pathway,databaseObject)){
            showWaitingMessage();
            presenter.getParticipatingMolecules();
        }
    }

    @Override
    public boolean showInstanceDetailsIfExists(Pathway pathway, DatabaseObject databaseObject) {
        DatabaseObject toShow = databaseObject!=null?databaseObject:pathway;
        if(moleculesPanelLoaded.containsKey(toShow)){
            currentPanel = moleculesPanelLoaded.get(toShow);
            showMoleculesPanel(currentPanel);
            return true;
        }else{
            this.setInitialState();
            return false;
        }
    }

    private void showMoleculesPanel(MoleculesPanel panel){
        title.getElement().setInnerHTML(TYPE.getTitle() + " (" + panel.getMoleculesNumber() +")");
        tab.clear();
        tab.add(panel);
    }

    private void showWaitingMessage(){
        HorizontalPanel message = new HorizontalPanel();
        Image loader = new Image(ReactomeImages.INSTANCE.loader());
        message.add(loader);

        Label label = new Label("Loading participating molecules, please wait...");
        label.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        message.add(label);

        tab.clear();
        tab.add(message);
    }

    @Override
    public void submitForm(String url, JSONObject params) {
        currentPanel.getDownload().submitForm(url, params);
    }
}
