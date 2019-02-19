package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.resources.client.ImageResource;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectFactory;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectUtils;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;
import org.reactome.web.pwp.client.common.model.images.DatabaseObjectImages;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class Event extends DatabaseObject {

    private List<InstanceEdit> authored;
    private List<InstanceEdit> edited;
    private List<InstanceEdit> revised;
    private List<InstanceEdit> reviewed;
    private List<Species> species;
    private String definition;
    private EvidenceType evidenceType;
    private GO_BiologicalProcess goBiologicalProcess;
    private String keywords;
    private List<Summation> summation;
    private EventStatus releaseStatus;
    private List<Figure> figure;
    private List<org.reactome.web.pwp.client.common.model.classes.Event> precedingEvent;
    private List<org.reactome.web.pwp.client.common.model.classes.Event> followingEvent;
    private List<Publication> literatureReference;
    private List<DatabaseObject> requirements;
    private List<DatabaseIdentifier> crossReference;
    private List<Disease> disease;
    // A simple label to indicate if this Event object is a disease
    private Boolean isInDisease;
    private List<org.reactome.web.pwp.client.common.model.classes.Event> inferredFrom;
    // A simple flag to indicate if this Event is inferred from another
    private Boolean isInferred;
    private List<String> name;
    private List<org.reactome.web.pwp.client.common.model.classes.Event> orthologousEvent;
    private List<Compartment> compartment;

    public Event(SchemaClass schemaClass) {
        super(schemaClass);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        this.authored  = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "authored")) {
            this.authored.add((InstanceEdit) DatabaseObjectFactory.create(object));
        }

        this.edited  = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "edited")) {
            this.edited.add((InstanceEdit) DatabaseObjectFactory.create(object));
        }

        this.revised = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "revised")) {
            this.revised.add((InstanceEdit) DatabaseObjectFactory.create(object));
        }

        this.reviewed = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "reviewed")) {
            this.reviewed.add((InstanceEdit) DatabaseObjectFactory.create(object));
        }

        this.species = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "species")) {
            this.species.add((Species) DatabaseObjectFactory.create(object));
        }

        if (jsonObject.containsKey("definition")) {
            this.definition = DatabaseObjectUtils.getStringValue(jsonObject, "definition");
        }

        if (jsonObject.containsKey("evidenceType")) {
            this.evidenceType = DatabaseObjectUtils.getDatabaseObject(jsonObject, "evidenceType");
        }

        if (jsonObject.containsKey("goBiologicalProcess")) {
            this.goBiologicalProcess = DatabaseObjectUtils.getDatabaseObject(jsonObject, "goBiologicalProcess");
        }

        if (jsonObject.containsKey("keywords")) {
            this.keywords = DatabaseObjectUtils.getStringValue(jsonObject, "keywords");
        }

        this.summation = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "summation")) {
            this.summation.add((Summation) DatabaseObjectFactory.create(object));
        }

        if (jsonObject.containsKey("releaseStatus")) {
            String status = DatabaseObjectUtils.getStringValue(jsonObject, "releaseStatus");
            this.releaseStatus = EventStatus.getEventStatus(status);
        } else {
            this.releaseStatus = EventStatus.REGULAR;
        }

        this.figure = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "figure")) {
            this.figure.add((Figure) DatabaseObjectFactory.create(object));
        }

        this.precedingEvent = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "precedingEvent")) {
            this.precedingEvent.add((org.reactome.web.pwp.client.common.model.classes.Event) DatabaseObjectFactory.create(object));
        }

        this.followingEvent = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "followingEvent")) {
            this.followingEvent.add((org.reactome.web.pwp.client.common.model.classes.Event) DatabaseObjectFactory.create(object));
        }

        this.literatureReference = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "literatureReference")) {
            this.literatureReference.add((Publication) DatabaseObjectFactory.create(object));
        }

        this.requirements = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "requirements")) {
            this.requirements.add(DatabaseObjectFactory.create(object));
        }

        this.crossReference = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "crossReference")) {
            this.crossReference.add((DatabaseIdentifier) DatabaseObjectFactory.create(object));
        }

        this.disease = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "disease")) {
            this.disease.add((Disease) DatabaseObjectFactory.create(object));
        }

        if (jsonObject.containsKey("isInDisease")) {
            this.isInDisease = DatabaseObjectUtils.getBooleanValue(jsonObject, "isInDisease");
        }

        this.inferredFrom = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "inferredFrom")) {
            this.inferredFrom.add((org.reactome.web.pwp.client.common.model.classes.Event) DatabaseObjectFactory.create(object));
        }

        if (jsonObject.containsKey("isInferred")) {
            this.isInferred = DatabaseObjectUtils.getBooleanValue(jsonObject, "isInferred");
        }

        this.name  = new LinkedList<>();
        this.name.addAll(DatabaseObjectUtils.getStringList(jsonObject, "name"));

        this.orthologousEvent = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "orthologousEvent")) {
            this.orthologousEvent.add((org.reactome.web.pwp.client.common.model.classes.Event) DatabaseObjectFactory.create(object));
        }

        this.compartment = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "compartment")) {
            DatabaseObject databaseObject = DatabaseObjectFactory.create(object);
            //Sometimes the RESTFul service is retrieving GO_CellularComponent in the compartment -> BUG?
            if (databaseObject instanceof Compartment) {
                this.compartment.add((Compartment) databaseObject);
            }
        }
    }

    public ImageResource getStatusIcon() {
        switch (getReleaseStatus()) {
            case NEW:       return DatabaseObjectImages.INSTANCE.isNew();
            case UPDATED:   return DatabaseObjectImages.INSTANCE.isUpdated();
            case REGULAR:
            default:        return null;
        }
    }

    public ImageResource getDiseaseIcon(){
        if(isInDisease){
            return DatabaseObjectImages.INSTANCE.isDisease();
        }
        return null;
    }

    public ImageResource getInferredIcon(){
        if(isInferred){
            return DatabaseObjectImages.INSTANCE.isInferred();
        }
        return null;
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

    public List<org.reactome.web.pwp.client.common.model.classes.Event> getPrecedingEvent() {
        return precedingEvent;
    }

    public List<org.reactome.web.pwp.client.common.model.classes.Event> getFollowingEvent() {
        return followingEvent;
    }

    public List<Publication> getLiteratureReference() {
        return literatureReference;
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

    public List<org.reactome.web.pwp.client.common.model.classes.Event> getInferredFrom() {
        return inferredFrom;
    }

    public Boolean isInferred() {
        return isInferred;
    }

    public List<String> getName() {
        return name;
    }

    public List<org.reactome.web.pwp.client.common.model.classes.Event> getOrthologousEvent() {
        return orthologousEvent;
    }

    public List<Compartment> getCompartment() {
        return compartment;
    }
}
