package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.FactoryUtils;
import org.reactome.web.elv.client.common.data.factory.ModelFactory;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public abstract class ExternalOntology extends DatabaseObject {
    private String definition;
    private String identifier;
    private List<ExternalOntology> instanceOf = new LinkedList<ExternalOntology>();
    private List<String> name = new LinkedList<String>();
    private ReferenceDatabase referenceDatabase;
    private List<String> synonym = new LinkedList<String>();

    public ExternalOntology(SchemaClass schemaClass, JSONObject jsonObject) {
        super(schemaClass, jsonObject);

        if(jsonObject.containsKey("definition")){
            this.definition = FactoryUtils.getStringValue(jsonObject, "definition");
        }

        if(jsonObject.containsKey("identifier")){
            this.identifier = FactoryUtils.getStringValue(jsonObject, "identifier");
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "instanceOf")) {
            this.instanceOf.add((ExternalOntology) ModelFactory.getDatabaseObject(object));
        }

        for (String name : FactoryUtils.getStringList(jsonObject, "name")) {
            this.name.add(name);
        }

        if(jsonObject.containsKey("referenceDatabase")){
            this.referenceDatabase = (ReferenceDatabase) FactoryUtils.getDatabaseObject(jsonObject, "referenceDatabase");
        }

        for (String synonym : FactoryUtils.getStringList(jsonObject, "synonym")) {
            this.synonym.add(synonym);
        }
    }

    public String getDefinition() {
        return definition;
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<ExternalOntology> getInstanceOf() {
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
