package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectUtils;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("unused")
public class FunctionalStatusType extends DatabaseObject {
    private String definition;
    private List<String> name;

    public FunctionalStatusType() {
        super(SchemaClass.FUNCTIONAL_STATUS_TYPE);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        if (jsonObject.containsKey("definition")) {
            this.definition = DatabaseObjectUtils.getStringValue(jsonObject, "definition");
        }

        this.name = new LinkedList<>();
        for (String name : DatabaseObjectUtils.getStringList(jsonObject, "name")) {
            this.name.add(name);
        }
    }

    public String getDefinition() {
        return definition;
    }

    public List<String> getName() {
        return name;
    }
}
