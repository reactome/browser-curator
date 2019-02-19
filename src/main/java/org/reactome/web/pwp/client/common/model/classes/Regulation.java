package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.resources.client.ImageResource;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectFactory;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectUtils;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;
import org.reactome.web.pwp.client.common.model.images.DatabaseObjectImages;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public class Regulation extends DatabaseObject {
    //    private String authoredClass;
    private InstanceEdit authored;
    private List<InstanceEdit> edited;
    private List<Figure> figure;
    private List<Publication> literatureReference;
    private List<String> name;
    private DatabaseObject regulatedEntity;
    private RegulationType regulationType;
    private DatabaseObject regulator;
    private String releaseDate;
    private List<InstanceEdit> reviewed;
    private List<InstanceEdit> revised;
    private List<Summation> summation;
    // New attribute in December, 2013
    private List<Pathway> containedInPathway;
    private GO_BiologicalProcess goBiologicalProcess;

    public Regulation() {
        this(SchemaClass.REGULATION);
    }

    public Regulation(SchemaClass schemaClass){
        super(schemaClass);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        if (jsonObject.containsKey("authored")) {
            this.authored = DatabaseObjectUtils.getDatabaseObject(jsonObject, "authored");
        }

        this.edited = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "edited")) {
            this.edited.add((InstanceEdit) DatabaseObjectFactory.create(object));
        }

        this.figure = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "figure")) {
            this.figure.add((Figure) DatabaseObjectFactory.create(object));
        }

        this.literatureReference = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "literatureReference")) {
            this.literatureReference.add((Publication) DatabaseObjectFactory.create(object));
        }

        this.name = new LinkedList<>();
        for (String name : DatabaseObjectUtils.getStringList(jsonObject, "name")) {
            this.name.add(name);
        }

        if (jsonObject.containsKey("regulatedEntity")) {
            this.regulatedEntity = DatabaseObjectUtils.getDatabaseObject(jsonObject, "regulatedEntity");
        }

        if (jsonObject.containsKey("regulationType")) {
            this.regulationType = DatabaseObjectUtils.getDatabaseObject(jsonObject, "regulationType");
        }

        if (jsonObject.containsKey("regulator")) {
            this.regulator = DatabaseObjectUtils.getDatabaseObject(jsonObject, "regulator");
        }

        if (jsonObject.containsKey("releaseDate")) {
            this.releaseDate = DatabaseObjectUtils.getStringValue(jsonObject, "releaseDate");
        }

        this.reviewed = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "reviewed")) {
            this.reviewed.add((InstanceEdit) DatabaseObjectFactory.create(object));
        }

        this.revised = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "revised")) {
            this.revised.add((InstanceEdit) DatabaseObjectFactory.create(object));
        }

        this.summation = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "summation")) {
            this.summation.add((Summation) DatabaseObjectFactory.create(object));
        }

        this.containedInPathway = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "containedInPathway")) {
            this.containedInPathway.add((Pathway) DatabaseObjectFactory.create(object));
        }

        if (jsonObject.containsKey("goBiologicalProcess")) {
            this.goBiologicalProcess = DatabaseObjectUtils.getDatabaseObject(jsonObject, "goBiologicalProcess");
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

    public GO_BiologicalProcess getGoBiologicalProcess() {
        return goBiologicalProcess;
    }

    @Override
    public ImageResource getImageResource() {
        return DatabaseObjectImages.INSTANCE.regulator();
    }
}
