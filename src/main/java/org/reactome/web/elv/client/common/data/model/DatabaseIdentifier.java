package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.FactoryUtils;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public class DatabaseIdentifier extends DatabaseObject {
    private String identifier;
    private List<DatabaseIdentifier> crossReference = new LinkedList<DatabaseIdentifier>();
    private ReferenceDatabase referenceDatabase;
    private String url; // Valid URL based on referenceDatabase

    public DatabaseIdentifier(JSONObject jsonObject) {
        super(SchemaClass.DATABASE_IDENTIFIER, jsonObject);

        if(jsonObject.containsKey("identifier")){
            this.identifier = FactoryUtils.getStringValue(jsonObject, "identifier");
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "crossReference")) {
            this.crossReference.add(new DatabaseIdentifier(object));
        }

        if(jsonObject.containsKey("referenceDatabase")){
            this.referenceDatabase = (ReferenceDatabase) FactoryUtils.getDatabaseObject(jsonObject, "referenceDatabase");
        }

        if(jsonObject.containsKey("url")){
            this.url = FactoryUtils.getStringValue(jsonObject, "url");
        }
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<DatabaseIdentifier> getCrossReference() {
        return crossReference;
    }

    public ReferenceDatabase getReferenceDatabase() {
        return referenceDatabase;
    }

    public String getUrl() {
        return url;
    }
}
