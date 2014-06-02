package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ModifiedResidue extends TranslationalModification {

    public ModifiedResidue(JSONObject jsonObject) {
        super(SchemaClass.MODIFIED_RESIDUE, jsonObject);
    }
}
