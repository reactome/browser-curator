package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class NegativeGeneExpressionRegulation extends NegativeRegulation {

    public NegativeGeneExpressionRegulation() {
        super(SchemaClass.NEGATIVE_GENE_EXPRESSION_REGULATION);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);
    }
}
