package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.FactoryUtils;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public class CatalystActivity extends DatabaseObject implements Regulator {
    private GO_MolecularFunction activity;
    private PhysicalEntity physicalEntity;
    private String physicalEntityClass;

    public CatalystActivity(JSONObject jsonObject) {
        super(SchemaClass.CATALYST_ACTIVITY, jsonObject);

        if(jsonObject.containsKey("activity")){
            this.activity = (GO_MolecularFunction) FactoryUtils.getDatabaseObject(jsonObject, "activity");
        }

        if(jsonObject.containsKey("physicalEntity")){
            this.physicalEntity = (PhysicalEntity) FactoryUtils.getDatabaseObject(jsonObject, "physicalEntity");
        }

        if(jsonObject.containsKey("physicalEntityClass")){
            this.physicalEntityClass = FactoryUtils.getStringValue(jsonObject, "physicalEntityClass");
        }
    }

    public GO_MolecularFunction getActivity() {
        return activity;
    }

    public PhysicalEntity getPhysicalEntity() {
        return physicalEntity;
    }

    public String getPhysicalEntityClass() {
        return physicalEntityClass;
    }
}
