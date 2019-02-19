package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class IntraChainCrosslinkedResidue extends CrosslinkedResidue {

    public IntraChainCrosslinkedResidue() {
        super(SchemaClass.INTRA_CHAIN_CROSSLINKED_RESIDUE);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);
    }
}
