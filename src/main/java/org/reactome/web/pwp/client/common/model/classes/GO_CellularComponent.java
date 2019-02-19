package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectUtils;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public class GO_CellularComponent extends DatabaseObject {
    private String accession;
    private String definition;
    private ReferenceDatabase referenceDatabase;
    private String referenceDatabaseClass;

    public GO_CellularComponent() {
        this(SchemaClass.GO_CELLULAR_COMPONENT);
    }

    public GO_CellularComponent(SchemaClass schemaClass){
        super(schemaClass);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        if (jsonObject.containsKey("accession")) {
            this.accession = DatabaseObjectUtils.getStringValue(jsonObject, "accession");
        }

        if (jsonObject.containsKey("definition")) {
            this.definition = DatabaseObjectUtils.getStringValue(jsonObject, "definition");
        }

        if (jsonObject.containsKey("referenceDatabase")) {
            this.referenceDatabase = DatabaseObjectUtils.getDatabaseObject(jsonObject, "referenceDatabase");
        }

        if (jsonObject.containsKey("referenceDatabaseClass")) {
            this.referenceDatabaseClass = DatabaseObjectUtils.getStringValue(jsonObject, "referenceDatabaseClass");
        }
    }

    public String getAccession() {
        return accession;
    }

    public String getDefinition() {
        return definition;
    }

    public ReferenceDatabase getReferenceDatabase() {
        return referenceDatabase;
    }

    public String getReferenceDatabaseClass() {
        return referenceDatabaseClass;
    }
}
