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
public abstract class ReferenceSequence extends ReferenceEntity {
    private Integer sequenceLength;
    private Species species;
    private String checksum;
    private List<String> comment = new LinkedList<String>();
    private List<String> description = new LinkedList<String>();
    private List<String> geneName = new LinkedList<String>();
    private Boolean isSequenceChanged;
    private List<String> keyword = new LinkedList<String>();
    private List<String> secondaryIdentifier = new LinkedList<String>();
    private String url; // Valid URL based on referenceDatabase

    public ReferenceSequence(SchemaClass schemaClass, JSONObject jsonObject) {
        super(schemaClass, jsonObject);

        if(jsonObject.containsKey("sequenceLength")){
            this.sequenceLength = FactoryUtils.getIntValue(jsonObject, "sequenceLength");
        }

        if(jsonObject.containsKey("species")){
            this.species = (Species) FactoryUtils.getDatabaseObject(jsonObject, "species");
        }

        if(jsonObject.containsKey("checksum")){
            this.checksum = FactoryUtils.getStringValue(jsonObject, "checksum");
        }

        for (String comment : FactoryUtils.getStringList(jsonObject, "comment")) {
            this.comment.add(comment);
        }

        for (String description : FactoryUtils.getStringList(jsonObject, "description")) {
            this.description.add(description);
        }

        for (String geneName : FactoryUtils.getStringList(jsonObject, "geneName")) {
            this.geneName.add(geneName);
        }

        if(jsonObject.containsKey("isSequenceChanged")){
            this.isSequenceChanged = FactoryUtils.getBooleanValue(jsonObject, "isSequenceChanged");
        }

        for (String name : FactoryUtils.getStringList(jsonObject, "keyword")) {
            this.keyword.add(name);
        }

        for (String secondaryIdentifier : FactoryUtils.getStringList(jsonObject, "secondaryIdentifier")) {
            this.secondaryIdentifier.add(secondaryIdentifier);
        }

        if(jsonObject.containsKey("url")){
            this.url = FactoryUtils.getStringValue(jsonObject, "url");
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
