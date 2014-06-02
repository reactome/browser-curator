package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class PositiveRegulation extends Regulation {

    public PositiveRegulation(JSONObject jsonObject) {
        this(SchemaClass.POSITIVE_REGULATION, jsonObject);
    }

    public PositiveRegulation(SchemaClass schemaClass, JSONObject jsonObject) {
        super(schemaClass, jsonObject);
    }
}
