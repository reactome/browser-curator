package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.resources.client.ImageResource;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectUtils;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;
import org.reactome.web.pwp.client.common.model.images.DatabaseObjectImages;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public class Reaction extends ReactionLikeEvent {

    private org.reactome.web.pwp.client.common.model.classes.Reaction reverseReaction;
    private String totalProt;
    private String maxHomologues;
    private String inferredProt;

    public Reaction() {
        super(SchemaClass.REACTION);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        if (jsonObject.containsKey("reverseReaction")) {
            this.reverseReaction = DatabaseObjectUtils.getDatabaseObject(jsonObject, "reverseReaction");
        }

        if (jsonObject.containsKey("totalProt")) {
            this.totalProt = DatabaseObjectUtils.getStringValue(jsonObject, "totalProt");
        }

        if (jsonObject.containsKey("maxHomologues")) {
            this.maxHomologues = DatabaseObjectUtils.getStringValue(jsonObject, "maxHomologues");
        }

        if (jsonObject.containsKey("inferredProt")) {
            this.inferredProt = DatabaseObjectUtils.getStringValue(jsonObject, "inferredProt");
        }
    }

    @Override
    public ImageResource getImageResource() {
        return DatabaseObjectImages.INSTANCE.reaction();
    }

    public org.reactome.web.pwp.client.common.model.classes.Reaction getReverseReaction() {
        return reverseReaction;
    }

    public String getTotalProt() {
        return totalProt;
    }

    public String getMaxHomologues() {
        return maxHomologues;
    }

    public String getInferredProt() {
        return inferredProt;
    }

}
