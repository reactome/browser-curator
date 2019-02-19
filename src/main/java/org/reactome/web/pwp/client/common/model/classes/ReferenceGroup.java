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
public class ReferenceGroup extends ReferenceEntity {

    private String atomicConnectivity;
    private String formula;

    public ReferenceGroup() {
        super(SchemaClass.REFERENCE_GROUP);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        if (jsonObject.containsKey("atomicConnectivity")) {
            this.atomicConnectivity = DatabaseObjectUtils.getStringValue(jsonObject, "atomicConnectivity");
        }

        if (jsonObject.containsKey("formula")) {
            this.formula = DatabaseObjectUtils.getStringValue(jsonObject, "formula");
        }
    }

    public String getAtomicConnectivity() {
        return atomicConnectivity;
    }

    public String getFormula() {
        return formula;
    }

    @Override
    public ImageResource getImageResource() {
        return DatabaseObjectImages.INSTANCE.referenceGroup();
    }
}
