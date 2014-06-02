package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.FactoryUtils;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class FunctionalStatus extends DatabaseObject {
    private FunctionalStatusType functionalStatusType;
    private SequenceOntology structuralVariant;

    public FunctionalStatus(JSONObject jsonObject) {
        super(SchemaClass.FUNCTIONAL_STATUS, jsonObject);

        if(jsonObject.containsKey("functionalStatusType")){
            this.functionalStatusType = (FunctionalStatusType) FactoryUtils.getDatabaseObject(jsonObject, "functionalStatusType");
        }

        if(jsonObject.containsKey("structuralVariant")){
            this.structuralVariant = (SequenceOntology) FactoryUtils.getDatabaseObject(jsonObject, "structuralVariant");
        }
    }

    public FunctionalStatusType getFunctionalStatusType() {
        return functionalStatusType;
    }

    public SequenceOntology getStructuralVariant() {
        return structuralVariant;
    }
}
