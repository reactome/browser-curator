package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class NegativeRegulation extends Regulation {

    public NegativeRegulation(JSONObject jsonObject) {
        super(SchemaClass.NEGATIVE_REGULATION, jsonObject);
    }
}
