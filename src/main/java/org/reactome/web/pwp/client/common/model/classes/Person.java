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
public class Person extends DatabaseObject {

    private List<Affiliation> affiliation;
    private String emailAddress;
    private String firstname;
    private String initial;
    private String project;
    private String surname;
    // A new attribute added in December, 2013
    private List<DatabaseIdentifier> crossReference;

    private List<Publication> publications;

    public Person() {
        super(SchemaClass.PERSON);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        this.affiliation = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "affiliation")) {
            this.affiliation.add((Affiliation) DatabaseObjectFactory.create(object));
        }

        if (jsonObject.containsKey("emailAddress")) {
            this.emailAddress = DatabaseObjectUtils.getStringValue(jsonObject, "emailAddress");
        }

        if (jsonObject.containsKey("firstname")) {
            this.firstname = DatabaseObjectUtils.getStringValue(jsonObject, "firstname");
        }

        if (jsonObject.containsKey("initial")) {
            this.initial = DatabaseObjectUtils.getStringValue(jsonObject, "initial");
        }

        if (jsonObject.containsKey("project")) {
            this.project = DatabaseObjectUtils.getStringValue(jsonObject, "project");
        }

        if (jsonObject.containsKey("surname")) {
            this.surname = DatabaseObjectUtils.getStringValue(jsonObject, "surname");
        }

        this.crossReference = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "crossReference")) {
            this.crossReference.add((DatabaseIdentifier) DatabaseObjectFactory.create(object));
        }
    }

    public List<Affiliation> getAffiliation() {
        return affiliation;
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

    public List<Publication> getPublications() {
        return publications;
    }

    public void setPublications(List<Publication> publications) {
        this.publications = publications;
    }
}
