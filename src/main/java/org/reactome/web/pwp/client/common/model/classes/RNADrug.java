package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.resources.client.ImageResource;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;
import org.reactome.web.pwp.client.common.model.images.DatabaseObjectImages;

public class RNADrug extends Drug {

    public RNADrug() {
        super(SchemaClass.RNA_DRUG);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);
    }

    @Override
    public ImageResource getImageResource() {
        return DatabaseObjectImages.INSTANCE.rnaSequenceDrug();
    }
}
