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
public abstract class ReferenceEntity extends DatabaseObject {

    private List<DatabaseIdentifier> crossReference = new LinkedList<DatabaseIdentifier>();
    private String identifier;
    private List<String> name = new LinkedList<String>();
    private List<String> otherIdentifier = new LinkedList<String>();
    private ReferenceDatabase referenceDatabase;

    public ReferenceEntity(SchemaClass schemaClass, JSONObject jsonObject) {
        super(schemaClass, jsonObject);

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "crossReference")) {
            this.crossReference.add(new DatabaseIdentifier(object));
        }

        if(jsonObject.containsKey("identifier")){
            this.identifier = FactoryUtils.getStringValue(jsonObject, "identifier");
        }

        for (String name : FactoryUtils.getStringList(jsonObject, "name")) {
            this.name.add(name);
        }

        for (String name : FactoryUtils.getStringList(jsonObject, "otherIdentifier")) {
            this.otherIdentifier.add(name);
        }

        if(jsonObject.containsKey("referenceDatabase")){
            this.referenceDatabase = (ReferenceDatabase) FactoryUtils.getDatabaseObject(jsonObject, "referenceDatabase");
        }
    }

    public List<DatabaseIdentifier> getCrossReference() {
        return crossReference;
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<String> getName() {
        return name;
    }

    public List<String> getOtherIdentifier() {
        return otherIdentifier;
    }

    public ReferenceDatabase getReferenceDatabase() {
        return referenceDatabase;
    }
}
