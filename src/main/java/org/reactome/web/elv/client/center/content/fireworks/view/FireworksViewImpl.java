package org.reactome.web.elv.client.center.content.fireworks.view;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.elv.client.common.analysis.helper.AnalysisHelper;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.common.widgets.disclosure.DisclosureImages;
import org.reactome.web.fireworks.client.FireworksFactory;
import org.reactome.web.fireworks.client.FireworksViewer;
import org.reactome.web.fireworks.events.NodeHoverEvent;
import org.reactome.web.fireworks.events.NodeOpenedEvent;
import org.reactome.web.fireworks.events.NodeSelectedEvent;
import org.reactome.web.fireworks.events.ProfileChangedEvent;
import org.reactome.web.fireworks.handlers.*;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class FireworksViewImpl extends DockLayoutPanel implements FireworksView, AnalysisHelper.ResourceChosenHandler,
        AnalysisResetHandler, NodeHoverHandler, NodeSelectedHandler, NodeSelectedResetHandler, NodeHoverResetHandler,
        NodeOpenedHandler, ProfileChangedHandler {
    private Presenter presenter;
    private FireworksViewer fireworks;
    private List<HandlerRegistration> handlers;

    private String token;
    private String resource;

    private Pathway toHighlight;
    private Pathway toSelect;
    private Pathway toOpen;

    public FireworksViewImpl() {
        super(Style.Unit.PX);
        this.add(getLoadingMessage());
        this.handlers = new LinkedList<HandlerRegistration>();
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void loadSpeciesFireworks(String speciesJson) {
        this.removeHandlers(); //Needed to allow the garbage collection to get rid of previous instances of fireworks
        this.fireworks = FireworksFactory.createFireworksViewer(speciesJson);
        handlers.add(this.fireworks.addAnalysisResetHandler(this));
        handlers.add(this.fireworks.addNodeHoverHandler(this));
        handlers.add(this.fireworks.addNodeOpenedHandler(this));
        handlers.add(this.fireworks.addNodeSelectedHandler(this));
        handlers.add(this.fireworks.addNodeSelectedResetHandler(this));
        handlers.add(this.fireworks.addNodeHoverResetHandler(this));
        handlers.add(this.fireworks.addProfileChangedHandler(this));
        this.clear();
        this.add(this.fireworks);
        //We need the fireworks to be rendered before applying the carried actions
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                applyCarriedActions();
            }
        });
    }

    private void applyCarriedActions(){
        //ORDER IS IMPORTANT IN THE FOLLOWING CONDITIONS
        if(this.toOpen!=null){
            this.fireworks.openPathway(this.toOpen.getDbId());
            this.toOpen = null;
            this.toSelect = null;
            this.toHighlight = null;
        }
        if(this.toSelect!=null){
            this.fireworks.selectNode(this.toSelect.getDbId());
            this.toSelect = null;
        }
        if(this.toHighlight!=null){
            this.fireworks.highlightNode(this.toHighlight.getDbId());
            this.toHighlight = null;
        }
        if(this.token!=null) {
            this.fireworks.setAnalysisToken(token, resource);
        }
    }

    @Override
    public void highlightPathway(Pathway pathway) {
        if(pathway==null) return;
        if(this.fireworks!=null) {
            this.fireworks.highlightNode(pathway.getDbId());
        }
        this.toHighlight = pathway;
    }

    @Override
    public void resetHighlight() {
        if(this.fireworks!=null) {
            this.fireworks.resetHighlight();
        }
        this.toHighlight = null;
    }

    @Override
    public void openPathway(Pathway pathway) {
        if(this.fireworks!=null) {
            this.fireworks.openPathway(pathway.getDbId());
        }
        this.toOpen = pathway;
    }

    @Override
    public void setAnalysisToken(final String token) {
        this.token = token;
        AnalysisHelper.chooseResource(token, this);
    }

    @Override
    public void setAnalysisResource(String resource) {
        if(this.fireworks!=null) {
            fireworks.setAnalysisToken(this.token, resource);
        }
        this.resource = resource;
    }

    @Override
    public void resetAnalysisToken() {
        if(this.token!=null) {
            this.token = null;
            fireworks.resetAnalysis();
        }
    }

    @Override
    public void selectPathway(Pathway pathway) {
        if(pathway==null) return;
        if(this.fireworks!=null) {
            this.fireworks.selectNode(pathway.getDbId());
        }
        this.toSelect = pathway;
    }

    /**
     * Gets rid of current possible existing handlers to the fireworks object, so the garbage collector will
     * clean the memory and this class wont be listening to previous deleted instances of Fireworks
     */
    private void removeHandlers(){
        for (HandlerRegistration handler : handlers) {
            handler.removeHandler();
        }
        handlers.clear();
    }

    @Override
    public void resetSelection() {
        this.fireworks.resetSelection();
    }

    @Override
    public void resetView() {
        this.removeHandlers(); //Needed to allow the garbage collection to get rid of previous instances of fireworks
        this.fireworks = null;
        this.clear();
        this.add(getLoadingMessage());
    }

    @Override
    public void onAnalysisReset() {
        this.presenter.resetAnalysis();
    }

    @Override
    public void onNodeHover(NodeHoverEvent event) {
        this.presenter.highlightPathway(event.getNode().getDbId());
    }

    @Override
    public void onNodeOpened(NodeOpenedEvent event) {
        this.presenter.showPathwayDiagram(event.getNode().getDbId());
    }

    @Override
    public void onNodeSelected(NodeSelectedEvent event) {
        this.presenter.selectPathway(event.getNode().getDbId());
    }

    @Override
    public void onNodeSelectionReset() {
        this.presenter.resetPathwaySelection();
    }

    @Override
    public void onNodeHoverReset() {
        this.presenter.resetPathwayHighlighting();
    }

    @Override
    public void onProfileChanged(ProfileChangedEvent event) {
        this.presenter.profileChanged(event.getProfile().getName());
    }

    @Override
    public void onResourceChosen(String resource) {
        if(this.token!=null) {
            this.resource = resource;
            this.fireworks.setAnalysisToken(token, resource);
        }
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if(visible && this.fireworks!=null) {
            this.fireworks.onResize();
        }
    }

    /**
     * Getting a panel with loading message and symbol.
     * @return Widget
     */
    private Widget getLoadingMessage(){
        HorizontalPanel hp = new HorizontalPanel();
        hp.add(new Image(DisclosureImages.INSTANCE.getLoadingImage()));
        hp.add(new HTMLPanel("Loading pathways overview graph..."));
        hp.setSpacing(5);

        return hp;
    }
}
