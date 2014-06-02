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
public class GenericDomain extends Domain {
    private List<DatabaseObject> hasInstance = new LinkedList<DatabaseObject>();

    public GenericDomain(JSONObject jsonObject) {
        super(SchemaClass.GENERIC_DOMAIN, jsonObject);

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "hasInstance")) {
            this.hasInstance.add(ModelFactory.getDatabaseObject(object));
        }
    }

    public List<DatabaseObject> getHasInstance() {
        return hasInstance;
    }
}
