package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectUtils;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class ReferenceSequence extends ReferenceEntity {
    private Integer sequenceLength;
    private Species species;
    private String checksum;
    private List<String> comment;
    private List<String> description;
    private List<String> geneName;
    private Boolean isSequenceChanged;
    private List<String> keyword;
    private List<String> secondaryIdentifier;
    private String url; // Valid URL based on referenceDatabase

    public ReferenceSequence(SchemaClass schemaClass) {
        super(schemaClass);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        if (jsonObject.containsKey("sequenceLength")) {
            this.sequenceLength = DatabaseObjectUtils.getIntValue(jsonObject, "sequenceLength");
        }

        if (jsonObject.containsKey("species")) {
            this.species = DatabaseObjectUtils.getDatabaseObject(jsonObject, "species");
        }

        if (jsonObject.containsKey("checksum")) {
            this.checksum = DatabaseObjectUtils.getStringValue(jsonObject, "checksum");
        }

        this.comment = new LinkedList<>();
        for (String comment : DatabaseObjectUtils.getStringList(jsonObject, "comment")) {
            this.comment.add(comment);
        }

        this.description = new LinkedList<>();
        for (String description : DatabaseObjectUtils.getStringList(jsonObject, "description")) {
            this.description.add(description);
        }

        this.geneName = new LinkedList<>();
        for (String geneName : DatabaseObjectUtils.getStringList(jsonObject, "geneName")) {
            this.geneName.add(geneName);
        }

        if (jsonObject.containsKey("isSequenceChanged")) {
            this.isSequenceChanged = DatabaseObjectUtils.getBooleanValue(jsonObject, "isSequenceChanged");
        }

        this.keyword = new LinkedList<>();
        for (String name : DatabaseObjectUtils.getStringList(jsonObject, "keyword")) {
            this.keyword.add(name);
        }

        this.secondaryIdentifier = new LinkedList<>();
        for (String secondaryIdentifier : DatabaseObjectUtils.getStringList(jsonObject, "secondaryIdentifier")) {
            this.secondaryIdentifier.add(secondaryIdentifier);
        }

        if (jsonObject.containsKey("url")) {
            this.url = DatabaseObjectUtils.getStringValue(jsonObject, "url");
        }
    }

    public Integer getSequenceLength() {
        return sequenceLength;
    }

    public Species getSpecies() {
        return species;
    }

    public String getChecksum() {
        return checksum;
    }

    public List<String> getComment() {
        return comment;
    }

    public List<String> getDescription() {
        return description;
    }

    public List<String> getGeneName() {
        return geneName;
    }

    public Boolean getSequenceChanged() {
        return isSequenceChanged;
    }

    public List<String> getKeyword() {
        return keyword;
    }

    public List<String> getSecondaryIdentifier() {
        return secondaryIdentifier;
    }

    public String getUrl() {
        return url;
    }
}
