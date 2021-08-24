package org.reactome.web.pwp.client.viewport.diagram;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Timer;
import org.reactome.web.pwp.client.common.AnalysisStatus;
import org.reactome.web.pwp.client.common.Selection;
import org.reactome.web.pwp.client.common.events.*;
import org.reactome.web.pwp.client.common.handlers.DatabaseObjectHoveredHandler;
import org.reactome.web.pwp.client.common.handlers.ViewportChangedHandler;
import org.reactome.web.pwp.client.common.model.classes.CellLineagePath;
import org.reactome.web.pwp.client.common.model.classes.DatabaseObject;
import org.reactome.web.pwp.client.common.model.classes.Event;
import org.reactome.web.pwp.client.common.model.classes.Pathway;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectFactory;
import org.reactome.web.pwp.client.common.model.handlers.DatabaseObjectCreatedHandler;
import org.reactome.web.pwp.client.common.model.util.Path;
import org.reactome.web.pwp.client.common.module.AbstractPresenter;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.manager.state.State;
import org.reactome.web.pwp.client.viewport.ViewportToolType;

import java.util.Objects;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DiagramPresenter extends AbstractPresenter implements Diagram.Presenter, ViewportChangedHandler,
        DatabaseObjectHoveredHandler {

    private Diagram.Display display;

    private Event displayedPathwayOrCellLineagePath;

    private Event event;
    private DatabaseObject selected;
    private Path path;
    private AnalysisStatus analysisStatus = new AnalysisStatus();
    private Long hovered;
    private String flag;

    public DiagramPresenter(EventBus eventBus, Diagram.Display display) {
        super(eventBus);
        this.display = display;
        this.display.setPresenter(this);
        this.path = new Path();

        this.eventBus.addHandler(ViewportChangedEvent.TYPE, this);
        this.eventBus.addHandler(DatabaseObjectHoveredEvent.TYPE, this);
    }

    @Override
    public void databaseObjectSelected(final Long dbId) {
        if (timer != null && timer.isRunning()) timer.cancel();
        if (dbId != null) {
            DatabaseObjectFactory.get(dbId, new DatabaseObjectCreatedHandler() {
                @Override
                public void onDatabaseObjectLoaded(DatabaseObject databaseObject) {
                    Selection selection = new Selection(event, databaseObject, path);
                    eventBus.fireEventFromSource(new DatabaseObjectSelectedEvent(selection), DiagramPresenter.this);
                }

                @Override
                public void onDatabaseObjectError(Throwable exception) {
                    String errorMsg = "An error has occurred while retrieving data for " + dbId;
                    eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg), DiagramPresenter.this);
                }
            });
        } else {
            if (selected != null) {
                this.selected = null;
                Selection selection = new Selection(this.event, this.path);
                this.eventBus.fireEventFromSource(new DatabaseObjectSelectedEvent(selection), DiagramPresenter.this);
            }
        }
    }

    @Override
    public void onDatabaseObjectHovered(DatabaseObjectHoveredEvent event) {
        Long hovered = event.getDatabaseObject() != null ? event.getDatabaseObject().getDbId() : null;
        if(!Objects.equals(hovered, this.hovered)) {
            this.hovered = hovered;
            this.display.highlight(event.getDatabaseObject());
        }
    }

    private static final int HOVER_DELAY = 500;
    private Timer timer ;

    @Override
    public void databaseObjectHovered(final Long dbId) {
        if (Objects.equals(this.hovered, dbId)) return;
        this.hovered = dbId;
        if (timer != null && timer.isRunning()) timer.cancel();
        if (dbId != null) {
            timer = new Timer() {
                @Override
                public void run() {
                    DatabaseObjectFactory.get(dbId, new DatabaseObjectCreatedHandler() {
                        @Override
                        public void onDatabaseObjectLoaded(DatabaseObject databaseObject) {
                            if(!Objects.equals(selected, databaseObject)) {
                                eventBus.fireEventFromSource(new DatabaseObjectHoveredEvent(databaseObject), DiagramPresenter.this);
                            }
                        }

                        @Override
                        public void onDatabaseObjectError(Throwable exception) {
                            String errorMsg = "An error has occurred while retrieving data for " + dbId;
                            eventBus.fireEventFromSource(new ErrorMessageEvent(errorMsg), DiagramPresenter.this);
                        }
                    });
                }
            };
            timer.schedule(HOVER_DELAY);
        } else {
            this.eventBus.fireEventFromSource(new DatabaseObjectHoveredEvent(), DiagramPresenter.this);
        }
    }

    @Override
    public void onStateChanged(StateChangedEvent event) {
        State state = event.getState();
        boolean isNewDiagram = !Objects.equals(this.event, state.getEventWithDiagram());
        this.event = state.getEventWithDiagram();
        this.selected = state.getSelected();
        this.path = state.getPath();
        this.analysisStatus = state.getAnalysisStatus();
        this.flag = state.getFlag();
        if(this.display.isVisible()) {
            if (isNewDiagram) {
                this.loadCurrentDiagram();
            } else {
                updateView();
            }
        }
    }

    @Override
    public void onViewportChanged(ViewportChangedEvent event) {
        if(event.getViewportTool().equals(ViewportToolType.DIAGRAM)) {
            this.loadCurrentDiagram();
        }
    }

    @Override
    public void diagramLoaded(final Long dbId) {
        DatabaseObjectFactory.get(dbId, new DatabaseObjectCreatedHandler() {
            @Override
            public void onDatabaseObjectLoaded(DatabaseObject databaseObject) {
                setDisplayedPathwayOrCellLineagePath(databaseObject);
                if (Objects.equals(event, displayedPathwayOrCellLineagePath)) {
                    updateView();
                } else {
                    event = displayedPathwayOrCellLineagePath;
                    Selection selection = new Selection(event, new Path());
                    eventBus.fireEventFromSource(new DatabaseObjectSelectedEvent(selection), DiagramPresenter.this);
                }
            }

            @Override
            public void onDatabaseObjectError(Throwable exception) {
                displayedPathwayOrCellLineagePath = null;
            }
        });
    }

    private void loadCurrentDiagram(){
        if (this.event == null) {
            Console.warn("Undetermined event for diagram...", this);
        } else if (!Objects.equals(event, displayedPathwayOrCellLineagePath)) {
            if (this.event instanceof Pathway) {
                this.display.loadPathway((Pathway) this.event);
            } else if (this.event instanceof CellLineagePath) {
                this.display.loadCellLineagePath((CellLineagePath) this.event);
            } else {
                Console.warn("Unknown event type for displaying diagram for " + event.getDisplayName() +
                    ": " + this.event.getClass().getName());
            }
        } else {
            updateView();
        }
    }

    private void updateView(){
        this.display.select(this.selected);
        display.setAnalysisToken(analysisStatus);
        display.flag(this.flag);
    }

    private void setDisplayedPathwayOrCellLineagePath(DatabaseObject databaseObject) {
        if (databaseObject instanceof Pathway) {
            displayedPathwayOrCellLineagePath = (Pathway) databaseObject;
        } else if (databaseObject instanceof CellLineagePath) {
            displayedPathwayOrCellLineagePath = (CellLineagePath) databaseObject;
        } else {
            Console.warn("Unknown database object type for displaying diagram " + databaseObject);
        }
    }
}
