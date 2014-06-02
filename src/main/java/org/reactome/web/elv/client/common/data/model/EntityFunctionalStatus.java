package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.FactoryUtils;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class EntityFunctionalStatus extends DatabaseObject {
    private List<FunctionalStatus> functionalStatus = new LinkedList<FunctionalStatus>();
    private PhysicalEntity physicalEntity;

    public EntityFunctionalStatus(JSONObject jsonObject) {
        super(SchemaClass.ENTITY_FUNCTIONAL_STATUS, jsonObject);

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "functionalStatus")) {
            this.functionalStatus.add(new FunctionalStatus(object));
        }

        if(jsonObject.containsKey("physicalEntity")){
            this.physicalEntity = (PhysicalEntity) FactoryUtils.getDatabaseObject(jsonObject, "physicalEntity");
        }
    }

    public List<FunctionalStatus> getFunctionalStatus() {
        return functionalStatus;
    }

    public PhysicalEntity getPhysicalEntity() {
        return physicalEntity;
    }
}
