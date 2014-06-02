package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.FactoryUtils;
import org.reactome.web.elv.client.common.data.factory.ModelFactory;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 *
 */
@SuppressWarnings("UnusedDeclaration")
public class Summation extends DatabaseObject {

    private String text;
    private List<Publication> literatureReference = new LinkedList<Publication>();

    public Summation(JSONObject jsonObject) {
        super(SchemaClass.SUMMATION, jsonObject);

        if(jsonObject.containsKey("text")){
            this.text = FactoryUtils.getStringValue(jsonObject, "text");
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "literatureReference")) {
            this.literatureReference.add((Publication) ModelFactory.getDatabaseObject(object));
        }
    }

    public String getText() {
        return text;
    }

    public List<Publication> getLiteratureReference() {
        return literatureReference;
    }
}
