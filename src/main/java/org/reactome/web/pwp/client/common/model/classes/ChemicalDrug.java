package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.resources.client.ImageResource;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;
import org.reactome.web.pwp.client.common.model.images.DatabaseObjectImages;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ChemicalDrug extends Drug {

    public ChemicalDrug() {
        super(SchemaClass.CHEMICAL_DRUG);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);
    }


    @Override
    public ImageResource getImageResource() {
        return DatabaseObjectImages.INSTANCE.chemicalDrug();
    }
}

