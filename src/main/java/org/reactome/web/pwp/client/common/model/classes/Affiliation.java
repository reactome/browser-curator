package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectUtils;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("unused")
public class Affiliation extends DatabaseObject {

    private String address;
    private String name;

    public Affiliation() {
        super(SchemaClass.AFFILIATION);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        if (jsonObject.containsKey("address")) {
            this.address = DatabaseObjectUtils.getStringValue(jsonObject, "address");
        }

        if (jsonObject.containsKey("name")) {
            this.name = DatabaseObjectUtils.getStringValue(jsonObject, "name");
        }
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }
}
