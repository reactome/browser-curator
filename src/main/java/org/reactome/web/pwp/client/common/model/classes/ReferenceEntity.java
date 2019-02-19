package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectFactory;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectUtils;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class ReferenceEntity extends DatabaseObject {

    private List<DatabaseIdentifier> crossReference;
    private String identifier;
    private List<String> name;
    private List<String> otherIdentifier;
    private ReferenceDatabase referenceDatabase;

    public ReferenceEntity(SchemaClass schemaClass){
        super(schemaClass);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        this.crossReference = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "crossReference")) {
            this.crossReference.add((DatabaseIdentifier) DatabaseObjectFactory.create(object));
        }

        if (jsonObject.containsKey("identifier")) {
            this.identifier = DatabaseObjectUtils.getStringValue(jsonObject, "identifier");
        }

        this.name = new LinkedList<>();
        for (String name : DatabaseObjectUtils.getStringList(jsonObject, "name")) {
            this.name.add(name);
        }

        this.otherIdentifier = new LinkedList<>();
        for (String name : DatabaseObjectUtils.getStringList(jsonObject, "otherIdentifier")) {
            this.otherIdentifier.add(name);
        }

        if (jsonObject.containsKey("referenceDatabase")) {
            this.referenceDatabase = DatabaseObjectUtils.getDatabaseObject(jsonObject, "referenceDatabase");
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
