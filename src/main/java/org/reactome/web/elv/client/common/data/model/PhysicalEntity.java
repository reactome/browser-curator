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
public abstract class PhysicalEntity extends DatabaseObject {
    private InstanceEdit authored;
    private String definition;
    private GO_CellularComponent goCellularComponent;
    private String shortName;
    private List<PhysicalEntity> inferredFrom = new LinkedList<PhysicalEntity>();
    private List<PhysicalEntity> inferredTo = new LinkedList<PhysicalEntity>();
    private List<Figure> figure = new LinkedList<Figure>();
    private List<Summation> summation = new LinkedList<Summation>();
    private List<InstanceEdit> edited = new LinkedList<InstanceEdit>();
    private List<InstanceEdit> reviewed = new LinkedList<InstanceEdit>();
    private List<InstanceEdit> revised = new LinkedList<InstanceEdit>();
    private List<String> name = new LinkedList<String>();
    private List<EntityCompartment> compartment = new LinkedList<EntityCompartment>();
    private List<DatabaseIdentifier> crossReference = new LinkedList<DatabaseIdentifier>();
    private List<Disease> disease = new LinkedList<Disease>();
    private List<Publication> literatureReference = new LinkedList<Publication>();
    // The following properties are used for detailed view
    private List<Event> catalyzedEvent = new LinkedList<Event>(); // List of Events catalysed by this PE
    private List<GO_MolecularFunction> goActivity = new LinkedList<GO_MolecularFunction>(); // List of GO MF related to this PE via CatalystActivity
    private List<Event> inhibitedEvent = new LinkedList<Event>();
    private List<Event> activatedEvent = new LinkedList<Event>();
    private List<Event> requiredEvent = new LinkedList<Event>();
    private List<Event> producedByEvent = new LinkedList<Event>();
    private List<Event> consumedByEvent = new LinkedList<Event>();
    // Next one does NOT appear in the RESTFul Service Model definition
    private List<Species> species = new LinkedList<Species>();

    public PhysicalEntity(SchemaClass schemaClass, JSONObject jsonObject) {
        super(schemaClass, jsonObject);

//        System.out.println(getClass() + " -> " + jsonObject.toString());

        if(jsonObject.containsKey("authored")){
            this.authored = (InstanceEdit) FactoryUtils.getDatabaseObject(jsonObject, "authored");
        }

        if(jsonObject.containsKey("definition")){
            this.definition = FactoryUtils.getStringValue(jsonObject, "definition");
        }

        if(jsonObject.containsKey("goCellularComponent")){
            this.goCellularComponent = (GO_CellularComponent) FactoryUtils.getDatabaseObject(jsonObject, "goCellularComponent");
        }

        if(jsonObject.containsKey("shortName")){
            this.shortName = FactoryUtils.getStringValue(jsonObject, "shortName");
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "inferredFrom")) {
            this.inferredFrom.add((PhysicalEntity) ModelFactory.getDatabaseObject(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "inferredTo")) {
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
            if(object.keySet().size()==1){
                for (String s : object.keySet()) {
                    this.inferredTo.add((PhysicalEntity) ModelFactory.getDatabaseObject(object.get(s).isObject()));
                }
            }else{
                this.inferredTo.add((PhysicalEntity) ModelFactory.getDatabaseObject(object));
            }
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "figure")) {
            this.figure.add(new Figure(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "summation")) {
            this.summation.add(new Summation(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "edited")) {
            this.edited.add(new InstanceEdit(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "reviewed")) {
            this.reviewed.add(new InstanceEdit(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "revised")) {
            this.revised.add(new InstanceEdit(object));
        }

        for (String object : FactoryUtils.getStringList(jsonObject, "name")) {
            this.name.add(object);
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "compartment")) {
            this.compartment.add(new EntityCompartment(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "crossReference")) {
            this.crossReference.add(new DatabaseIdentifier(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "disease")) {
            this.disease.add(new Disease(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "literatureReference")) {
            this.literatureReference.add((Publication) ModelFactory.getDatabaseObject(object));
        }

        // The following properties are used for detailed view
        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "catalyzedEvent")) {
            this.catalyzedEvent.add((Event) ModelFactory.getDatabaseObject(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "goActivity")) {
            this.goActivity.add(new GO_MolecularFunction(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "inhibitedEvent")) {
            this.inhibitedEvent.add((Event) ModelFactory.getDatabaseObject(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "activatedEvent")) {
            this.activatedEvent.add((Event) ModelFactory.getDatabaseObject(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "requiredEvent")) {
            this.requiredEvent.add((Event) ModelFactory.getDatabaseObject(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "producedByEvent")) {
            this.producedByEvent.add((Event) ModelFactory.getDatabaseObject(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "consumedByEvent")) {
            this.consumedByEvent.add((Event) ModelFactory.getDatabaseObject(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "species")) {
            this.species.add(new Species(object));
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

    public List<PhysicalEntity> getInferredFrom() {
        return inferredFrom;
    }

    public List<PhysicalEntity> getInferredTo() {
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

    public List<EntityCompartment> getCompartment() {
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