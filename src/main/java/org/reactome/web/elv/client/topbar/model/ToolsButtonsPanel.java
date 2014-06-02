package org.reactome.web.elv.client.topbar.model;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import org.reactome.web.elv.client.common.ReactomeImages;
import org.reactome.web.elv.client.common.widgets.button.CustomButton;
import org.reactome.web.elv.client.topbar.view.TopBarView;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ToolsButtonsPanel extends HorizontalPanel {

    private CustomButton analysisButton;

    public ToolsButtonsPanel(final TopBarView.Presenter presenter) {
        super();
        analysisButton = new CustomButton();
        analysisButton.setTitle("Analyse your data");
        analysisButton.setResourceNoBorder(ReactomeImages.INSTANCE.analysisTool());
        analysisButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                presenter.analysisToolSelected();
            }
        });
        add(analysisButton);
    }
}
