package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.FactoryUtils;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class FunctionalStatusType extends DatabaseObject {
    private String definition;
    private List<String> name = new LinkedList<String>();

    public FunctionalStatusType(JSONObject jsonObject) {
        super(SchemaClass.FUNCTIONAL_STATUS_TYPE, jsonObject);

        if(jsonObject.containsKey("definition")){
            this.definition = FactoryUtils.getStringValue(jsonObject, "definition");
        }

        for (String name : FactoryUtils.getStringList(jsonObject, "name")) {
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
