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
public class InstanceEdit extends DatabaseObject {
    private String applyToAllEditedInstances;
    private String dateTime;
    private String note;
    private List<Person> author;

    public InstanceEdit() {
        super(SchemaClass.INSTANCE_EDIT);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        if (jsonObject.containsKey("applyToAllEditedInstances")) {
            this.applyToAllEditedInstances = DatabaseObjectUtils.getStringValue(jsonObject, "applyToAllEditedInstances");
        }

        if (jsonObject.containsKey("dateTime")) {
            this.dateTime = DatabaseObjectUtils.getStringValue(jsonObject, "dateTime");
        }

        if (jsonObject.containsKey("note")) {
            this.note = DatabaseObjectUtils.getStringValue(jsonObject, "note");
        }

        this.author = new LinkedList<>();
        for (JSONObject author : DatabaseObjectUtils.getObjectList(jsonObject, "author")) {
            this.author.add((Person) DatabaseObjectFactory.create(author));
        }
    }

    public String getApplyToAllEditedInstances() {
        return applyToAllEditedInstances;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getNote() {
        return note;
    }

    public List<Person> getAuthor() {
        return author;
    }
}
