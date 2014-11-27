package org.reactome.web.elv.client.center.content.fireworks.presenter;

import com.google.gwt.core.client.GWT;
import org.reactome.web.elv.client.center.content.fireworks.view.FireworksView;
import org.reactome.web.elv.client.common.Controller;
import org.reactome.web.elv.client.common.EventBus;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Event;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.common.events.ELVEventType;
import org.reactome.web.elv.client.common.events.EventHoverEvent;
import org.reactome.web.elv.client.common.events.EventHoverResetEvent;
import org.reactome.web.elv.client.common.handlers.EventHoverHandler;
import org.reactome.web.elv.client.common.handlers.EventHoverResetHandler;
import org.reactome.web.elv.client.common.model.Pair;
import org.reactome.web.elv.client.manager.data.DataManager;
import org.reactome.web.elv.client.manager.messages.MessageObject;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class FireworksPresenter extends Controller implements FireworksView.Presenter, EventHoverHandler, EventHoverResetHandler {
    private FireworksView view;

    private Long selected;

    public FireworksPresenter(EventBus eventBus, FireworksView view) {
        super(eventBus);
        this.view = view;
        this.view.setPresenter(this);

        this.eventBus.addHandler(EventHoverEvent.TYPE, this);
        this.eventBus.addHandler(EventHoverResetEvent.TYPE, this);
    }

    @Override
    public void onEventHovered(EventHoverEvent e) {
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
    public void onEventHoveredReset() {
        this.view.resetHighlight();
    }


    @Override
    public void onStateManagerDatabaseObjectsSelected(List<Event> path, Pathway pathway, DatabaseObject databaseObject) {
        Pathway toSelect;
        if(databaseObject instanceof Pathway){
            toSelect = (Pathway) databaseObject;
        }else{
            toSelect = (Pathway) path.get(path.size()-1);
        }
        this.selected = toSelect.getDbId();
        this.view.selectPathway(toSelect);
    }

    @Override
    public void onStateManagerInstancesInitialStateReached() {
        this.view.resetSelection();
    }

    @Override
    public void selectPathway(Long dbId) {
        if(dbId.equals(selected)) return;
        Pair<Long, ELVEventType> tuple = new Pair<Long, ELVEventType>(dbId, ELVEventType.FIREWORKS_PATHWAY_SELECTED);
        this.eventBus.fireELVEvent(ELVEventType.DATABASE_OBJECT_REQUIRED, tuple);
    }

    @Override
    public void resetPathwaySelection() {
        this.selected = null;
        this.eventBus.fireELVEvent(ELVEventType.FIREWORKS_PATHWAY_SELECTION_RESET);
    }

    @Override
    public void highlightPathway(Long dbId) {
        final FireworksPresenter _this = this;
        try {
            DataManager.getDataManager().databaseObjectDetailedViewRequired(dbId, new DataManager.DataManagerObjectRetrievedHandler() {
                @Override
                public void onDatabaseObjectRetrieved(DatabaseObject databaseObject) {
                    eventBus.fireEventFromSource(new EventHoverEvent((Pathway) databaseObject), _this);
                }

                @Override
                public void onError(MessageObject messageObject) {
                    eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, messageObject);
                }
            });
        } catch (Exception e) {
            //VERY UNLIKELY TO HAPPEN HERE :)
            if(!GWT.isScript()) e.printStackTrace();
        }
    }

    @Override
    public void resetPathwayHighlighting() {
        eventBus.fireEventFromSource(new EventHoverResetEvent(), this);
    }
}
