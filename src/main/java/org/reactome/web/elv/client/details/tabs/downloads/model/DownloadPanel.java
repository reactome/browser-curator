package org.reactome.web.elv.client.details.tabs.downloads.model;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import org.reactome.web.elv.client.common.ReactomeImages;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.widgets.button.CustomButton;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DownloadPanel extends CustomButton {

    public DownloadPanel(final String dbName, final DownloadType type, final DatabaseObject databaseObject){
        super();
        this.addStyleName("elv-Download-Item");
        this.setWidth("100px");
        this.setText(type.getName());
        this.setResource(ReactomeImages.INSTANCE.download());
        this.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                Window.open(type.getUrl(dbName, databaseObject.getDbId()), "_blank", "");
            }
        });
        this.setTitle("View/download in " + type.getTooltip() + " format");
    }

}
