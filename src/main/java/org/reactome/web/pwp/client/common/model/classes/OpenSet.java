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
public class OpenSet extends EntitySet {

    private ReferenceEntity referenceEntity;

    public OpenSet() {
        super(SchemaClass.OPEN_SET);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        if (jsonObject.containsKey("evidenceType")) {
            this.referenceEntity = DatabaseObjectUtils.getDatabaseObject(jsonObject, "evidenceType");
        }
    }

    public ReferenceEntity getReferenceEntity() {
        return referenceEntity;
    }

    @Override
    public ImageResource getImageResource() {
        return DatabaseObjectImages.INSTANCE.openSet();
    }
}
