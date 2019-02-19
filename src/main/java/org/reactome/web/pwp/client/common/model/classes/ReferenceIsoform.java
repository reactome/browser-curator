package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectFactory;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectUtils;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public class ReferenceIsoform extends ReferenceGeneProduct {
    private String variantIdentifier;
    private List<ReferenceGeneProduct> isoformParent;

    public ReferenceIsoform() {
        super(SchemaClass.REFERENCE_ISOFORM);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        if (jsonObject.containsKey("variantIdentifier")) {
            this.variantIdentifier = DatabaseObjectUtils.getStringValue(jsonObject, "variantIdentifier");
        }

        this.isoformParent = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "isoformParent")) {
            this.isoformParent.add((ReferenceGeneProduct) DatabaseObjectFactory.create(object));
        }
    }

    public String getVariantIdentifier() {
        return variantIdentifier;
    }

    public List<ReferenceGeneProduct> getIsoformParent() {
        return isoformParent;
    }
}
