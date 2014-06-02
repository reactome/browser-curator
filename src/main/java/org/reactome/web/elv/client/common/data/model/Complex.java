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
public class Complex extends PhysicalEntity {

    private Boolean isChimeric;
    private String totalProt;
    private String maxHomologues;
    private String inferredProt;
    private List<PhysicalEntity> hasComponent = new LinkedList<PhysicalEntity>();
    private List<PhysicalEntity> entityOnOthercell = new LinkedList<PhysicalEntity>();
    private List<EntityCompartment> includedLocation = new LinkedList<EntityCompartment>();

    public Complex(JSONObject jsonObject) {
        super(SchemaClass.COMPLEX, jsonObject);

        if(jsonObject.containsKey("isChimeric")){
            this.isChimeric = FactoryUtils.getBooleanValue(jsonObject, "isChimeric");
        }

        if(jsonObject.containsKey("totalProt")){
            this.totalProt = FactoryUtils.getStringValue(jsonObject, "totalProt");
        }

        if(jsonObject.containsKey("maxHomologues")){
            this.maxHomologues = FactoryUtils.getStringValue(jsonObject, "maxHomologues");
        }

        if(jsonObject.containsKey("inferredProt")){
            this.inferredProt = FactoryUtils.getStringValue(jsonObject, "inferredProt");
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "hasComponent")) {
            this.hasComponent.add((PhysicalEntity) ModelFactory.getDatabaseObject(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "entityOnOthercell")) {
            this.entityOnOthercell.add((PhysicalEntity) ModelFactory.getDatabaseObject(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "includedLocation")) {
            this.includedLocation.add(new EntityCompartment(object));
        }
    }

    public Boolean getChimeric() {
        return isChimeric;
    }

    public String getTotalProt() {
        return totalProt;
    }

    public String getMaxHomologues() {
        return maxHomologues;
    }

    public String getInferredProt() {
        return inferredProt;
    }

    public List<PhysicalEntity> getHasComponent() {
        return hasComponent;
    }

    public List<PhysicalEntity> getEntityOnOthercell() {
        return entityOnOthercell;
    }

    public List<EntityCompartment> getIncludedLocation() {
        return includedLocation;
    }
}
