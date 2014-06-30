package org.reactome.web.elv.client.topbar.model;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.elv.client.common.LocationHelper;
import org.reactome.web.elv.client.common.ReactomeImages;
import org.reactome.web.elv.client.common.data.model.Species;
import org.reactome.web.elv.client.manager.state.AdvancedState;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ToolBarPanel extends HTMLPanel implements ClickHandler {

    private boolean goHomepageAllowed = true;

    /**
     * A ClientBundle of resources used by this widget.
     */
    public interface Resources extends ClientBundle {
        /**
         * The styles used in this widget.
         */
        @Source(Style.DEFAULT_CSS)
        Style toolBarStyle();
    }

    /**
     * Styles used by this widget.
     */
    @CssResource.ImportedWithPrefix("reactome-ToolBarPanel")
    public interface Style extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String DEFAULT_CSS = "org/reactome/web/elv/client/topbar/model/ToolBarPanel.css";

        /**
         * Applied to the widget.
         */
        String toolBarContainer();

        /**
         * Applied to the
         */
        String toolBarBanner();

        /**
         * Applied to the
         */
        String toolBarSpeciesSelector();

        /**
         * Applied to the
         */
        String toolBarSpeciesSelectorNoHuman();

        /**
         * Applied to the
         */
        String toolBarLayoutButtons();

        /**
         * Applied to the
         */
        String toolBarToolsButtons();

        /**
         * Applied to the
         */
        String toolBarDiagramKey();
    }

    private static Resources DEFAULT_RESOURCES;

    /**
     * Get the default {@link Resources} for this widget.
     */
    private static Resources getDefaultResources() {
        if (DEFAULT_RESOURCES == null) {
            DEFAULT_RESOURCES = GWT.create(Resources.class);
        }
        return DEFAULT_RESOURCES;
    }

    public ToolBarPanel(Widget speciesSelector, Widget layoutButtons, Widget diagramKey, Widget tools) {
        super("");
        // Inject the styles used by this widget.
        Style style = getDefaultResources().toolBarStyle();
        style.ensureInjected();

        //noinspection GWTStyleCheck
        setStyleName("clearfix");

        addStyleName(style.toolBarContainer());

        Image banner = new Image(ReactomeImages.INSTANCE.banner());
        banner.setTitle("Back to homepage");
        banner.setStyleName(style.toolBarBanner());
        banner.addClickHandler(this);
        banner.getElement().getStyle().setCursor(com.google.gwt.dom.client.Style.Cursor.POINTER);
        add(banner);

        if(!LocationHelper.getLocation().equals(LocationHelper.Location.PRODUCTION)){
            Image phase = new Image(ReactomeImages.INSTANCE.beta());
            banner.getElement().getStyle().setMarginLeft(15, com.google.gwt.dom.client.Style.Unit.PX);
            phase.setTitle("Back to homepage");
            phase.setStyleName(style.toolBarBanner());
            phase.addClickHandler(this);
            phase.getElement().getStyle().setCursor(com.google.gwt.dom.client.Style.Cursor.POINTER);
            phase.getElement().getStyle().setPosition(com.google.gwt.dom.client.Style.Position.ABSOLUTE);
            phase.getElement().getStyle().setLeft(0, com.google.gwt.dom.client.Style.Unit.PX);
            phase.getElement().getStyle().setTop(0, com.google.gwt.dom.client.Style.Unit.PX);
            phase.getElement().getStyle().setWidth(50, com.google.gwt.dom.client.Style.Unit.PX);
            phase.getElement().getStyle().setHeight(50, com.google.gwt.dom.client.Style.Unit.PX);
            add(phase);
        }

        speciesSelector.setStyleName(style.toolBarSpeciesSelector());
        add(speciesSelector);

        layoutButtons.setStyleName(style.toolBarLayoutButtons());
        add(layoutButtons);

        diagramKey.setStyleName(style.toolBarDiagramKey());
        add(diagramKey);

        tools.setStyleName(style.toolBarToolsButtons());
        add(tools);
    }

    @Override
    public void onClick(ClickEvent event) {
        if(this.goHomepageAllowed){
            Window.Location.assign("/");
        }else{
            PopupPanel popup = new PopupPanel(true);
            popup.setWidget(new Label("Now you would be returned to the homepage"));
            popup.setPopupPosition(5,5);
            popup.show();
        }
    }

    public void setSpeciesSelectorStyle(SpeciesSelectorPanel speciesSelectorPanel, Species species){
        // Inject the styles used by this widget.
        Style style = getDefaultResources().toolBarStyle();
        style.ensureInjected();

        if(!AdvancedState.DEFAULT_SPECIES_ID.equals(species.getDbId())){
            speciesSelectorPanel.addStyleName(style.toolBarSpeciesSelectorNoHuman());
        }else{
            speciesSelectorPanel.removeStyleName(style.toolBarSpeciesSelectorNoHuman());
        }
    }

    public void setGoHomepageAllowed(boolean goHomepageAllowed) {
        this.goHomepageAllowed = goHomepageAllowed;
    }
}
