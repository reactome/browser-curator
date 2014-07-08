package org.reactome.web.elv.client.details.tabs.molecules.model;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.elv.client.common.ReactomeImages;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Event;
import org.reactome.web.elv.client.common.data.model.PhysicalEntity;
import org.reactome.web.elv.client.common.data.model.Species;
import org.reactome.web.elv.client.common.provider.InstanceTypeExplanation;
import org.reactome.web.elv.client.common.provider.InstanceTypeIconProvider;
import org.reactome.web.elv.client.common.widgets.button.CustomButton;
import org.reactome.web.elv.client.details.tabs.molecules.model.data.Result;
import org.reactome.web.elv.client.details.tabs.molecules.model.widget.MoleculesDownloadPanel;
import org.reactome.web.elv.client.details.tabs.molecules.model.widget.MoleculesViewPanel;
import org.reactome.web.elv.client.details.tabs.molecules.view.MoleculesView;
import org.reactome.web.elv.client.popups.help.HelpPopup;
import org.reactome.web.elv.client.popups.help.HelpPopupImage;

import java.util.List;

/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
public class MoleculesPanel extends DockLayoutPanel implements MouseOverHandler, MouseOutHandler {
    Result result;

//    final ToggleButton button = new ToggleButton("Download", "Molecules View");
    final CustomButton downloadBtn = new CustomButton(ReactomeImages.INSTANCE.downloadFile(), "Download");
    final CustomButton moleculeBtn = new CustomButton(ReactomeImages.INSTANCE.back(), "Molecule View");

    FocusPanel infoPanel;
    HelpPopup popup;
    DockLayoutPanel swapPanel;
    MoleculesViewPanel view;
    MoleculesDownloadPanel downloads;

    public MoleculesPanel(final Result result, DatabaseObject databaseObject, MoleculesView.Presenter presenter) {
        super(Style.Unit.PX);
        //noinspection GWTStyleCheck
        setStyleName("clearfix");
        addStyleName("elv-Details-Tab");

        this.result = result;
        this.swapPanel = new DockLayoutPanel(Style.Unit.PX);
        this.view = new MoleculesViewPanel(result);
        this.downloads = new MoleculesDownloadPanel(result, presenter);

        //Creating TopBar with a ToggleButton for switching between Molecule and Download View.
        HorizontalPanel topBar = new HorizontalPanel();
//        HorizontalPanel infoBar = new HorizontalPanel();
        topBar.add(getTitle(databaseObject));
        topBar.add(getSpecies(databaseObject));
        topBar.add(getInfo());
//        topBar.add(infoBar);

        final HorizontalPanel buttonBar = new HorizontalPanel();
        topBar.add(buttonBar);

        downloadBtn.setTitle("Go to Download-View");
        moleculeBtn.setTitle("Go back to Molecules-View");

        downloadBtn.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                swapPanel.removeFromParent();
                downloads.initialise(result);
                swapPanel = downloads;

                add(swapPanel);
                buttonBar.clear();
                buttonBar.add(moleculeBtn);
            }
        });


        moleculeBtn.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                swapPanel.removeFromParent();
                view.update(result);
                swapPanel = view;

                add(swapPanel);
                buttonBar.clear();
                buttonBar.add(downloadBtn);
            }
        });
        downloadBtn.setStyleName("elv-Molecules-Button");
        moleculeBtn.setStyleName("elv-Molecules-Button");
        buttonBar.add(downloadBtn);
        buttonBar.setStyleName("elv-Molecules-ButtonBar");

        topBar.add(buttonBar);
        buttonBar.getElement().getStyle().setFloat(Style.Float.LEFT);
//        buttonBar.getElement().getStyle().setPaddingTop(1, Style.Unit.PX);
//        buttonBar.getElement().getStyle().setMarginTop(1, Style.Unit.PX);

        this.addNorth(topBar, 35);
//        topBar.setStyleName("elv-Molecules-TopBar");
        this.swapPanel = this.view;
        this.add(swapPanel);
    }

    private Widget getTitle(DatabaseObject databaseObject) {
        HorizontalPanel titlePanel = new HorizontalPanel();
        titlePanel.setStyleName("elv-Details-Title");
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

    private Widget getSpecies(DatabaseObject databaseObject){
        String species = null;
        if(databaseObject instanceof PhysicalEntity){
            List<Species> speciesList = ((PhysicalEntity) databaseObject).getSpecies();
            if(!speciesList.isEmpty()){
                species = speciesList.get(0).getDisplayName();
            }
        }else if(databaseObject instanceof Event){
            Event event = (Event) databaseObject;
            if(!event.getSpecies().isEmpty()){
                species = event.getSpecies().get(0).getDisplayName();
            }
        }

        HorizontalPanel speciesPanel = new HorizontalPanel();
        if(species!=null){
            speciesPanel.setStyleName("elv-Details-Species");
            speciesPanel.add(new HTMLPanel("Species: " + species));
        }
        return speciesPanel;
    }

    private Widget getInfo() {
        infoPanel = new FocusPanel();
        infoPanel.setStyleName("elv-Molecules-InfoPanel");
        HorizontalPanel content = new HorizontalPanel();
        try{
            Image img = new Image(ReactomeImages.INSTANCE.information());
            String helpTitle = "Info";
            HTMLPanel helpContent = new HTMLPanel(
                    "The molecules tab shows you all the molecules of a complete pathway diagram.\n" +
                    "Molecules are grouped in Chemical Compounds, Proteins, Sequences and Others.\n" +
                    "The molecules of a selected object appear highlighted in the molecules lists;\n" +
                    "a molecule selected in the list will be highlighted in the diagram.\n" +
                    "For each molecule you can see a symbol, a link to the main reference DB, a name and the number of\n" +
                    "occurrences in the pathway. Clicking on the symbol several times will allow you to circle through\n" +
                    "all its occurrences in the diagram.\n" +
                    "Expanding by clicking on the '+' will provide you with further external links.\n" +
                    "Lists can be downloaded. Just click on the button in the top right\n" +
                    "corner, select the fields and types you are interested in and click 'Start Download'.");

            content.add(img);
            popup = new HelpPopup(helpTitle, helpContent);
            infoPanel.addMouseOverHandler(this);
            infoPanel.addMouseOutHandler(this);
            infoPanel.getElement().getStyle().setProperty("cursor", "help");
        }catch (Exception e){
            e.printStackTrace();
        }
        HTMLPanel title = new HTMLPanel("Info");
        title.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        content.add(title);

        infoPanel.add(content.asWidget());

        return infoPanel;
    }

    public Integer getNumberOfLoadedMolecules() {
        if(result == null){
            return 0;
        }
        return result.getNumberOfMolecules();
    }

    public Integer getNumberOfHighlightedMolecules() {
        return result.getNumberOfHighlightedMolecules();
    }

    //Avoids loading if Pathway-with-Diagram stays the same.
    public void update(Result result) {
        this.result = result;
        this.view.update(result);
        this.downloads.update(result);
    }

    public MoleculesDownloadPanel getDownload() {
        return downloads;
    }


    @Override
    public void onMouseOver(MouseOverEvent event) {
        popup.setPositionAndShow(event);
    }

    @Override
    public void onMouseOut(MouseOutEvent event) {
        popup.hide(true);
    }
}