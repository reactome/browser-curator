package org.reactome.web.elv.client.center.content.fireworks.view;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Label;
import org.reactome.web.elv.client.common.analysis.helper.AnalysisHelper;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.fireworks.client.FireworksFactory;
import org.reactome.web.fireworks.client.FireworksViewer;
import org.reactome.web.fireworks.events.NodeHoverEvent;
import org.reactome.web.fireworks.events.NodeOpenedEvent;
import org.reactome.web.fireworks.events.NodeSelectedEvent;
import org.reactome.web.fireworks.handlers.*;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class FireworksViewImpl extends DockLayoutPanel implements FireworksView, AnalysisHelper.ResourceChosenHandler,
        AnalysisResetHandler, NodeHoverHandler, NodeSelectedHandler, NodeSelectedResetHandler, NodeHoverResetHandler,
        NodeOpenedHandler {
    private Presenter presenter;
    private FireworksViewer fireworks;
    private List<HandlerRegistration> handlers;

    private String token;
    private String resource;

    public FireworksViewImpl() {
        super(Style.Unit.PX);
        this.add(new Label("Loading..."));
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
        if(this.token!=null) {
            this.fireworks.setAnalysisToken(token, resource);
        }
        this.clear();
        this.add(this.fireworks);
    }

    @Override
    public void highlightPathway(Pathway pathway) {
        if(pathway==null) return;
        this.fireworks.highlightNode(pathway.getDbId());
    }

    @Override
    public void resetHighlight() {
        this.fireworks.resetHighlight();
    }

    @Override
    public void openPathway(Pathway pathway) {
        this.fireworks.openPathway(pathway.getDbId());
    }

    @Override
    public void setAnalysisToken(final String token) {
        this.token = token;
        AnalysisHelper.chooseResource(token, this);
    }

    @Override
    public void setAnalysisResource(String resource) {
        fireworks.setAnalysisToken(this.token, resource);
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
        this.fireworks.selectNode(pathway.getDbId());
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
}
