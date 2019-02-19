package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectUtils;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public class EvidenceType extends DatabaseObject {

    private String definition;

    public EvidenceType() {
        super(SchemaClass.EVIDENCE_TYPE);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        if (jsonObject.containsKey("definition")) {
            this.definition = DatabaseObjectUtils.getStringValue(jsonObject, "definition");
        }
    }

    public String getDefinition() {
        return definition;
    }
}
