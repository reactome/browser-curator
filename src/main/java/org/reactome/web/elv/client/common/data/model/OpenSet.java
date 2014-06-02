package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.FactoryUtils;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public class OpenSet extends EntitySet {

    private ReferenceEntity referenceEntity;

    public OpenSet(JSONObject jsonObject) {
        super(SchemaClass.OPEN_SET, jsonObject);

        if(jsonObject.containsKey("evidenceType")){
            this.referenceEntity = (ReferenceEntity) FactoryUtils.getDatabaseObject(jsonObject, "evidenceType");
        }
    }

    public ReferenceEntity getReferenceEntity() {
        return referenceEntity;
    }
}
