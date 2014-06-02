package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.FactoryUtils;
import org.reactome.web.elv.client.common.data.factory.ModelFactory;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public class ReferenceIsoform extends ReferenceGeneProduct {
    private String variantIdentifier;
    private List<ReferenceGeneProduct> isoformParent = new LinkedList<ReferenceGeneProduct>();

    public ReferenceIsoform(JSONObject jsonObject) {
        super(SchemaClass.REFERENCE_ISOFORM, jsonObject);

        if(jsonObject.containsKey("variantIdentifier")){
            this.variantIdentifier = FactoryUtils.getStringValue(jsonObject, "variantIdentifier");
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "isoformParent")) {
            this.isoformParent.add((ReferenceGeneProduct) ModelFactory.getDatabaseObject(object));
        }
    }

    public String getVariantIdentifier() {
        return variantIdentifier;
    }

    public List<ReferenceGeneProduct> getIsoformParent() {
        return isoformParent;
    }
}
