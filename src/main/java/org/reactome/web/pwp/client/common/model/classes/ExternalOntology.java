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
@SuppressWarnings("unused")
public abstract class ExternalOntology extends DatabaseObject {
    private String definition;
    private String identifier;
    private List<org.reactome.web.pwp.client.common.model.classes.ExternalOntology> instanceOf;
    private List<String> name;
    private ReferenceDatabase referenceDatabase;
    private List<String> synonym;

    public ExternalOntology(SchemaClass schemaClass) {
        super(schemaClass);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        if (jsonObject.containsKey("definition")) {
            this.definition = DatabaseObjectUtils.getStringValue(jsonObject, "definition");
        }

        if (jsonObject.containsKey("identifier")) {
            this.identifier = DatabaseObjectUtils.getStringValue(jsonObject, "identifier");
        }

        this.instanceOf = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "instanceOf")) {
            this.instanceOf.add((org.reactome.web.pwp.client.common.model.classes.ExternalOntology) DatabaseObjectFactory.create(object));
        }

        this.name = new LinkedList<>();
        for (String name : DatabaseObjectUtils.getStringList(jsonObject, "name")) {
            this.name.add(name);
        }

        if (jsonObject.containsKey("referenceDatabase")) {
            this.referenceDatabase = DatabaseObjectUtils.getDatabaseObject(jsonObject, "referenceDatabase");
        }

        this.synonym = new LinkedList<>();
        for (String synonym : DatabaseObjectUtils.getStringList(jsonObject, "synonym")) {
            this.synonym.add(synonym);
        }
    }

    public String getDefinition() {
        return definition;
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<org.reactome.web.pwp.client.common.model.classes.ExternalOntology> getInstanceOf() {
        return instanceOf;
    }

    public List<String> getName() {
        return name;
    }

    public ReferenceDatabase getReferenceDatabase() {
        return referenceDatabase;
    }

    public List<String> getSynonym() {
        return synonym;
    }
}
