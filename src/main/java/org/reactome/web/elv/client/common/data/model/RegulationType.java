package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.FactoryUtils;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public class RegulationType extends DatabaseObject {
    private String value;

    public RegulationType(JSONObject jsonObject) {
        super(SchemaClass.REGULATION_TYPE, jsonObject);

        if(jsonObject.containsKey("value")){
            this.value = FactoryUtils.getStringValue(jsonObject, "value");
        }
    }

    public String getValue() {
        return value;
    }
}
