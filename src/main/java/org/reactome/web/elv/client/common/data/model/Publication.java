package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.FactoryUtils;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class Publication extends DatabaseObject {

    private String title;
    private List<Person> authors = new LinkedList<Person>();

    public Publication(SchemaClass schemaClass, JSONObject jsonObject) {
        super(schemaClass, jsonObject);

        if(jsonObject.containsKey("title")){
            this.title = FactoryUtils.getStringValue(jsonObject, "title");
        }

        for (JSONObject author : FactoryUtils.getObjectList(jsonObject, "author")) {
            this.authors.add(new Person(author));
        }
    }

    public String getTitle() {
        return title;
    }

    public List<Person> getAuthors() {
        return authors;
    }
}
