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
public class Taxon extends DatabaseObject {

    private List<DatabaseIdentifier> crossReference = new LinkedList<DatabaseIdentifier>();
    private List<String> name = new LinkedList<String>();
    private Taxon superTaxon;

    public Taxon(JSONObject jsonObject) {
        this(SchemaClass.TAXON, jsonObject);
    }

    public Taxon(SchemaClass schemaClass, JSONObject jsonObject) {
        super(schemaClass, jsonObject);

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "crossReference")) {
            this.crossReference.add(new DatabaseIdentifier(object));
        }

        for (String name : FactoryUtils.getStringList(jsonObject, "name")) {
            this.name.add(name);
        }

        if(jsonObject.containsKey("superTaxon")){
            this.superTaxon = (Taxon) FactoryUtils.getDatabaseObject(jsonObject, "superTaxon");
        }
    }

    public List<DatabaseIdentifier> getCrossReference() {
        return crossReference;
    }

    public List<String> getName() {
        return name;
    }

    public Taxon getSuperTaxon() {
        return superTaxon;
    }
}
