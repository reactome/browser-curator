package org.reactome.web.elv.client.topbar.view;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.elv.client.common.data.model.Species;
import org.reactome.web.elv.client.common.widgets.glass.GlassPanel;
import org.reactome.web.elv.client.topbar.model.*;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class TopBarViewImpl implements TopBarView {

    private Presenter presenter;

	private ToolBarPanel topBar;

    private SpeciesSelectorPanel speciesSelectorPanel;

    private LayoutButtonsPanel buttonsPanel;

    private ToolsButtonsPanel toolsButtonsPanel;

    private DiagramKeyPanel diagramKeyPanel;

    /**
     * The glass element.
     */
    private GlassPanel glass = null;

    public TopBarViewImpl() {
    }

    @Override
    public Widget asWidget() {
        return topBar;
    }

    @Override
    public void allowBannerRedirection(boolean allow) {
        topBar.setGoHomepageAllowed(allow);
    }

    private void init(){
        this.speciesSelectorPanel = new SpeciesSelectorPanel(this.presenter);
        this.buttonsPanel = new LayoutButtonsPanel(this.presenter);
        this.diagramKeyPanel = new DiagramKeyPanel(this.presenter);
        this.toolsButtonsPanel = new ToolsButtonsPanel(this.presenter);
        topBar = new ToolBarPanel(speciesSelectorPanel, buttonsPanel, diagramKeyPanel, toolsButtonsPanel);
    }

    @Override
    public void selectSpecies(Species species) {
        this.speciesSelectorPanel.selectSpecies(species);
    }

    @Override
    public void setButtonPressed(String btn, boolean pressed) {
        this.buttonsPanel.setButtonPressed(btn, pressed);
        this.diagramKeyPanel.setButtonPressed(btn, pressed);
    }

    @Override
    public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
        init();
	}

    @Override
    public void setSpeciesList(List<Species> speciesList) {
        this.speciesSelectorPanel.setSpeciesList(speciesList);
    }

    @Override
    public void tourFadeIn() {
        if(glass==null){
            glass = new GlassPanel(this.topBar);
            Window.addResizeHandler(new ResizeHandler() {
                @Override
                public void onResize(ResizeEvent event) {
                    if(glass.isAttached()){
                        glass.onResize();
                    }
                }
            });
        }
        RootPanel.get().add(glass);
    }

    @Override
    public void tourFadeOut() {
        if(glass!=null){
            RootPanel.get().remove(glass);
        }
    }
}


