package org.reactome.web.elv.client.topbar.model;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import org.reactome.web.elv.client.common.LocationHelper;
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
        if(LocationHelper.isAnalysisAvailable()) {
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
        }else{
            Image img = new Image(ReactomeImages.INSTANCE.analysisToolsDisabled());
            img.getElement().getStyle().setWidth(28, Style.Unit.PX);
            img.getElement().getStyle().setHeight(28, Style.Unit.PX);
            img.setTitle("Analysis tool not available in this server");
            add(img);
        }
    }
}
