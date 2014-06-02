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
public abstract class ReactionLikeEvent extends Event {

    private Boolean isChimeric;
    private List<PhysicalEntity> inputs = new LinkedList<PhysicalEntity>();
    private List<PhysicalEntity> outputs = new LinkedList<PhysicalEntity>();
    private List<PhysicalEntity> entityOnOtherCell = new LinkedList<PhysicalEntity>();
    private List<DatabaseObject> requiredInputComponent = new LinkedList<DatabaseObject>();
    private ReactionLikeEvent hasMember;
    private List<CatalystActivity> catalystActivities = new LinkedList<CatalystActivity>();
    private List<ReactionLikeEvent> normalReaction = new LinkedList<ReactionLikeEvent>();
    private List<EntityFunctionalStatus> entityFunctionalStatus = new LinkedList<EntityFunctionalStatus>();

    public ReactionLikeEvent(SchemaClass schemaClass, JSONObject jsonObject) {
        super(schemaClass, jsonObject);

        if(jsonObject.containsKey("isChimeric")){
            this.isChimeric = FactoryUtils.getBooleanValue(jsonObject, "isChimeric");
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "input")) {
            this.inputs.add((PhysicalEntity) ModelFactory.getDatabaseObject(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "output")) {
            this.outputs.add((PhysicalEntity) ModelFactory.getDatabaseObject(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "entityOnOtherCell")) {
            this.entityOnOtherCell.add((PhysicalEntity) ModelFactory.getDatabaseObject(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "requiredInputComponent")) {
            this.requiredInputComponent.add(ModelFactory.getDatabaseObject(object));
        }

        if(jsonObject.containsKey("hasMember")){
            this.hasMember = (ReactionLikeEvent) FactoryUtils.getDatabaseObject(jsonObject, "hasMember");
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "catalystActivity")) {
            this.catalystActivities.add(new CatalystActivity(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "normalReaction")) {
            this.normalReaction.add((ReactionLikeEvent) ModelFactory.getDatabaseObject(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "entityFunctionalStatus")) {
            this.entityFunctionalStatus.add((EntityFunctionalStatus) ModelFactory.getDatabaseObject(object));
        }
    }

    public Boolean getChimeric() {
        return this.isChimeric;
    }

    public List<PhysicalEntity> getInputs() {
        return this.inputs;
    }

    public List<PhysicalEntity> getOutputs() {
        return this.outputs;
    }

    public List<PhysicalEntity> getEntityOnOtherCell() {
        return this.entityOnOtherCell;
    }

    public List<DatabaseObject> getRequiredInputComponent() {
        return this.requiredInputComponent;
    }

    public ReactionLikeEvent getHasMember() {
        return this.hasMember;
    }

    public List<CatalystActivity> getCatalystActivities() {
        return this.catalystActivities;
    }

    public List<ReactionLikeEvent> getNormalReaction() {
        return this.normalReaction;
    }

    public List<EntityFunctionalStatus> getEntityFunctionalStatus() {
        return entityFunctionalStatus;
    }
}
