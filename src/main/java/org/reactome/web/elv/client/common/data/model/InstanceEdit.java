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
public class InstanceEdit extends DatabaseObject {
    private String applyToAllEditedInstances;
    private String dateTime;
    private String note;
    private List<Person> author = new LinkedList<Person>();

    public InstanceEdit(JSONObject jsonObject) {
        super(SchemaClass.INSTANCE_EDIT, jsonObject);

        if(jsonObject.containsKey("applyToAllEditedInstances")){
            this.applyToAllEditedInstances = FactoryUtils.getStringValue(jsonObject, "applyToAllEditedInstances");
        }

        if(jsonObject.containsKey("dateTime")){
            this.dateTime = FactoryUtils.getStringValue(jsonObject, "dateTime");
        }

        if(jsonObject.containsKey("note")){
            this.note = FactoryUtils.getStringValue(jsonObject, "note");
        }

        for (JSONObject author : FactoryUtils.getObjectList(jsonObject, "author")) {
            this.author.add(new Person(author));
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
