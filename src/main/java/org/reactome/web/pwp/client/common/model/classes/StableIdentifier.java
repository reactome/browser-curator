package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectUtils;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public class StableIdentifier extends DatabaseObject {
    private String identifier;
    private String identifierVersion;
    private ReferenceDatabase referenceDatabase;
    private String referenceDatabaseClass;

    public StableIdentifier() {
        super(SchemaClass.STABLE_IDENTIFIER);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        if (jsonObject.containsKey("identifier")) {
            this.identifier = DatabaseObjectUtils.getStringValue(jsonObject, "identifier");
        }

        if (jsonObject.containsKey("identifierVersion")) {
            this.identifierVersion = DatabaseObjectUtils.getStringValue(jsonObject, "identifierVersion");
        }

        if (jsonObject.containsKey("referenceDatabase")) {
            this.referenceDatabase = DatabaseObjectUtils.getDatabaseObject(jsonObject, "referenceDatabase");
        }

        if (jsonObject.containsKey("referenceDatabaseClass")) {
            this.referenceDatabaseClass = DatabaseObjectUtils.getStringValue(jsonObject, "referenceDatabaseClass");
        }
    }

    public String getIdentifier() {
        return identifier != null ? identifier : getDisplayName().split("\\.")[0];
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
