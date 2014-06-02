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
@SuppressWarnings("UnusedDeclaration")
public class ComplexDomain extends Domain {
    private List<DatabaseObject> hasComponent = new LinkedList<DatabaseObject>();

    public ComplexDomain(JSONObject jsonObject) {
        super(SchemaClass.COMPLEX_DOMAIN, jsonObject);

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "hasComponent")) {
            this.hasComponent.add(ModelFactory.getDatabaseObject(object));
        }
    }

    public List<DatabaseObject> getHasComponent() {
        return hasComponent;
    }
}
