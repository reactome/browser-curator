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
@SuppressWarnings("unused")
public class EntityFunctionalStatus extends DatabaseObject {
    private List<FunctionalStatus> functionalStatus;
    private PhysicalEntity diseaseEntity;
    private PhysicalEntity normalEntity;

    public EntityFunctionalStatus() {
        super(SchemaClass.ENTITY_FUNCTIONAL_STATUS);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        this.functionalStatus = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "functionalStatus")) {
            this.functionalStatus.add((FunctionalStatus) DatabaseObjectFactory.create(object));
        }

        if (jsonObject.containsKey("diseaseEntity")) {
            this.diseaseEntity = DatabaseObjectUtils.getDatabaseObject(jsonObject, "diseaseEntity");
        }

        if (jsonObject.containsKey("normalEntity")) {
            this.normalEntity = DatabaseObjectUtils.getDatabaseObject(jsonObject, "normalEntity");
        }
    }

    public List<FunctionalStatus> getFunctionalStatus() {
        return functionalStatus;
    }

    public PhysicalEntity getDiseaseEntity() {
        return diseaseEntity;
    }

    public PhysicalEntity getNormalEntity() {
        return normalEntity;
    }
}
