package org.reactome.web.pwp.client.details.tabs.expression;

import com.google.gwt.event.shared.EventBus;
import org.reactome.web.pwp.client.common.events.ErrorMessageEvent;
import org.reactome.web.pwp.client.common.events.StateChangedEvent;
import org.reactome.web.pwp.client.common.model.classes.*;
import org.reactome.web.pwp.client.common.model.client.RESTFulClient;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectFactory;
import org.reactome.web.pwp.client.common.model.handlers.DatabaseObjectsCreatedHandler;
import org.reactome.web.pwp.client.common.model.handlers.DatabaseObjectsLoadedHandler;
import org.reactome.web.pwp.client.common.module.AbstractPresenter;
import org.reactome.web.pwp.client.manager.state.State;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ExpressionTabPresenter extends AbstractPresenter implements ExpressionTab.Presenter {

    private ExpressionTab.Display display;
    private DatabaseObject currentlyShown;

    public ExpressionTabPresenter(EventBus eventBus, ExpressionTab.Display display) {
        super(eventBus);
        this.display = display;
        this.display.setPresenter(this);
    }

    @Override
    public void onStateChanged(StateChangedEvent stateChangedEvent) {
        State state = stateChangedEvent.getState();

        //Is it me the one to show data?
        if (!state.getDetailsTab().equals(display.getDetailTabType())) return;

        //Show the data
        DatabaseObject databaseObject = state.getSelected();
        if (databaseObject instanceof Pathway || databaseObject instanceof CellLineagePath) {
            if(!databaseObject.equals(this.currentlyShown)) {
                this.currentlyShown = databaseObject;
                this.display.showEventWithDiagram((Event) databaseObject);
            }
        } else if (databaseObject instanceof PhysicalEntity || databaseObject instanceof Event) {
            if(!databaseObject.equals(this.currentlyShown)) {
                this.currentlyShown = databaseObject;
                this.display.showProteins(databaseObject);
            }
        } else if (state.getEventWithDiagram() != null) {
            Event eventWithDiagram = state.getEventWithDiagram();
            if(!eventWithDiagram.equals(this.currentlyShown)) {
                this.currentlyShown = eventWithDiagram;
                this.display.showEventWithDiagram(eventWithDiagram);
            }
        } else {
            this.currentlyShown = null;
            this.display.setInitialState();
        }
    }

    @Override
    public void getReferenceSequences(final DatabaseObject databaseObject) {
        RESTFulClient.loadReferenceSequences(databaseObject, new DatabaseObjectsLoadedHandler<ReferenceSequence>() {
            @Override
            public void onDatabaseObjectLoaded(List<ReferenceSequence> objects) {
                processReferenceSequences(databaseObject, objects);
            }

            @Override
            public void onDatabaseObjectError(Throwable ex) {
                display.showErrorMessage(ex.getMessage());
                eventBus.fireEventFromSource(new ErrorMessageEvent(ex.getMessage(), ex), ExpressionTabPresenter.this);
            }
        });
    }

    private void processReferenceSequences(final DatabaseObject databaseObject, List<ReferenceSequence> referenceSequenceList){
        if (referenceSequenceList.isEmpty()) {
            display.showReferenceSequences(databaseObject, referenceSequenceList);
        } else {
            DatabaseObjectFactory.get(referenceSequenceList, new DatabaseObjectsCreatedHandler() {
                @Override
                public void onDatabaseObjectsLoaded(Map<String, DatabaseObject> databaseObjects) {
                    List<ReferenceSequence> rtn = new LinkedList<>();
                    for (DatabaseObject object : databaseObjects.values()) {
                        if (object instanceof ReferenceSequence) {
                            rtn.add((ReferenceSequence) object);
                        }
                    }
                    if (!rtn.isEmpty()) {
                        display.showReferenceSequences(databaseObject, rtn);
                    } else {
                        String errorMsg = "There is not information available for " + databaseObject.getDisplayName();
                        display.showErrorMessage(errorMsg);
                        eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg), ExpressionTabPresenter.this);
                    }
                }

                @Override
                public void onDatabaseObjectError(Throwable exception) {
                    String errorMsg = "There is not information available for " + databaseObject.getDisplayName();
                    display.showErrorMessage(errorMsg);
                    eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg, exception), ExpressionTabPresenter.this);
                }
            });

        }
    }
}
