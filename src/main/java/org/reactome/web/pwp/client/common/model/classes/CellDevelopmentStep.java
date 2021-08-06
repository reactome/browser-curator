package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.resources.client.ImageResource;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectUtils;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;
import org.reactome.web.pwp.client.common.model.images.DatabaseObjectImages;

/**
 * @author Joel Weiser (joel.weiser@oicr.on.ca)
 */
public class CellDevelopmentStep extends ReactionLikeEvent {
    private Anatomy tissue;

    public CellDevelopmentStep() {
        super(SchemaClass.CELL_DEVELOPMENT_STEP);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        if (jsonObject.containsKey("tissue")) {
            this.tissue = DatabaseObjectUtils.getDatabaseObject(jsonObject, "tissue");
        }
    }

    public Anatomy getTissue() {
        return this.tissue;
    }

    @Override
    public ImageResource getImageResource() {
        return DatabaseObjectImages.INSTANCE.cellDevelopmentStep();
    }
}
