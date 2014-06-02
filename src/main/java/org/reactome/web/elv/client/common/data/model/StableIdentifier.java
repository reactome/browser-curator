package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.FactoryUtils;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 *
 */
@SuppressWarnings("UnusedDeclaration")
public class StableIdentifier extends DatabaseObject {
    private String identifier;
    private String identifierVersion;
    private ReferenceDatabase referenceDatabase;
    private String referenceDatabaseClass;

    public StableIdentifier(JSONObject jsonObject) {
        super(SchemaClass.STABLE_IDENTIFIER, jsonObject);

        if(jsonObject.containsKey("identifier")){
            this.identifier = FactoryUtils.getStringValue(jsonObject, "identifier");
        }

        if(jsonObject.containsKey("identifierVersion")){
            this.identifierVersion = FactoryUtils.getStringValue(jsonObject, "identifierVersion");
        }

        if(jsonObject.containsKey("referenceDatabase")){
            this.referenceDatabase = (ReferenceDatabase) FactoryUtils.getDatabaseObject(jsonObject, "referenceDatabase");
        }

        if(jsonObject.containsKey("referenceDatabaseClass")){
            this.referenceDatabaseClass = FactoryUtils.getStringValue(jsonObject, "referenceDatabaseClass");
        }
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getIdentifierVersion() {
        return identifierVersion;
    }

    public ReferenceDatabase getReferenceDatabase() {
        return referenceDatabase;
    }

    public String getReferenceDatabaseClass() {
        return referenceDatabaseClass;
    }
}
