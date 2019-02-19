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
public class CatalystActivity extends DatabaseObject implements Regulator {
    private GO_MolecularFunction activity;
    private PhysicalEntity physicalEntity;
    private String physicalEntityClass;
    private List<PhysicalEntity> activeUnit;

    public CatalystActivity() {
        super(SchemaClass.CATALYST_ACTIVITY);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);
        if (jsonObject.containsKey("activity")) {
            this.activity = DatabaseObjectUtils.getDatabaseObject(jsonObject, "activity");
        }

        if (jsonObject.containsKey("physicalEntity")) {
            this.physicalEntity = DatabaseObjectUtils.getDatabaseObject(jsonObject, "physicalEntity");
        }

        if (jsonObject.containsKey("physicalEntityClass")) {
            this.physicalEntityClass = DatabaseObjectUtils.getStringValue(jsonObject, "physicalEntityClass");
        }

        this.activeUnit = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "activeUnit")) {
            this.activeUnit.add((PhysicalEntity) DatabaseObjectFactory.create(object));
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

    public List<PhysicalEntity> getActiveUnit() {
        return activeUnit;
    }
}
