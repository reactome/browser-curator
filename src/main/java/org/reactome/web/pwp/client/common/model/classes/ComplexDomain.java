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
public class ComplexDomain extends Domain {
    private List<DatabaseObject> hasComponent;

    public ComplexDomain() {
        super(SchemaClass.COMPLEX_DOMAIN);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        this.hasComponent = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "hasComponent")) {
            this.hasComponent.add(DatabaseObjectFactory.create(object));
        }
    }

    public List<DatabaseObject> getHasComponent() {
        return hasComponent;
    }
}
