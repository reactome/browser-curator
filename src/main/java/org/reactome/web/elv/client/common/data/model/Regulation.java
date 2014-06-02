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
public class Regulation extends DatabaseObject {
//    private String authoredClass;
    private InstanceEdit authored;
    private List<InstanceEdit> edited = new LinkedList<InstanceEdit>();
    private List<Figure> figure = new LinkedList<Figure>();
    private List<Publication> literatureReference = new LinkedList<Publication>();
    private List<String> name = new LinkedList<String>();
    private DatabaseObject regulatedEntity;
    private RegulationType regulationType;
    private DatabaseObject regulator;
    private String releaseDate;
    private List<InstanceEdit> reviewed = new LinkedList<InstanceEdit>();
    private List<InstanceEdit> revised = new LinkedList<InstanceEdit>();
    private List<Summation> summation = new LinkedList<Summation>();
    // New attribute in December, 2013
    private List<Pathway> containedInPathway = new LinkedList<Pathway>();

    public Regulation(JSONObject jsonObject) {
        this(SchemaClass.REGULATION, jsonObject);
    }

    public Regulation(SchemaClass schemaClass, JSONObject jsonObject) {
        super(schemaClass, jsonObject);

        if(jsonObject.containsKey("authored")){
            this.authored = (InstanceEdit) FactoryUtils.getDatabaseObject(jsonObject, "authored");
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "edited")) {
            this.edited.add(new InstanceEdit(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "figure")) {
            this.figure.add(new Figure(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "literatureReference")) {
            this.literatureReference.add(new LiteratureReference(object));
        }

        for (String name : FactoryUtils.getStringList(jsonObject, "name")) {
            this.name.add(name);
        }

        if(jsonObject.containsKey("regulatedEntity")){
            this.regulatedEntity = FactoryUtils.getDatabaseObject(jsonObject, "regulatedEntity");
        }

        if(jsonObject.containsKey("regulationType")){
            this.regulationType = (RegulationType)FactoryUtils.getDatabaseObject(jsonObject, "regulationType");
        }

        if(jsonObject.containsKey("regulator")){
            this.regulator = FactoryUtils.getDatabaseObject(jsonObject, "regulator");
        }

        if(jsonObject.containsKey("releaseDate")){
            this.releaseDate = FactoryUtils.getStringValue(jsonObject, "releaseDate");
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "reviewed")) {
            this.reviewed.add(new InstanceEdit(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "revised")) {
            this.revised.add(new InstanceEdit(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "summation")) {
            this.summation.add(new Summation(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "containedInPathway")) {
            this.containedInPathway.add(new Pathway(object));
        }
    }

    public InstanceEdit getAuthored() {
        return authored;
    }

    public List<InstanceEdit> getEdited() {
        return edited;
    }

    public List<Figure> getFigure() {
        return figure;
    }

    public List<Publication> getLiteratureReference() {
        return literatureReference;
    }

    public List<String> getName() {
        return name;
    }

    public DatabaseObject getRegulatedEntity() {
        return regulatedEntity;
    }

    public RegulationType getRegulationType() {
        return regulationType;
    }

    public DatabaseObject getRegulator() {
        return regulator;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public List<InstanceEdit> getReviewed() {
        return reviewed;
    }

    public List<InstanceEdit> getRevised() {
        return revised;
    }

    public List<Summation> getSummation() {
        return summation;
    }

    public List<Pathway> getContainedInPathway() {
        return containedInPathway;
    }
}
