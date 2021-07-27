package org.reactome.web.pwp.client.hierarchy;

import com.google.gwt.event.shared.EventBus;
import org.reactome.web.pwp.client.common.AnalysisStatus;
import org.reactome.web.pwp.client.common.Selection;
import org.reactome.web.pwp.client.common.events.*;
import org.reactome.web.pwp.client.common.model.classes.CellLineagePath;
import org.reactome.web.pwp.client.common.model.classes.Event;
import org.reactome.web.pwp.client.common.model.classes.Pathway;
import org.reactome.web.pwp.client.common.model.classes.Species;
import org.reactome.web.pwp.client.common.model.client.RESTFulClient;
import org.reactome.web.pwp.client.common.model.handlers.DatabaseObjectsLoadedHandler;
import org.reactome.web.pwp.client.common.model.util.Path;
import org.reactome.web.pwp.client.common.module.AbstractPresenter;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.hierarchy.delgates.HierarchyPathLoader;
import org.reactome.web.pwp.client.manager.state.State;

import java.util.*;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class HierarchyPresenter extends AbstractPresenter implements Hierarchy.Presenter, HierarchyPathLoader.HierarchyPathLoaderHandler {

    private Hierarchy.Display display;

    private HierarchyPathLoader pathLoader;

    private Species currentSpecies;
    private Selection selection;
    private Selection toSelect; //Used in the call back from the display

    private AnalysisStatus analysisStatus;

    public HierarchyPresenter(EventBus eventBus, Hierarchy.Display display) {
        super(eventBus);
        this.display = display;
        this.display.setPresenter(this);
    }

    @Override
    public void onStateChanged(StateChangedEvent event) {
        State state = event.getState();
        Selection selection = new Selection(state.getPathway(), state.getSelected(), state.getPath());
        Species toSpecies = state.getSpecies();
        this.toSelect = selection; //Important!
        if(!toSpecies.equals(currentSpecies)){
            this.currentSpecies = toSpecies;
            display.show(this.currentSpecies);
        }else if(!selection.equals(this.selection)) {
            this.pathLoader = new HierarchyPathLoader(this);
            Event target = getTarget(selection);
            if(target!=null) {
                this.pathLoader.loadHierarchyEvent(selection.getPath(), getTarget(selection));
            }else{
                this.selection = this.toSelect;
                this.display.select(null, null); //Just clear highlights
            }
        }

        if(!Objects.equals(this.analysisStatus, state.getAnalysisStatus())){
            this.analysisStatus = state.getAnalysisStatus();
            display.clearAnalysisResult();
        }
    }

    @Override
    public void expandPathway(Path path, Pathway pathway) {
        display.expandPathway(path, pathway);
    }

    @Override
    public void onPathLoaded(Path path) {
        this.pathLoader = null;
        this.selection = toSelect;
        this.display.select(getTarget(this.toSelect), path);
    }

    @Override
    public void eventHovered(Pathway pathway, Event event, Path path) {
        Event target = event!=null?event:pathway;
        this.eventBus.fireEventFromSource(new DatabaseObjectHoveredEvent(target, path), this);
    }

    @Override
    public void eventHoveredReset() {
        this.eventBus.fireEventFromSource(new DatabaseObjectHoveredEvent(), this);
    }

    @Override
    public void eventSelected(Pathway pathway, Event event, Path path) {
        Selection selection = new Selection(pathway, event, path);
        if(!selection.equals(this.selection)) {
            this.selection = selection;
            this.eventBus.fireEventFromSource(new DatabaseObjectSelectedEvent(selection), this);
        }
    }

    @Override
    public void hierarchyChanged(Species species) {
        display.clearAnalysisResult();

        this.pathLoader = new HierarchyPathLoader(this);
        Event target = getTarget(toSelect);
        if(target!=null) {
            this.pathLoader.loadHierarchyEvent(toSelect.getPath(), getTarget(toSelect));
        }else{
            this.selection = this.toSelect;
            this.display.select(null, null);  //Just clear highlights
        }
    }

    @Override
    public void openDiagram(Pathway pathway) {
        this.eventBus.fireEventFromSource(new PathwayDiagramOpenRequestEvent(pathway), this);
    }

    @Override
    public void pathwayExpanded(Pathway pathway) {
        if(this.pathLoader!=null) {
            this.pathLoader.expandPath(); //continue expanding path
        }
        Set<Pathway> pathways = new HashSet<>();
        Set<Pathway> pathwaysWithReactions = new HashSet<>();
        for (Event event : pathway.getHasEvent()) {
            if (event instanceof Pathway) {
                pathways.add((Pathway) event);
            } else {
                pathwaysWithReactions.add(pathway);
            }
        }
    }

    @Override
    public void retrieveData(final Species species) {
        List<Event> topLevelEvents = new ArrayList<>();
        RESTFulClient.getFrontPageItems(species, new DatabaseObjectsLoadedHandler<Event>() {
            @Override
            public void onDatabaseObjectLoaded(List<Event> objects) {
                topLevelEvents.addAll(objects);

                RESTFulClient.getCellLineagePathItems(species, new DatabaseObjectsLoadedHandler<CellLineagePath>() {
                    @Override
                    public void onDatabaseObjectLoaded(List<CellLineagePath> objects) {
                        topLevelEvents.addAll(objects);
                        display.setData(species, topLevelEvents);
                    }

                    @Override
                    public void onDatabaseObjectError(Throwable ex) {
                        Console.error(ex.getMessage(), HierarchyPresenter.this);
                        eventBus.fireEventFromSource(new ErrorMessageEvent(ex.getMessage()), this);
                    }
                });
            }

            @Override
            public void onDatabaseObjectError(Throwable ex) {
                Console.error(ex.getMessage(), HierarchyPresenter.this);
                eventBus.fireEventFromSource(new ErrorMessageEvent(ex.getMessage()), this);
            }
        });
    }

    private Event getTarget(Selection selection){
        Event event;
        if(selection.getDatabaseObject()!=null && selection.getDatabaseObject() instanceof Event){
            event = selection.getDatabaseObject().cast();
        }else{
            event = selection.getDiagram();
        }
        return event;
    }
}
