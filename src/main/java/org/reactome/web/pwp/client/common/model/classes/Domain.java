package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectUtils;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class Domain extends DatabaseObject {

    private List<String> name;

    public Domain(SchemaClass schemaClass){
        super(schemaClass);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        this.name = new LinkedList<>();
        for (String name : DatabaseObjectUtils.getStringList(jsonObject, "name")) {
            this.name.add(name);
        }
    }

    public List<String> getName() {
        return name;
    }
}
