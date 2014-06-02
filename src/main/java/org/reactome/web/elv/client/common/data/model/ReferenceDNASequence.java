package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ReferenceDNASequence extends ReferenceSequence {

    public ReferenceDNASequence(JSONObject jsonObject) {
        super(SchemaClass.REFERENCE_DNA_SEQUENCE, jsonObject);
    }
}
