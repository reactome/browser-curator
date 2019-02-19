package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectUtils;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public class Figure extends DatabaseObject {
    private String url;

    public Figure() {
        super(SchemaClass.FIGURE);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        if (jsonObject.containsKey("url")) {
            this.url = DatabaseObjectUtils.getStringValue(jsonObject, "url");
        }
    }

    public String getUrl() {
        return url;
    }
}
