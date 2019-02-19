package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectFactory;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectUtils;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class PhysicalEntity extends DatabaseObject {
    private InstanceEdit authored;
    private String definition;
    private GO_CellularComponent goCellularComponent;
    private String shortName;
    private List<org.reactome.web.pwp.client.common.model.classes.PhysicalEntity> inferredFrom;
    private List<org.reactome.web.pwp.client.common.model.classes.PhysicalEntity> inferredTo;
    private List<Figure> figure;
    private List<Summation> summation;
    private List<InstanceEdit> edited;
    private List<InstanceEdit> reviewed;
    private List<InstanceEdit> revised;
    private List<String> name;
    private List<Compartment> compartment;
    private List<DatabaseIdentifier> crossReference;
    private List<Disease> disease;
    private List<Publication> literatureReference;
    // The following properties are used for detailed view
    private List<Event> catalyzedEvent; // List of Events catalysed by this PE
    private List<GO_MolecularFunction> goActivity; // List of GO MF related to this PE via CatalystActivity
    private List<Event> inhibitedEvent;
    private List<Event> activatedEvent;
    private List<Event> requiredEvent;
    private List<Event> producedByEvent;
    private List<Event> consumedByEvent;
    // Next one does NOT appear in the RESTFul Service Model definition
    private List<Species> species;

    public PhysicalEntity(SchemaClass schemaClass){
        super(schemaClass);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        if (jsonObject.containsKey("authored")) {
            this.authored = DatabaseObjectUtils.getDatabaseObject(jsonObject, "authored");
        }

        if (jsonObject.containsKey("definition")) {
            this.definition = DatabaseObjectUtils.getStringValue(jsonObject, "definition");
        }

        if (jsonObject.containsKey("goCellularComponent")) {
            this.goCellularComponent = DatabaseObjectUtils.getDatabaseObject(jsonObject, "goCellularComponent");
        }

        if (jsonObject.containsKey("shortName")) {
            this.shortName = DatabaseObjectUtils.getStringValue(jsonObject, "shortName");
        }

        this.inferredFrom = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "inferredFrom")) {
            this.inferredFrom.add((org.reactome.web.pwp.client.common.model.classes.PhysicalEntity) DatabaseObjectFactory.create(object));
        }

        this.inferredTo = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "inferredTo")) {
            /**
             * Since Species has been added to PhysicalEntities, the inferredTo list contains something like
             *
             * "inferredTo": [{
             *      "EntityWithAccessionedSequence": {
             *          "dbId": 2676791,
             *          "displayName": "Protease/caspase cleaved UNC5B fragment bound to PM [plasma membrane]",
             *          "schemaClass": "EntityWithAccessionedSequence"
             *       },
             *       ..
             * ]
             *
             * And the next bit of code tries to deal with that situation
             * TODO: Remove condition when retrieved data comes with the expected format!!
             */
            if (object.keySet().size() == 1) {
                for (String s : object.keySet()) {
                    this.inferredTo.add((org.reactome.web.pwp.client.common.model.classes.PhysicalEntity) DatabaseObjectFactory.create(object.get(s).isObject()));
                }
            } else {
                this.inferredTo.add((org.reactome.web.pwp.client.common.model.classes.PhysicalEntity) DatabaseObjectFactory.create(object));
            }
        }

        this.figure = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "figure")) {
            this.figure.add((Figure) DatabaseObjectFactory.create(object));
        }

        this.summation = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "summation")) {
            this.summation.add((Summation) DatabaseObjectFactory.create(object));
        }

        this.edited = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "edited")) {
            this.edited.add((InstanceEdit) DatabaseObjectFactory.create(object));
        }

        this.reviewed = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "reviewed")) {
            this.reviewed.add((InstanceEdit) DatabaseObjectFactory.create(object));
        }

        this.revised = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "revised")) {
            this.revised.add((InstanceEdit) DatabaseObjectFactory.create(object));
        }

        this.name = new LinkedList<>();
        for (String object : DatabaseObjectUtils.getStringList(jsonObject, "name")) {
            this.name.add(object);
        }

        this.compartment = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "compartment")) {
            this.compartment.add((Compartment) DatabaseObjectFactory.create(object));
        }

        this.crossReference = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "crossReference")) {
            this.crossReference.add((DatabaseIdentifier) DatabaseObjectFactory.create(object));
        }

        this.disease = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "disease")) {
            this.disease.add((Disease) DatabaseObjectFactory.create(object));
        }

        this.literatureReference = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "literatureReference")) {
            this.literatureReference.add((Publication) DatabaseObjectFactory.create(object));
        }

        // The following properties are used for detailed view
        this.catalyzedEvent = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "catalyzedEvent")) {
            this.catalyzedEvent.add((Event) DatabaseObjectFactory.create(object));
        }

        this.goActivity = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "goActivity")) {
            this.goActivity.add((GO_MolecularFunction) DatabaseObjectFactory.create(object));
        }

        this.inhibitedEvent = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "inhibitedEvent")) {
            this.inhibitedEvent.add((Event) DatabaseObjectFactory.create(object));
        }

        this.activatedEvent = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "activatedEvent")) {
            this.activatedEvent.add((Event) DatabaseObjectFactory.create(object));
        }

        this.requiredEvent = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "requiredEvent")) {
            this.requiredEvent.add((Event) DatabaseObjectFactory.create(object));
        }

        this.producedByEvent = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "producedByEvent")) {
            this.producedByEvent.add((Event) DatabaseObjectFactory.create(object));
        }

        this.consumedByEvent = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "consumedByEvent")) {
            this.consumedByEvent.add((Event) DatabaseObjectFactory.create(object));
        }

        this.species = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "species")) {
            this.species.add((Species) DatabaseObjectFactory.create(object));
        }
    }

    public InstanceEdit getAuthored() {
        return authored;
    }

    public String getDefinition() {
        return definition;
    }

    public GO_CellularComponent getGoCellularComponent() {
        return goCellularComponent;
    }

    public String getShortName() {
        return shortName;
    }

    public List<org.reactome.web.pwp.client.common.model.classes.PhysicalEntity> getInferredFrom() {
        return inferredFrom;
    }

    public List<org.reactome.web.pwp.client.common.model.classes.PhysicalEntity> getInferredTo() {
        return inferredTo;
    }

    public List<Figure> getFigure() {
        return figure;
    }

    public List<Summation> getSummation() {
        return summation;
    }

    public List<InstanceEdit> getEdited() {
        return edited;
    }

    public List<InstanceEdit> getReviewed() {
        return reviewed;
    }

    public List<InstanceEdit> getRevised() {
        return revised;
    }

    public List<String> getName() {
        return name;
    }

    public List<Compartment> getCompartment() {
        return compartment;
    }

    public List<DatabaseIdentifier> getCrossReference() {
        return crossReference;
    }

    public List<Disease> getDisease() {
        return disease;
    }

    public List<Publication> getLiteratureReference() {
        return literatureReference;
    }

    public List<Event> getCatalyzedEvent() {
        return catalyzedEvent;
    }

    public List<GO_MolecularFunction> getGoActivity() {
        return goActivity;
    }

    public List<Event> getInhibitedEvent() {
        return inhibitedEvent;
    }

    public List<Event> getActivatedEvent() {
        return activatedEvent;
    }

    public List<Event> getRequiredEvent() {
        return requiredEvent;
    }

    public List<Event> getProducedByEvent() {
        return producedByEvent;
    }

    public List<Event> getConsumedByEvent() {
        return consumedByEvent;
    }

    public List<Species> getSpecies() {
        return species;
    }
}