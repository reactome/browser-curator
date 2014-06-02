package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class SequenceOntology extends ExternalOntology {

    public SequenceOntology(JSONObject jsonObject) {
        super(SchemaClass.SEQUENCE_ONTOLOGY, jsonObject);
    }
}
