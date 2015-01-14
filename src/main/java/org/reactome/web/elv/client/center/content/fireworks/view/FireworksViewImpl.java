package org.reactome.web.elv.client.center.content.fireworks.view;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.fireworks.client.FireworksFactory;
import org.reactome.web.fireworks.client.FireworksViewer;
import org.reactome.web.fireworks.events.NodeHoverEvent;
import org.reactome.web.fireworks.events.NodeSelectedEvent;
import org.reactome.web.fireworks.handlers.NodeHoverEventHandler;
import org.reactome.web.fireworks.handlers.NodeHoverResetEventHandler;
import org.reactome.web.fireworks.handlers.NodeSelectedEventHandler;
import org.reactome.web.fireworks.handlers.NodeSelectedResetEventHandler;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class FireworksViewImpl implements FireworksView, NodeHoverEventHandler, NodeSelectedEventHandler, NodeSelectedResetEventHandler, NodeHoverResetEventHandler {
    private Presenter presenter;
    private FireworksViewer fireworks;
    private ResizeLayoutPanel container;
    private List<HandlerRegistration> handlers;

    public FireworksViewImpl() {
        this.container = new ResizeLayoutPanel();
        this.container.add(new Label("Loading..."));
        this.handlers = new LinkedList<HandlerRegistration>();
    }

    @Override
    public Widget asWidget() {
        return this.container;
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void loadSpeciesFireworks(String speciesJson) {
        this.removeHandlers(); //Needed to allow the garbage collection to get rid of previous instances of fireworks
        this.fireworks = FireworksFactory.createFireworksViewer(speciesJson);
        handlers.add(this.fireworks.addNodeHoverEventHandler(this));
        handlers.add(this.fireworks.addNodeSelectedEventHandler(this));
        handlers.add(this.fireworks.addNodeSelectedResetEventHandler(this));
        handlers.add(this.fireworks.addNodeHoverResetEventHandler(this));
        this.container.clear();
        this.container.add(this.fireworks);
    }

    @Override
    public void highlightPathway(Pathway pathway) {
        if(pathway==null) return;
        this.fireworks.highlightNodeByDbIdentifier(pathway.getDbId());
    }

    @Override
    public void resetHighlight() {
        this.fireworks.resetHighlight();
    }

    @Override
    public void selectPathway(Pathway pathway) {
        if(pathway==null) return;
        this.fireworks.selectNodeByDbIdentifier(pathway.getDbId());
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
    public void onNodeHover(NodeHoverEvent event) {
        this.presenter.highlightPathway(event.getNode().getDbId());
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
}
