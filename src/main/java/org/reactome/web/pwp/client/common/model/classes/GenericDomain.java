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
public class GenericDomain extends Domain {
    private List<DatabaseObject> hasInstance;

    public GenericDomain() {
        super(SchemaClass.GENERIC_DOMAIN);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        this.hasInstance = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "hasInstance")) {
            this.hasInstance.add(DatabaseObjectFactory.create(object));
        }
    }

    public List<DatabaseObject> getHasInstance() {
        return hasInstance;
    }
}
