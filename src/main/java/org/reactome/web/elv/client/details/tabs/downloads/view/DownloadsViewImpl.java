package org.reactome.web.elv.client.details.tabs.downloads.view;

import com.google.gwt.dom.client.Style;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.elv.client.common.ReactomeImages;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.common.provider.InstanceTypeExplanation;
import org.reactome.web.elv.client.common.provider.InstanceTypeIconProvider;
import org.reactome.web.elv.client.details.model.DetailsTabType;
import org.reactome.web.elv.client.details.tabs.downloads.model.DownloadPanel;
import org.reactome.web.elv.client.details.tabs.downloads.model.DownloadType;
import org.reactome.web.elv.client.popups.help.HelpPopupImage;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DownloadsViewImpl implements DownloadsView {

    private final DetailsTabType TYPE = DetailsTabType.DOWNLOADS;
    Presenter presenter;

    private String dbName = null;
    private DockLayoutPanel tab;
    private HTMLPanel title;

    public DownloadsViewImpl() {
        title = new HTMLPanel(TYPE.getTitle());
        tab = new DockLayoutPanel(Style.Unit.PX);
        tab.addStyleName("elv-Download-Tab");
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
    public void setInitialState() {
        tab.clear();
        tab.add(new HTMLPanel("Allows the download of chosen event(s) in a number of different formats, ranging from " +
                "biological xml-type formats to simple Word and PDF formats."));
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showInstanceDetails(final Pathway pathway, final DatabaseObject databaseObject) {
        this.showWaitingMessage();

        //To continue ahead the dbName is needed. If it is null, the presenter should still
        //be trying to retrieve that from the db
        if(this.dbName==null){
            (new Timer() {
                @Override
                public void run() {
                    showInstanceDetailsIfExists(pathway, databaseObject);
                }
            }).schedule(250);
            return;
        }

        DockLayoutPanel aux = new DockLayoutPanel(Style.Unit.PX);

        VerticalPanel vp = new VerticalPanel();
        vp.add(getTitle(pathway));
        vp.add(getExplanation());
        aux.addNorth(vp, 78);

        FlowPanel flowPanel = new FlowPanel();
        for (DownloadType downloadType : DownloadType.values()) {
            DownloadPanel dp = new DownloadPanel(this.dbName, downloadType, pathway);
            dp.getElement().getStyle().setMarginLeft(30, Style.Unit.PX);
            dp.getElement().getStyle().setMarginRight(30, Style.Unit.PX);
            dp.getElement().getStyle().setMarginTop(15, Style.Unit.PX);
            dp.getElement().getStyle().setMarginBottom(15, Style.Unit.PX);
            flowPanel.add(dp);
        }
        ScrollPanel sp = new ScrollPanel(flowPanel);
        sp.setStyleName("elv-Download-ItemsPanel");
        aux.add(sp);

        tab.clear();
        tab.add(aux);
    }

    @Override
    public boolean showInstanceDetailsIfExists(Pathway pathway, DatabaseObject databaseObject) {
        this.setInitialState();
        return false;
    }

    private Widget getTitle(DatabaseObject databaseObject){
        HorizontalPanel titlePanel = new HorizontalPanel();
        titlePanel.setStyleName("elv-Download-Title");
        try{
            ImageResource img = InstanceTypeIconProvider.getItemIcon(databaseObject.getSchemaClass());
            String helpTitle = databaseObject.getSchemaClass().name;
            HTMLPanel helpContent = new HTMLPanel(InstanceTypeExplanation.getExplanation(databaseObject.getSchemaClass()));
            titlePanel.add(new HelpPopupImage(img, helpTitle, helpContent));
        }catch (Exception e){
            e.printStackTrace();
        }
        HTMLPanel title = new HTMLPanel(databaseObject.getDisplayName());
        title.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        titlePanel.add(title);

        return titlePanel;
    }

    private Widget getExplanation(){
        HTMLPanel explanation = new HTMLPanel("The download options below are for the selected pathway, " +
                "not individual events or entities selected in it.");
        explanation.setStyleName("elv-Download-Explanation");
        return explanation;
    }

    private void showWaitingMessage(){
        HorizontalPanel message = new HorizontalPanel();
        Image loader = new Image(ReactomeImages.INSTANCE.loader());
        message.add(loader);

        Label label = new Label("Loading download panel, please wait...");
        label.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        message.add(label);

        tab.clear();
        tab.add(message);
    }

    @SuppressWarnings("UnusedDeclaration")
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
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}
