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
public abstract class Publication extends DatabaseObject {

    private String title;
    private List<Person> authors;

    public Publication(SchemaClass schemaClass){
        super(schemaClass);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        if (jsonObject.containsKey("title")) {
            this.title = DatabaseObjectUtils.getStringValue(jsonObject, "title");
        }

        this.authors = new LinkedList<>();
        for (JSONObject author : DatabaseObjectUtils.getObjectList(jsonObject, "author")) {
            this.authors.add((Person) DatabaseObjectFactory.create(author));
        }
    }

    public String getTitle() {
        return title;
    }

    public List<Person> getAuthors() {
        return authors;
    }
}
