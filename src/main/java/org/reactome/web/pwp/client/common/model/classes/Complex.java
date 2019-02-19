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
public class Complex extends PhysicalEntity {

    private Boolean isChimeric;
    private String totalProt;
    private String maxHomologues;
    private String inferredProt;
    private List<PhysicalEntity> hasComponent;
    private List<PhysicalEntity> entityOnOthercell;
    private List<Compartment> includedLocation;
    private Boolean stoichiometryKnown;

    public Complex() {
        super(SchemaClass.COMPLEX);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        if (jsonObject.containsKey("isChimeric")) {
            this.isChimeric = DatabaseObjectUtils.getBooleanValue(jsonObject, "isChimeric");
        }

        if (jsonObject.containsKey("totalProt")) {
            this.totalProt = DatabaseObjectUtils.getStringValue(jsonObject, "totalProt");
        }

        if (jsonObject.containsKey("maxHomologues")) {
            this.maxHomologues = DatabaseObjectUtils.getStringValue(jsonObject, "maxHomologues");
        }

        if (jsonObject.containsKey("inferredProt")) {
            this.inferredProt = DatabaseObjectUtils.getStringValue(jsonObject, "inferredProt");
        }

        this.hasComponent = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "hasComponent")) {
            this.hasComponent.add((PhysicalEntity) DatabaseObjectFactory.create(object));
        }

        this.entityOnOthercell = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "entityOnOthercell")) {
            this.entityOnOthercell.add((PhysicalEntity) DatabaseObjectFactory.create(object));
        }

        this.includedLocation = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "includedLocation")) {
            this.includedLocation.add((Compartment) DatabaseObjectFactory.create(object));
        }

        if (jsonObject.containsKey("stoichiometryKnown")){
            this.stoichiometryKnown = DatabaseObjectUtils.getBooleanValue(jsonObject, "stoichiometryKnown");
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

    public List<Compartment> getIncludedLocation() {
        return includedLocation;
    }

    public Boolean getStoichiometryKnown() {
        return stoichiometryKnown;
    }

    @Override
    public ImageResource getImageResource() {
        return DatabaseObjectImages.INSTANCE.complex();
    }
}
