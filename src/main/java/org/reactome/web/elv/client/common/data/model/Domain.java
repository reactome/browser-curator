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
public abstract class Domain extends DatabaseObject {

    private List<String> name = new LinkedList<String>();

    public Domain(SchemaClass schemaClass, JSONObject jsonObject) {
        super(schemaClass, jsonObject);

        for (String name : FactoryUtils.getStringList(jsonObject, "name")) {
            this.name.add(name);
        }
    }

    public List<String> getName() {
        return name;
    }
}
