package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.FactoryUtils;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public class ReferenceGroup extends ReferenceEntity {

    private String atomicConnectivity;
    private String formula;

    public ReferenceGroup(JSONObject jsonObject) {
        super(SchemaClass.REFERENCE_GROUP, jsonObject);

        if(jsonObject.containsKey("atomicConnectivity")){
            this.atomicConnectivity = FactoryUtils.getStringValue(jsonObject, "atomicConnectivity");
        }

        if(jsonObject.containsKey("formula")){
            this.formula = FactoryUtils.getStringValue(jsonObject, "formula");
        }
    }

    public String getAtomicConnectivity() {
        return atomicConnectivity;
    }

    public String getFormula() {
        return formula;
    }
}
