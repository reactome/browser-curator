package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public abstract class GeneticallyModifiedResidue extends AbstractModifiedResidue {

    public GeneticallyModifiedResidue(SchemaClass schemaClass, JSONObject jsonObject) {
        super(schemaClass, jsonObject);
    }
}
