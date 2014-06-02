package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class Requirement extends PositiveRegulation {

    public Requirement(JSONObject jsonObject) {
        super(SchemaClass.REQUIREMENT, jsonObject);
    }
}
