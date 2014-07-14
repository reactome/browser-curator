package org.reactome.web.elv.client.details.tabs.downloads.model;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import org.reactome.web.elv.client.common.ReactomeImages;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.common.widgets.button.CustomButton;
import org.reactome.web.elv.client.details.tabs.downloads.view.DownloadsView;

/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
public class DownloadMolecule extends CustomButton {

    public DownloadMolecule(final DownloadsView.Presenter presenter, final Pathway pathway){
        super();
        this.addStyleName("elv-Download-Item");
        this.setWidth("100px");
        this.setText("Molecules");
        this.setResource(ReactomeImages.INSTANCE.download());
        this.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                //swap to Molecules download
                presenter.swapToMolecules(pathway);
            }
        });
        this.setTitle("View/download participating molecules.");
    }
}
