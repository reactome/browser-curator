package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.FactoryUtils;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class AbstractModifiedResidue extends DatabaseObject{

    private ReferenceSequence referenceSequence;

    public AbstractModifiedResidue(SchemaClass schemaClass, JSONObject jsonObject) {
        super(schemaClass, jsonObject);

        if(jsonObject.containsKey("referenceSequence")){
            this.referenceSequence = (ReferenceSequence) FactoryUtils.getDatabaseObject(jsonObject, "referenceSequence");
        }
    }

    public ReferenceSequence getReferenceSequence() {
        return referenceSequence;
    }
}
