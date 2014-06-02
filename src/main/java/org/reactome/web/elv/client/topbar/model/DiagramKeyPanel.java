package org.reactome.web.elv.client.topbar.model;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.elv.client.common.ReactomeImages;
import org.reactome.web.elv.client.topbar.view.TopBarView.Presenter;

/**
 * Contains the elements shown in the diagram key panel
 *
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DiagramKeyPanel extends HorizontalPanel {

    private ToggleButton legend;

    private class DiagramKeySummary extends HorizontalPanel{

        private DiagramKeySummary(String label, ImageResource icon) {
            super();

            Label labelKey = new Label(label);
            labelKey.getElement().getStyle().setMarginRight(5, Style.Unit.PX);
            add(labelKey);

            Image imageKey = new Image(icon);
            imageKey.getElement().getStyle().setMarginRight(10, Style.Unit.PX);
            add(imageKey);

            getElement().getStyle().setMarginTop(3, Style.Unit.PX);
        }
    }

    public DiagramKeyPanel(final Presenter presenter) {
        super();
        setSpacing(5);

        add(new DiagramKeySummary("Protein", ReactomeImages.INSTANCE.proteinKey()));
        add(new DiagramKeySummary("Small molecule", ReactomeImages.INSTANCE.smallMoleculeKey()));
        add(new DiagramKeySummary("Complex", ReactomeImages.INSTANCE.complexKey()));

        this.legend = new ToggleButton(new Image(ReactomeImages.INSTANCE.downArrow()));
        this.legend.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            public void onValueChange(ValueChangeEvent<Boolean> booleanValueChangeEvent) {
                if (!legend.getValue()) {
                    legend.setTitle("Show diagram key");
                    legend.getUpFace().setImage(new Image(ReactomeImages.INSTANCE.downArrow()));
                } else {
                    legend.setTitle("Hide diagram key");
                    legend.getUpFace().setImage(new Image(ReactomeImages.INSTANCE.upArrow()));
                }

                presenter.diagramKeyButtonToggled(legend);
            }
        });
        this.legend.setTitle("Show diagram key");
        this.legend.ensureDebugId("cwCustomButton-toggle-normal");

        add(this.legend);
    }

    public void setButtonPressed(String btn, boolean pressed) {
        String name = btn.toLowerCase();
        if(name.equals("diagramkey")){
            legend.setValue(pressed, true);
        }
    }
}
