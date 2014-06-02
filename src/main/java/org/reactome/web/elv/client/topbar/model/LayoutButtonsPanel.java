package org.reactome.web.elv.client.topbar.model;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ToggleButton;
import org.reactome.web.elv.client.common.ReactomeImages;
import org.reactome.web.elv.client.topbar.view.TopBarView;

/**
 * Contains the buttons related to the layout selection
 *
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class LayoutButtonsPanel extends HorizontalPanel {

    private ToggleButton detailsBtn;
    private ToggleButton hierarchyBtn;
    private ToggleButton diagramBtn;

    public LayoutButtonsPanel(final TopBarView.Presenter presenter) {
        super();

        hierarchyBtn = new ToggleButton(new Image(ReactomeImages.INSTANCE.hierarchy()));
        hierarchyBtn.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> booleanValueChangeEvent) {
                if(!hierarchyBtn.getValue()){
                    hierarchyBtn.setTitle("Show hierarchy panel");
                    if(!detailsBtn.getValue()){
                        diagramBtn.setValue(true, false);
                    }
                }else{
                    diagramBtn.setTitle("Expand diagram");
                    diagramBtn.setValue(false, false);
                    hierarchyBtn.setTitle("Hide hierarchy panel");
                }

                presenter.hierarchyButtonToggled(hierarchyBtn);
            }
        });
        hierarchyBtn.setTitle("Hide hierarchy panel");
        hierarchyBtn.setDown(true);
        hierarchyBtn.ensureDebugId("cwCustomButton-toggle-normal");

        detailsBtn = new ToggleButton(new Image(ReactomeImages.INSTANCE.details()));
        detailsBtn.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> booleanValueChangeEvent) {
                if(!detailsBtn.getValue()){
                    detailsBtn.setTitle("Show details panel");
                    if(!hierarchyBtn.getValue()){
                        diagramBtn.setValue(true, false);
                    }
                }else{
                    diagramBtn.setTitle("Expand diagram");
                    diagramBtn.setValue(false, false);
                    detailsBtn.setTitle("Hide details panel");
                }

                presenter.detailsButtonToggled(detailsBtn);
            }
        });

        detailsBtn.setTitle("Hide details panel");
        detailsBtn.setDown(true);
        detailsBtn.ensureDebugId("cwCustomButton-toggle-normal");

        diagramBtn = new ToggleButton(new Image(ReactomeImages.INSTANCE.diagram()));
        diagramBtn.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> booleanValueChangeEvent) {
                if(!diagramBtn.getValue()){
                    diagramBtn.setTitle("Expand diagram");
                    detailsBtn.setValue(true, true);
                    hierarchyBtn.setValue(true, true);
                }else{
                    diagramBtn.setTitle("Display hierarchy and details panel");
                    detailsBtn.setValue(false, true);
                    hierarchyBtn.setValue(false, true);
                }

                presenter.detailsButtonToggled(hierarchyBtn);
            }
        });
        diagramBtn.setTitle("Expand diagram");
        diagramBtn.ensureDebugId("cwCustomButton-push-normal");

        add(hierarchyBtn);
        add(detailsBtn);
        add(diagramBtn);

    }

    public void setButtonPressed(String btn, boolean pressed) {
        String name = btn.toLowerCase();
        if(name.equals("details")){
            detailsBtn.setValue(pressed, true);
        }else if(name.equals("hierarchy")){
            hierarchyBtn.setValue(pressed, true);
        }
    }
}
