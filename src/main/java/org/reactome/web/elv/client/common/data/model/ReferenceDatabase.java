package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.FactoryUtils;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public class ReferenceDatabase extends DatabaseObject {

    private String accessUrl;
    private List<String> name;
    private String url;

    public ReferenceDatabase(JSONObject jsonObject) {
        super(SchemaClass.REFERENCE_DATABASE, jsonObject);

        if(jsonObject.containsKey("accessUrl")){
            this.accessUrl = FactoryUtils.getStringValue(jsonObject, "accessUrl");
        }

        for (String object : FactoryUtils.getStringList(jsonObject, "name")) {
            this.name.add(object);
        }

        if(jsonObject.containsKey("url")){
            this.url = FactoryUtils.getStringValue(jsonObject, "url");
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
