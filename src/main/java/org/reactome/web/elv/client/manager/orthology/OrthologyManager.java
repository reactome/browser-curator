package org.reactome.web.elv.client.manager.orthology;

import org.reactome.web.elv.client.common.Controller;
import org.reactome.web.elv.client.common.EventBus;
import org.reactome.web.elv.client.common.data.model.*;
import org.reactome.web.elv.client.common.events.ELVEventType;
import org.reactome.web.elv.client.common.model.Path;
import org.reactome.web.elv.client.common.utils.Console;
import org.reactome.web.elv.client.manager.state.AdvancedState;

import java.util.*;

/**
 * This manager is in charge to infer the selected diagram and diagram selected object (if that is the case)
 * when switching from one species to another.
 *
 * NOTE: In REACTOME KB there is information from Homo Sapiens to Other Species (orthologous) or Other Species to Homo
 *       Sapiens (inferredFrom) so this manager provides the ID's for the orthologous and inferredFrom in those cases,
 *       but also provides inference between Other Species to Other Species just taking the inferredFrom to get the
 *       Homo Sapiens correspondence, and then getting the orthologous from Homo Sapiens to the next Other Species.
 *
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class OrthologyManager extends Controller {
    private Species species;
    private List<Event> path = new LinkedList<Event>();
    private Pathway diagram;
    private DatabaseObject databaseObject;

    private Map<Long, DatabaseObject> detailedViewMap = new HashMap<Long, DatabaseObject>();
    private Set<Long> requiredDetailedObjects = new HashSet<Long>();
    private Species otherSpecies = null;

    private boolean moveToDatabaseObject = false;

    public OrthologyManager(EventBus eventBus) {
        super(eventBus);
    }

    private void cacheDetailedView(DatabaseObject databaseObject) {
        this.detailedViewMap.put(databaseObject.getDbId(), databaseObject);
    }

    private void ensureObjectForOrthologousFromOtherSpecies(Event event){
        Event e = this.getInferredFrom(event);
        if (e!=null && !this.detailedViewMap.containsKey(e.getDbId())) {
            this.requiredDetailedObjects.add(e.getDbId());
            this.eventBus.fireELVEvent(ELVEventType.DATABASE_OBJECT_DETAILED_VIEW_REQUIRED, e.getDbId());
        }
    }

    private void ensureObjectForOrthologousFromOtherSpecies(PhysicalEntity physicalEntity){
        PhysicalEntity p = this.getInferredFrom(physicalEntity);
        if(p!=null && !this.detailedViewMap.containsKey(p.getDbId())) {
            this.requiredDetailedObjects.add(p.getDbId());
            this.eventBus.fireELVEvent(ELVEventType.DATABASE_OBJECT_DETAILED_VIEW_REQUIRED, p.getDbId());
        }
    }

    private void ensureObjectForOrthologousFromOtherSpecies(Species nextSpecies){
        for (Event e : this.path) {
            Event event = (Event) this.detailedViewMap.get(e.getDbId());
            if(event!=null){
                this.ensureObjectForOrthologousFromOtherSpecies(event);
            }
        }

        Long selectedDiagram = this.diagram!=null?this.diagram.getDbId():null;
        if (selectedDiagram != null) {
            Pathway aux = (Pathway) this.detailedViewMap.get(selectedDiagram);
            this.ensureObjectForOrthologousFromOtherSpecies(aux);
        }

        Long selectedDatabaseObject = this.databaseObject!=null?this.databaseObject.getDbId():null;
        if (selectedDatabaseObject != null) {
            DatabaseObject aux = this.detailedViewMap.get(selectedDatabaseObject);
            if (aux instanceof Event) {
                this.ensureObjectForOrthologousFromOtherSpecies((Event) aux);
            } else if (aux instanceof PhysicalEntity) {
                this.ensureObjectForOrthologousFromOtherSpecies((PhysicalEntity) aux);
            }
        }

        //Asynchronous call done -> there will be room for this before "DATA_MANAGER_OBJECT_DETAILED_VIEW_RETRIEVED"
        if(this.requiredDetailedObjects.isEmpty()){
            //All needed detailed objects available
            this.goFromOtherSpeciesToOtherSpecies(nextSpecies);
        }else{
            this.otherSpecies = nextSpecies;
        }
    }

    private AdvancedState getFromHomoSapiensToOtherSpecies(Species nextSpecies) {
        AdvancedState desiredState = new AdvancedState();
        desiredState.setSpecies(nextSpecies);

        desiredState.setPath(getOrthologousPathFromHumanToOtherSpecies(this.path, nextSpecies));

        Long selectedDiagram = this.diagram!=null?this.diagram.getDbId():null;
        Event diagramOrth = null;
        if (selectedDiagram != null) {
            Pathway d = (Pathway) this.detailedViewMap.get(selectedDiagram);
            diagramOrth = this.getOrthologousFromHomoSapiens(d, nextSpecies);
            if(diagramOrth!=null && diagramOrth instanceof Pathway){
                desiredState.setPathway((Pathway) diagramOrth);
            }
        }

        Long selectedDatabaseObject = this.databaseObject!=null?this.databaseObject.getDbId():null;
        if (diagramOrth!=null && selectedDatabaseObject != null) {
            DatabaseObject aux = this.detailedViewMap.get(selectedDatabaseObject);
            if (aux instanceof Event) {
                Event e = (Event) aux;
                Event orth = this.getOrthologousFromHomoSapiens(e, nextSpecies);
                if(orth!=null){
                    desiredState.setInstance(orth);
                }
            } else if (aux instanceof PhysicalEntity) {
                PhysicalEntity p = (PhysicalEntity) aux;
                PhysicalEntity orth = this.getOrthologousFromHomoSapiens(p, nextSpecies);
                if(orth!=null){
                    desiredState.setInstance(orth);
                }
            }
        }

        return desiredState;
    }

    private AdvancedState getFromOtherSpeciesToHomoSapiens(Species nextSpecies){
        AdvancedState desiredState = new AdvancedState();
        desiredState.setSpecies(nextSpecies);

        desiredState.setPath(getOrhtologousPathFromOtherSpeciesToHuman(this.path));

        Long selectedDiagram = this.diagram!=null?this.diagram.getDbId():null;
        if (selectedDiagram != null) {
            Pathway aux = (Pathway) this.detailedViewMap.get(selectedDiagram);
            Event inf = this.getInferredFrom(aux);
            if(inf!=null && inf instanceof Pathway){
                desiredState.setPathway((Pathway) inf);
            }
        }

        Long selectedDatabaseObject = this.databaseObject!=null?this.databaseObject.getDbId():null;
        if (selectedDatabaseObject != null) {
            DatabaseObject aux = this.detailedViewMap.get(selectedDatabaseObject);
            if (aux instanceof Event) {
                Event inf = this.getInferredFrom((Event) aux);
                if(inf!=null){
                    desiredState.setInstance(inf);
                }
            } else if (aux instanceof PhysicalEntity) {
                PhysicalEntity inf = this.getInferredFrom((PhysicalEntity) aux);
                if(inf!=null){
                    desiredState.setInstance(inf);
                }
            }
        }

        return desiredState;
    }

    private List<Event> getOrthologousPathFromHumanToOtherSpecies(List<Event> path, Species nextSpecies){
        List<Event> pathOrth = new LinkedList<Event>();
        for (Event e : path) {
            Event event = (Event) this.detailedViewMap.get(e.getDbId());
            Event eOrth = this.getOrthologousFromHomoSapiens(event, nextSpecies);
            if(eOrth!=null){
                pathOrth.add(eOrth);
            }
        }
        return pathOrth;
    }

    private List<Event> getOrhtologousPathFromOtherSpeciesToHuman(List<Event> path){
        List<Event> pathInf = new LinkedList<Event>();
        for (Event e : path) {
            Event event = (Event) this.detailedViewMap.get(e.getDbId());
            Event inf = this.getInferredFrom(event);
            if(inf!=null){
                pathInf.add(inf);
            }
        }
        return pathInf;
    }

    private List<Event> getOrthologousPathFromOtherSpeciesToOtherSpecies(List<Event> path){
        List<Event> pathAux = new LinkedList<Event>();
        for (Event e : path) {
            Event event = (Event) this.detailedViewMap.get(e.getDbId());
            if(event!=null){
                pathAux.add(this.getInferredFrom(event));
            }
        }
        return pathAux;
    }

    private void goFromOtherSpeciesToOtherSpecies(Species nextSpecies){
        this.otherSpecies = null;

        this.path = getOrthologousPathFromOtherSpeciesToOtherSpecies(this.path);

        Long selectedDiagram = this.diagram!=null?this.diagram.getDbId():null;
        if (selectedDiagram != null) {
            Pathway d = (Pathway) this.detailedViewMap.get(selectedDiagram);
            this.diagram = (Pathway) this.getInferredFrom(d);
        }

        Long selectedDatabaseObject = this.databaseObject!=null?this.databaseObject.getDbId():null;
        if (selectedDatabaseObject != null) {
            DatabaseObject aux = this.detailedViewMap.get(selectedDatabaseObject);
            if (aux instanceof Event) {
                this.databaseObject = this.getInferredFrom((Event) aux);
            } else if (aux instanceof PhysicalEntity) {
                this.databaseObject = this.getInferredFrom((PhysicalEntity) aux);
            }
        }

        AdvancedState desiredState = getFromHomoSapiensToOtherSpecies(nextSpecies);
        this.eventBus.fireELVEvent(ELVEventType.ORTHOLOGOUS_MANAGER_STATE_SELECTED, desiredState);
    }

    private Event getInferredFrom(Event event){
        if(event.getInferredFrom().isEmpty()){
            Console.error(getClass() + " -> there is not inferred from event");
            return null;
        }else{
            if(event.getInferredFrom().size()==1){
                return event.getInferredFrom().get(0);
            }else{
                Console.error(getClass() + " -> there is more than one inferred from event");
                return null;
            }
        }
    }

    private PhysicalEntity getInferredFrom(PhysicalEntity physicalEntity){
        if(physicalEntity.getInferredFrom().size()!=1){
            Console.error(getClass() + " -> there is more than one inferred from PE");
            return null;
        }else{
            return physicalEntity.getInferredFrom().get(0);
        }
    }

    private Event getOrthologousFromHomoSapiens(Event event, Species species){
        for (Event e : event.getOrthologousEvent()) {
            for (Species s : e.getSpecies()) {
                if (species.equals(s)) {
                    return e;
                }
            }
        }
        return null;
    }

    private PhysicalEntity getOrthologousFromHomoSapiens(PhysicalEntity physicalEntity, Species species){
        for (PhysicalEntity p : physicalEntity.getInferredTo()) {
            for (Species s : p.getSpecies()) {
                if (species.equals(s)) {
                    return p;
                }
            }
        }
        return null;
    }

    private Species getSpecies(DatabaseObject databaseObject){
        if(databaseObject instanceof Species){
            return (Species) databaseObject;
        }

        List<Species> speciesList = new LinkedList<Species>();
        if(databaseObject instanceof Event){
            Event e = (Event) databaseObject;
            speciesList = e.getSpecies();
        }
        if(databaseObject instanceof PhysicalEntity){
            PhysicalEntity p = (PhysicalEntity) databaseObject;
            speciesList = p.getSpecies();
        }
        if(speciesList.isEmpty()){
            Console.error(getClass() + " -> unknown action for " + databaseObject + " >> species set to " + this.species.getDisplayName());
            return this.species;
        }else if(speciesList.size()!=1){
            Console.error(getClass() + " -> several species for " + databaseObject + " >> species set to " + this.species.getDisplayName());
            return this.species;
        }else{
            return speciesList.get(0);
        }
    }

    private boolean isHomoSapiens(Species species){
        return species.getDbId().equals(AdvancedState.DEFAULT_SPECIES_ID);
    }

    //when called, databaseObject should be loaded using the detailed view RESTFul method
    private void moveToDatabaseObject(DatabaseObject databaseObject){
        Species species = this.getSpecies(databaseObject);

        List<Event> path;
        Event nextDiagram;
        if(this.species.equals(species)){
            path = this.path;
            nextDiagram = diagram;
        }else if(this.diagram.isInferred()){
            path = getOrhtologousPathFromOtherSpeciesToHuman(this.path);
            Pathway d = (Pathway) this.detailedViewMap.get(this.diagram.getDbId());
            nextDiagram = this.getInferredFrom(d);
        }else {
            path = getOrthologousPathFromHumanToOtherSpecies(this.path, species);
            Pathway d = (Pathway) this.detailedViewMap.get(this.diagram.getDbId());
            nextDiagram = this.getOrthologousFromHomoSapiens(d, species);
        }

        // when the next diagram can not be calculated we stays where we are
        // and just show the selected instance without changing the scope
        if(nextDiagram==null){
            species = this.species;
            path = this.path;
            nextDiagram = this.diagram;
        }

        AdvancedState desiredState = new AdvancedState();
        desiredState.setSpecies(species);
        desiredState.setPath(path);
        desiredState.setPathway(nextDiagram!=null ? (Pathway) nextDiagram : null);
        desiredState.setInstance(databaseObject);
        this.eventBus.fireELVEvent(ELVEventType.ORTHOLOGOUS_MANAGER_STATE_SELECTED, desiredState);
    }

    private void moveToSpecies(Species species){
        if(this.isHomoSapiens(this.species)){
            //FROM HomoSapiens to other species
            AdvancedState desiredState = this.getFromHomoSapiensToOtherSpecies(species);
            this.eventBus.fireELVEvent(ELVEventType.ORTHOLOGOUS_MANAGER_STATE_SELECTED, desiredState);
        }else if(this.isHomoSapiens(species)){
            //FROM other species to HomoSapiens
            AdvancedState desiredState = this.getFromOtherSpeciesToHomoSapiens(species);
            this.eventBus.fireELVEvent(ELVEventType.ORTHOLOGOUS_MANAGER_STATE_SELECTED, desiredState);
        }else{
            //FROM other species to other species (both different to HomoSapiens)
            this.ensureObjectForOrthologousFromOtherSpecies(species);
        }
    }

    @Override
    public void onDataManagerObjectDetailedViewRetrieved(DatabaseObject databaseObject) {
        this.cacheDetailedView(databaseObject);

        if(this.moveToDatabaseObject){
            this.moveToDatabaseObject = false;
            moveToDatabaseObject(databaseObject);
            return;
        }

        //When this happens is because the manager is in its way from one Species (different to Homo Sapiens)
        //to other Species (different to Homo Sapines as well).
        if(this.otherSpecies!=null){
            this.requiredDetailedObjects.remove(databaseObject.getDbId());
            //When this happens means that all the needed data is available
            if(this.requiredDetailedObjects.isEmpty()){
                //NOTE: this.otherSpecies will be set to null into the method
                this.goFromOtherSpeciesToOtherSpecies(this.otherSpecies);
            }
        }
    }

    private List<Event> getPathUntilPathwayWithDiagram(List<Event> path){
        List<Event> pathAux = new LinkedList<Event>();
        boolean added = false;
        ListIterator li = path.listIterator(path.size());
        while(li.hasPrevious()) {
            Event e = (Event) li.previous();
            Event event = (Event) this.detailedViewMap.get(e.getDbId());
            if(added){
                pathAux.add(0, event);
            }else if(event instanceof Pathway && ((Pathway) event).getHasDiagram()){
                pathAux.add(0, event);
                added = true;
            }
        }
        return pathAux;
    }

    private void goToDatabaseObject(DatabaseObject databaseObject){
        if(databaseObject instanceof PhysicalEntity){
            this.path = this.getPathUntilPathwayWithDiagram(this.path);
        }

        if(databaseObject instanceof Species){
            moveToSpecies((Species) databaseObject);
            return;
        }

        if(!this.detailedViewMap.containsKey(databaseObject.getDbId())){
            this.moveToDatabaseObject = true;
            this.eventBus.fireELVEvent(ELVEventType.DATABASE_OBJECT_DETAILED_VIEW_REQUIRED, databaseObject.getDbId());
            return;
        }

        databaseObject = this.detailedViewMap.get(databaseObject.getDbId());
        this.moveToDatabaseObject(databaseObject);
    }

    @Override
    public void onMoleculesItemSelected(DatabaseObject databaseObject) {
        goToDatabaseObject(databaseObject);
    }

    @Override
    public void onDiagramEntitySelected(DatabaseObject databaseObject) {
        goToDatabaseObject(databaseObject);
    }

    @Override
    public void onOverviewEventSelected(Path path, Pathway pathway, Event event) {
        //Event detailed view is being loaded in the EVENT PANEL so species will be present
        //If there is more than one, for the moment we only take into account the first one
        cacheDetailedView(event);

//        this.path = path==null ? new LinkedList<Event>() : path.getPath();
//        this.diagram = pathway;
        goToDatabaseObject(event);
    }

    @Override
    public void onOverviewItemSelected(DatabaseObject databaseObject) {
//        this.cacheDetailedView(databaseObject);

        //Wen onOverviewEventSelected is back to work, please remove the following
        if(databaseObject instanceof Event && this.databaseObject instanceof Event){
            Event event = (Event) this.databaseObject;
            Event aux = (Event) databaseObject;
            if(!getSpecies(event).equals(getSpecies(aux))){
                moveToSpecies(getSpecies(aux));
                return;
            }
        }

        goToDatabaseObject(databaseObject);
    }

    @Override
    public void onStateManagerDatabaseObjectsSelected(List<Event> path, Pathway pathway, DatabaseObject databaseObject) {

        this.path = path;
        this.diagram = pathway;
        this.databaseObject = databaseObject;

        for (Event event : path) {
            if(!this.detailedViewMap.containsKey(event.getDbId())){
                this.eventBus.fireELVEvent(ELVEventType.DATABASE_OBJECT_DETAILED_VIEW_REQUIRED, event.getDbId());
            }
        }

        if (pathway!=null && !this.detailedViewMap.containsKey(pathway.getDbId())) {
            this.eventBus.fireELVEvent(ELVEventType.DATABASE_OBJECT_DETAILED_VIEW_REQUIRED, pathway.getDbId());
        }

        if (databaseObject!=null && !this.detailedViewMap.containsKey(databaseObject.getDbId())) {
            this.eventBus.fireELVEvent(ELVEventType.DATABASE_OBJECT_DETAILED_VIEW_REQUIRED, databaseObject.getDbId());
        }
    }

    @Override
    public void onStateManagerSpeciesSelected(Species species) {
        this.species = species;
    }

    @Override
    public void onTopBarSpeciesSelected(Species species) {
        moveToSpecies(species);
    }
}