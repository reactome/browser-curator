package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectUtils;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public abstract class Drug extends PhysicalEntity {

    private ReferenceTherapeutic referenceEntity;

    Drug(SchemaClass schemaClass) {
        super(schemaClass);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        if (jsonObject.containsKey("referenceEntity")) {
            this.referenceEntity = DatabaseObjectUtils.getDatabaseObject(jsonObject, "referenceEntity");
        }
    }

    public ReferenceTherapeutic getReferenceEntity() {
        return referenceEntity;
    }

}
