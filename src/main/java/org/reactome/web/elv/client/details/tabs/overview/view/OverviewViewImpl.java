package org.reactome.web.elv.client.details.tabs.overview.view;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.elv.client.common.ReactomeImages;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.details.model.DetailsTabType;
import org.reactome.web.elv.client.details.tabs.overview.model.OverviewPanel;

import java.util.HashMap;
import java.util.Map;

public class OverviewViewImpl implements OverviewView {
    private final DetailsTabType TYPE = DetailsTabType.OVERVIEW;
    Presenter presenter;

    private DockLayoutPanel tab;
    private HTMLPanel title;

    private Map<DatabaseObject, OverviewPanel> overviewPanelLoaded = new HashMap<DatabaseObject, OverviewPanel>();
    private OverviewPanel currentPanel;

    public OverviewViewImpl() {
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

    public HTMLPanel getTitle() {
        return this.title;
    }

    @Override
    public void setInitialState() {
        this.title.getElement().setInnerHTML(TYPE.getTitle());
        this.tab.clear();
        this.tab.add(new HTMLPanel("Will display context-sensitive, general information for the item you've clicked " +
                "in the diagram above or the Pathway hierarchy on the left-hand side."));
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showInstanceDetails(Pathway pathway, DatabaseObject databaseObject) {
        if(!this.showInstanceDetailsIfExists(pathway, databaseObject)){
            showWaitingMessage();
            this.presenter.getOverviewData();
        }
    }

    @Override
    public boolean showInstanceDetailsIfExists(Pathway pathway, DatabaseObject databaseObject) {
        DatabaseObject toShow = databaseObject!=null?databaseObject:pathway;
        if(this.overviewPanelLoaded.containsKey(toShow)){
            this.currentPanel = this.overviewPanelLoaded.get(toShow);
            showOverviewPanel(this.currentPanel);
            return true;
        }else{
            this.setInitialState();
            return false;
        }
    }

    @Override
    public void setOverviewData(DatabaseObject databaseObject) {
        this.currentPanel = new OverviewPanel(databaseObject);
        this.overviewPanelLoaded.put(databaseObject, this.currentPanel);
        showOverviewPanel(this.currentPanel);
    }

    private void showWaitingMessage(){
        this.currentPanel = null;

        HorizontalPanel message = new HorizontalPanel();
        Image loader = new Image(ReactomeImages.INSTANCE.loader());
        message.add(loader);

        Label label = new Label("Loading overview data, please wait...");
        label.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        message.add(label);

        this.tab.clear();
        this.tab.add(message);
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

    private void showOverviewPanel(OverviewPanel panel){
        //title.getElement().setInnerHTML(TYPE.getTitle() + " (" + ?? +")");
        this.tab.clear();
        this.tab.add(panel);
    }
}
