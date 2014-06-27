package org.reactome.web.elv.client.details.tabs.molecules.model;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.elv.client.common.ReactomeImages;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Event;
import org.reactome.web.elv.client.common.data.model.PhysicalEntity;
import org.reactome.web.elv.client.common.data.model.Species;
import org.reactome.web.elv.client.common.provider.InstanceTypeExplanation;
import org.reactome.web.elv.client.common.provider.InstanceTypeIconProvider;
import org.reactome.web.elv.client.details.tabs.molecules.model.data.Result;
import org.reactome.web.elv.client.details.tabs.molecules.model.type.PropertyType;
import org.reactome.web.elv.client.details.tabs.molecules.model.widget.MoleculesDownloadPanel;
import org.reactome.web.elv.client.details.tabs.molecules.model.widget.MoleculesViewPanel;
import org.reactome.web.elv.client.popups.help.HelpPopupImage;

import java.util.List;

/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
public class MoleculesPanel extends DockLayoutPanel {
    Result result;

    final ToggleButton button = new ToggleButton("Download", "Molecules View");

    DockLayoutPanel swapPanel;
    MoleculesViewPanel view;
    MoleculesDownloadPanel downloads;

    public MoleculesPanel(final Result result, DatabaseObject databaseObject) {
        super(Style.Unit.PX);
        addStyleName("elv-Details-Tab");

        this.result = result;
        this.swapPanel = new DockLayoutPanel(Style.Unit.PX);
        this.view = new MoleculesViewPanel(result);
        this.downloads = new MoleculesDownloadPanel(result);

        //Creating TopBar with a ToggleButton for switching between Molecule and Download View.
        HorizontalPanel topBar = new HorizontalPanel();
        HorizontalPanel infoBar = new HorizontalPanel();
        infoBar.add(getTitle(databaseObject));
        infoBar.add(getSpecies(databaseObject));
        infoBar.add(getInfo());
        topBar.add(infoBar);

        button.setTitle("Go to Download-View");
        button.setDown(false);
        button.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                swapPanel.removeFromParent();
                if (button.isDown()) {
                    downloads.initialise(result);
                    swapPanel = downloads;
                    button.setTitle("Go back to Molecules-View");
                } else {
                    view.update(result);
                    swapPanel = view;
                    button.setTitle("Go to Download-View");
                }
                add(swapPanel);
            }
        });
        button.setStyleName("elv-Molecules-Button");
        topBar.add(button);
        this.addNorth(topBar, 35);
        topBar.getElement().getStyle().setWidth(99, Style.Unit.PCT);
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
        HorizontalPanel infoPanel = new HorizontalPanel();
        infoPanel.setStyleName("elv-Molecules-InfoPanel");
        try{
            ImageResource img = ReactomeImages.INSTANCE.information();
            String helpTitle = "Info";
            HTMLPanel helpContent = new HTMLPanel(
                    "You are now in the Molecules tab which shows you all the molecules of a pathway that has a diagram.\n" +
                    "The molecules will be grouped in Chemical Compounds, Proteins, Sequences and Others.\n" +
                    "If you select an element that is part of a pathway those molecules will be highlighted.\n" +
                    "For each molecule you can see a symbol, a link to the main reference DB, a name and the number of\n" +
                    "occurrences in the pathway.\n" +
                    "Expanding by clicking on the '+' will provide you with further external links.\n" +
                    "In addition to these lists there is a Download available. Just click on the button in the top right\n" +
                    "corner, select the fields and types you are interested in and click 'Start Download'.");
            infoPanel.add(new HelpPopupImage(img, helpTitle, helpContent));
        }catch (Exception e){
            e.printStackTrace();
        }
        HTMLPanel title = new HTMLPanel("Info");
        title.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        infoPanel.add(title);

        return infoPanel;
    }

    public Integer getNumberOfLoadedMolecules() {
        if(result == null){
            return 0;
        }
        return result.getNumberOfMolecules();
    }

    public Integer getNumberOfHighlightedMolecules() {
        int numOfHighlightedMolecules = 0;
        numOfHighlightedMolecules += this.result.getNumHighlight(PropertyType.OTHERS);
        numOfHighlightedMolecules += this.result.getNumHighlight(PropertyType.SEQUENCES);
        numOfHighlightedMolecules += this.result.getNumHighlight(PropertyType.PROTEINS);
        numOfHighlightedMolecules += this.result.getNumHighlight(PropertyType.CHEMICAL_COMPOUNDS);

        return numOfHighlightedMolecules;
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

    
}