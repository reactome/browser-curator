package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public class Compartment extends GO_CellularComponent {

    public Compartment(JSONObject jsonObject) {
        this(SchemaClass.COMPARTMENT, jsonObject);
    }

    public Compartment(SchemaClass schemaClass, JSONObject jsonObject) {
        super(schemaClass, jsonObject);
    }
}
