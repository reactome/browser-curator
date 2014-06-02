package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.FactoryUtils;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public class EvidenceType extends DatabaseObject {

    private String definition;

    public EvidenceType(JSONObject jsonObject) {
        super(SchemaClass.EVIDENCE_TYPE, jsonObject);

        if(jsonObject.containsKey("definition")){
            this.definition = FactoryUtils.getStringValue(jsonObject, "definition");
        }
    }

    public String getDefinition() {
        return definition;
    }
}
