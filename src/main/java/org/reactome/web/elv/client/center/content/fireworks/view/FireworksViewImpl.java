package org.reactome.web.elv.client.center.content.fireworks.view;

import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.elv.client.center.content.fireworks.util.Resources;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.fireworks.client.FireworksFactory;
import org.reactome.web.fireworks.client.FireworksViewer;
import org.reactome.web.fireworks.events.NodeHoverEvent;
import org.reactome.web.fireworks.events.NodeSelectedEvent;
import org.reactome.web.fireworks.handlers.NodeHoverEventHandler;
import org.reactome.web.fireworks.handlers.NodeHoverResetEventHandler;
import org.reactome.web.fireworks.handlers.NodeSelectedEventHandler;
import org.reactome.web.fireworks.handlers.NodeSelectedResetEventHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class FireworksViewImpl implements FireworksView, NodeHoverEventHandler, NodeSelectedEventHandler, NodeSelectedResetEventHandler, NodeHoverResetEventHandler {
    private Presenter presenter;
    private FireworksViewer fireworks;


    public FireworksViewImpl() {
        //For the time being the json is retrieved from a static file in resources
        TextResource json = Resources.INSTANCE.getFireworks();
        this.fireworks = FireworksFactory.createFireworksViewer(json);
        this.fireworks.addNodeHoverEventHandler(this);
        this.fireworks.addNodeSelectedEventHandler(this);
        this.fireworks.addNodeSelectedResetEventHandler(this);
        this.fireworks.addNodeHoverResetEventHandler(this);
    }

    @Override
    public Widget asWidget() {
        return this.fireworks.asWidget();
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
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
