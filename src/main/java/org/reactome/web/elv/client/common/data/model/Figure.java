package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.FactoryUtils;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public class Figure extends DatabaseObject {
    private String url;

    public Figure(JSONObject jsonObject) {
        super(SchemaClass.FIGURE, jsonObject);

        if(jsonObject.containsKey("url")){
            this.url = FactoryUtils.getStringValue(jsonObject, "url");
        }
    }

    public String getUrl() {
        return url;
    }
}
