package org.reactome.web.elv.client.details.tabs.molecules.model;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import org.reactome.web.elv.client.details.tabs.molecules.model.data.Result;
import org.reactome.web.elv.client.details.tabs.molecules.model.widget.MoleculesDownloadPanel;
import org.reactome.web.elv.client.details.tabs.molecules.model.widget.MoleculesViewPanel;

/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
public class MoleculesPanel extends DockLayoutPanel {
    Result result;

    final ToggleButton button = new ToggleButton("Download", "Molecules View");

    DockLayoutPanel swapPanel;
    MoleculesViewPanel view;
    MoleculesDownloadPanel downloads;

    public MoleculesPanel(final Result result) {
        super(Style.Unit.PX);
        addStyleName("elv-Details-Tab");

        this.result = result;
        this.swapPanel = new DockLayoutPanel(Style.Unit.PX);
        this.view = new MoleculesViewPanel(result);
        this.downloads = new MoleculesDownloadPanel(result);

        //Creating TopBar with a ToggleButton for switching between Molecule and Download View.
        HorizontalPanel topBar = new HorizontalPanel();
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
        this.swapPanel = this.view;
        this.add(swapPanel);
    }

    public Integer getNumberOfLoadedMolecules() {
        if(result == null){
            return 0;
        }
        return result.getNumberOfMolecules();
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