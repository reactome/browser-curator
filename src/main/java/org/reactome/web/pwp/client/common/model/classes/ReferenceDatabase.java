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
public class ReferenceDatabase extends DatabaseObject {

    private String accessUrl;
    private List<String> name;
    private String url;

    public ReferenceDatabase() {
        super(SchemaClass.REFERENCE_DATABASE);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        if (jsonObject.containsKey("accessUrl")) {
            this.accessUrl = DatabaseObjectUtils.getStringValue(jsonObject, "accessUrl");
        }

        this.name = new LinkedList<>();
        for (String object : DatabaseObjectUtils.getStringList(jsonObject, "name")) {
            this.name.add(object);
        }

        if (jsonObject.containsKey("url")) {
            this.url = DatabaseObjectUtils.getStringValue(jsonObject, "url");
        }
    }

    public String getAccessUrl() {
        return accessUrl;
    }

    public List<String> getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
