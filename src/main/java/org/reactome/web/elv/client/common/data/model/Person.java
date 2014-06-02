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
public class Person extends DatabaseObject {

    private String emailAddress;
    private String firstname;
    private String initial;
    private String project;
    private String surname;
    // A new attribute added in December, 2013
    private List<DatabaseIdentifier> crossReference = new LinkedList<DatabaseIdentifier>();

    public Person(JSONObject jsonObject) {
        super(SchemaClass.PERSON, jsonObject);

        if(jsonObject.containsKey("emailAddress")){
            this.emailAddress = FactoryUtils.getStringValue(jsonObject, "emailAddress");
        }

        if(jsonObject.containsKey("firstname")){
            this.firstname = FactoryUtils.getStringValue(jsonObject, "firstname");
        }

        if(jsonObject.containsKey("initial")){
            this.initial = FactoryUtils.getStringValue(jsonObject, "initial");
        }

        if(jsonObject.containsKey("project")){
            this.project = FactoryUtils.getStringValue(jsonObject, "project");
        }

        if(jsonObject.containsKey("surname")){
            this.surname = FactoryUtils.getStringValue(jsonObject, "surname");
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "crossReference")) {
            this.crossReference.add(new DatabaseIdentifier(object));
        }
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getInitial() {
        return initial;
    }

    public String getProject() {
        return project;
    }

    public String getSurname() {
        return surname;
    }

    public List<DatabaseIdentifier> getCrossReference() {
        return crossReference;
    }
}
