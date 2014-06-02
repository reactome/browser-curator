package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DefinedSet extends EntitySet {

    public DefinedSet(JSONObject jsonObject) {
        super(SchemaClass.DEFINED_SET, jsonObject);
    }
}
