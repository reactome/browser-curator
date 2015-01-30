package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.FactoryUtils;
import org.reactome.web.elv.client.common.data.factory.ModelFactory;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class Event extends DatabaseObject {

    private List<InstanceEdit> authored = new LinkedList<InstanceEdit>();
    private List<InstanceEdit> edited = new LinkedList<InstanceEdit>();
    private List<InstanceEdit> revised = new LinkedList<InstanceEdit>();
    private List<InstanceEdit> reviewed = new LinkedList<InstanceEdit>();
    private List<Species> species = new LinkedList<Species>();
    private String definition;
    private EvidenceType evidenceType;
    private GO_BiologicalProcess goBiologicalProcess;
    private String keywords;
    private List<Summation> summation = new LinkedList<Summation>();
    private EventStatus releaseStatus;
    private List<Figure> figure = new LinkedList<Figure>();
    private List<Event> precedingEvent = new LinkedList<Event>();
    private List<Event> followingEvent = new LinkedList<Event>();
    private List<Publication> literatureReference = new LinkedList<Publication>();
    // Regulation related attributes
    private List<DatabaseObject> negativeRegulators = new LinkedList<DatabaseObject>(); // PhysicalEntity, Event, CatalystActivity fit in here
    // Regulators in PositiveRegulations but not Requirements.
    // Note: Requirement is a subclass to PositiveRegulation.
    private List<DatabaseObject> positiveRegulators = new LinkedList<DatabaseObject>(); // PhysicalEntity, Event, CatalystActivity fit in here
    private List<DatabaseObject> requirements = new LinkedList<DatabaseObject>();
    private List<DatabaseIdentifier> crossReference = new LinkedList<DatabaseIdentifier>();
    private List<Disease> disease = new LinkedList<Disease>();
    // A simple label to indicate if this Event object is a disease
    private Boolean isInDisease;
    private List<Event> inferredFrom = new LinkedList<Event>();
    // A simple flag to indicate if this Event is inferred from another
    private Boolean isInferred;
    private List<String> name = new LinkedList<String>();
    private List<Event> orthologousEvent = new LinkedList<Event>();
    private List<Compartment> compartment = new LinkedList<Compartment>();


    public Event(Long dbId, String displayName, SchemaClass schemaClass) {
        super(dbId, displayName, schemaClass);
    }

    public Event(SchemaClass schemaClass, JSONObject jsonObject) {
        super(schemaClass, jsonObject);

        //System.out.println(getClass() + " -> " + jsonObject.toString());

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "authored")) {
            this.authored.add(new InstanceEdit(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "edited")) {
            this.edited.add(new InstanceEdit(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "revised")) {
            this.revised.add(new InstanceEdit(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "reviewed")) {
            this.reviewed.add(new InstanceEdit(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "species")) {
            this.species.add(new Species(object));
        }

        if(jsonObject.containsKey("definition")){
            this.definition = FactoryUtils.getStringValue(jsonObject, "definition");
        }

        if(jsonObject.containsKey("evidenceType")){
            this.evidenceType = (EvidenceType) FactoryUtils.getDatabaseObject(jsonObject, "evidenceType");
        }

        if(jsonObject.containsKey("goBiologicalProcess")){
            this.goBiologicalProcess = (GO_BiologicalProcess) FactoryUtils.getDatabaseObject(jsonObject, "goBiologicalProcess");
        }

        if(jsonObject.containsKey("keywords")){
            this.keywords = FactoryUtils.getStringValue(jsonObject, "keywords");
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "summation")) {
            this.summation.add(new Summation(object));
        }

        if(jsonObject.containsKey("releaseStatus")){
            String status = FactoryUtils.getStringValue(jsonObject, "releaseStatus");
            this.releaseStatus = EventStatus.getEventStatus(status);
        }else{
            this.releaseStatus = EventStatus.REGULAR;
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "figure")) {
            this.figure.add(new Figure(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "precedingEvent")) {
            this.precedingEvent.add((Event) ModelFactory.getDatabaseObject(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "followingEvent")) {
            this.followingEvent.add((Event) ModelFactory.getDatabaseObject(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "literatureReference")) {
            this.literatureReference.add((Publication) ModelFactory.getDatabaseObject(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "negativeRegulators")) {
            this.negativeRegulators.add(ModelFactory.getDatabaseObject(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "positiveRegulators")) {
            this.positiveRegulators.add(ModelFactory.getDatabaseObject(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "requirements")) {
            this.requirements.add(ModelFactory.getDatabaseObject(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "crossReference")) {
            this.crossReference.add(new DatabaseIdentifier(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "disease")) {
            this.disease.add(new Disease(object));
        }

        if(jsonObject.containsKey("isInDisease")){
            this.isInDisease = FactoryUtils.getBooleanValue(jsonObject, "isInDisease");
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "inferredFrom")) {
            this.inferredFrom.add((Event) ModelFactory.getDatabaseObject(object));
        }

        if(jsonObject.containsKey("isInferred")){
            this.isInferred = FactoryUtils.getBooleanValue(jsonObject, "isInferred");
        }

        for (String name : FactoryUtils.getStringList(jsonObject, "name")) {
            this.name.add(name);
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "orthologousEvent")) {
            this.orthologousEvent.add((Event) ModelFactory.getDatabaseObject(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "compartment")) {
            DatabaseObject databaseObject = ModelFactory.getDatabaseObject(object);
            //Sometimes the RESTFul service is retrieving GO_CellularComponent in the compartment -> BUG?
            if(databaseObject instanceof Compartment){
                this.compartment.add((Compartment) databaseObject);
            }
        }
    }

    public List<InstanceEdit> getAuthored() {
        return authored;
    }

    public List<InstanceEdit> getEdited() {
        return edited;
    }

    public List<InstanceEdit> getRevised() {
        return revised;
    }

    public List<InstanceEdit> getReviewed() {
        return reviewed;
    }

    public List<Species> getSpecies() {
        return species;
    }

    public String getDefinition() {
        return definition;
    }

    public EvidenceType getEvidenceType() {
        return evidenceType;
    }

    public GO_BiologicalProcess getGoBiologicalProcess() {
        return goBiologicalProcess;
    }

    public String getKeywords() {
        return keywords;
    }

    public List<Summation> getSummation() {
        return summation;
    }

    public EventStatus getReleaseStatus() {
        return releaseStatus;
    }

    public List<Figure> getFigure() {
        return figure;
    }

    public List<Event> getPrecedingEvent() {
        return precedingEvent;
    }

    public List<Event> getFollowingEvent() {
        return followingEvent;
    }

    public List<Publication> getLiteratureReference() {
        return literatureReference;
    }

    public List<DatabaseObject> getNegativeRegulators() {
        return negativeRegulators;
    }

    public List<DatabaseObject> getPositiveRegulators() {
        return positiveRegulators;
    }

    public List<DatabaseObject> getRequirements() {
        return requirements;
    }

    public List<DatabaseIdentifier> getCrossReference() {
        return crossReference;
    }

    public List<Disease> getDisease() {
        return disease;
    }

    public Boolean isInDisease() {
        return isInDisease;
    }

    public List<Event> getInferredFrom() {
        return inferredFrom;
    }

    public Boolean isInferred() {
        return isInferred;
    }

    public List<String> getName() {
        return name;
    }

    public List<Event> getOrthologousEvent() {
        return orthologousEvent;
    }

    public List<Compartment> getCompartment() {
        return compartment;
    }
}
