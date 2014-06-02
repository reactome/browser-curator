package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class FragmentDeletionModification extends FragmentModification {

    public FragmentDeletionModification(JSONObject jsonObject) {
        super(SchemaClass.FRAGMENT_DELETION_MODIFICATION, jsonObject);
    }
}
