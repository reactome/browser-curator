package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectUtils;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class AbstractModifiedResidue extends DatabaseObject {

    private ReferenceSequence referenceSequence;

    public AbstractModifiedResidue(SchemaClass schemaClass){
        super(schemaClass);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        if (jsonObject.containsKey("referenceSequence")) {
            this.referenceSequence = DatabaseObjectUtils.getDatabaseObject(jsonObject, "referenceSequence");
        }
    }

    public ReferenceSequence getReferenceSequence() {
        return referenceSequence;
    }
}
