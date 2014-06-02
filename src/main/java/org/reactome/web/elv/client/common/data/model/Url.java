package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.FactoryUtils;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public class Url extends Publication {
    private String uniformResourceLocator;

    public Url(JSONObject jsonObject) {
        super(SchemaClass.URL, jsonObject);

        if(jsonObject.containsKey("uniformResourceLocator")){
            this.uniformResourceLocator = FactoryUtils.getStringValue(jsonObject, "uniformResourceLocator");
        }
    }

    public String getUniformResourceLocator() {
        return uniformResourceLocator;
    }
}
