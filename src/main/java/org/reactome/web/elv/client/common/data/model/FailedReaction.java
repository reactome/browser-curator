package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class FailedReaction extends ReactionLikeEvent {

    public FailedReaction(JSONObject jsonObject) {
        super(SchemaClass.FAILED_REACTION, jsonObject);
    }

}
