package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class EntityCompartment extends Compartment {

    public EntityCompartment(JSONObject jsonObject) {
        this(SchemaClass.ENTITY_COMPARTMENT, jsonObject);
    }

    public EntityCompartment(SchemaClass schemaClass, JSONObject jsonObject) {
        super(schemaClass, jsonObject);
    }
}
