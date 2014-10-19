package org.reactome.web.elv.client.center.content.fireworks.presenter;

import org.reactome.web.elv.client.center.content.fireworks.view.FireworksView;
import org.reactome.web.elv.client.common.Controller;
import org.reactome.web.elv.client.common.EventBus;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.common.events.EventHoverEvent;
import org.reactome.web.elv.client.common.events.EventHoverResetEvent;
import org.reactome.web.elv.client.common.handlers.EventHoverHandler;
import org.reactome.web.elv.client.common.handlers.EventHoverResetHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class FireworksPresenter extends Controller implements FireworksView.Presenter, EventHoverHandler, EventHoverResetHandler {

    private FireworksView view;

    public FireworksPresenter(EventBus eventBus, FireworksView view) {
        super(eventBus);
        this.view = view;
        this.view.setPresenter(this);

        this.eventBus.addHandler(EventHoverEvent.TYPE, this);
        this.eventBus.addHandler(EventHoverResetEvent.TYPE, this);
    }

    @Override
    public void onHierarchyEventHovered(EventHoverEvent e) {
        if(e.getSource()==this) return;
        Pathway toHighlight;
        if(e.getEvent() instanceof Pathway){
            toHighlight = (Pathway) e.getEvent();
        }else{
            toHighlight = (Pathway) e.getPath().get(e.getPath().size()-1);
        }
        this.view.highlightPathway(toHighlight);
    }

    @Override
    public void onHierarchyEventHoveredReset() {
        this.view.resetHighlight();
    }
}
