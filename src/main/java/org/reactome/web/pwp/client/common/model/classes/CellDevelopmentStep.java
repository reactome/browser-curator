package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.resources.client.ImageResource;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectUtils;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;
import org.reactome.web.pwp.client.common.model.images.DatabaseObjectImages;

public class CellDevelopmentStep extends ReactionLikeEvent {
    private String tissue;
    private String tissueClass;

    public CellDevelopmentStep() {
        super(SchemaClass.CELL_DEVELOPMENT_STEP);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        if (jsonObject.containsKey("tissue")) {
            this.tissue = DatabaseObjectUtils.getStringValue(jsonObject, "tissue");
        }

        if (jsonObject.containsKey("tissueClass")) {
            this.tissueClass = DatabaseObjectUtils.getStringValue(jsonObject, "tissueClass");
        }
    }

    public String getTissue() {
        return this.tissue;
    }

    public String getTissueClass() {
        return this.tissueClass;
    }

    @Override
    public ImageResource getImageResource() {
        return DatabaseObjectImages.INSTANCE.cellDevelopmentStep();
    }
}
