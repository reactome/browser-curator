package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectFactory;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectUtils;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public class Summation extends DatabaseObject {

    private String text;
    private List<Publication> literatureReference;

    public Summation() {
        super(SchemaClass.SUMMATION);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        if (jsonObject.containsKey("text")) {
            this.text = DatabaseObjectUtils.getStringValue(jsonObject, "text");
        }

        this.literatureReference = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "literatureReference")) {
            this.literatureReference.add((Publication) DatabaseObjectFactory.create(object));
        }
    }

    public String getText() {
        return text;
    }

    public List<Publication> getLiteratureReference() {
        return literatureReference;
    }
}
