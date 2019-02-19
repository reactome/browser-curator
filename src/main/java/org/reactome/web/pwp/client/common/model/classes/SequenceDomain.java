package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectUtils;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public class SequenceDomain extends Domain {
    private Integer endCoordinate;
    private ReferenceSequence referenceEntity;
    private String referenceEntityClass;
    private Integer startCoordinate;

    public SequenceDomain() {
        super(SchemaClass.SEQUENCE_DOMAIN);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        if (jsonObject.containsKey("endCoordinate")) {
            this.endCoordinate = DatabaseObjectUtils.getIntValue(jsonObject, "endCoordinate");
        }

        if (jsonObject.containsKey("referenceEntity")) {
            this.referenceEntity = DatabaseObjectUtils.getDatabaseObject(jsonObject, "referenceEntity");
        }

        if (jsonObject.containsKey("referenceEntityClass")) {
            this.referenceEntityClass = DatabaseObjectUtils.getStringValue(jsonObject, "referenceEntityClass");
        }

        if (jsonObject.containsKey("startCoordinate")) {
            this.startCoordinate = DatabaseObjectUtils.getIntValue(jsonObject, "startCoordinate");
        }
    }

    public Integer getEndCoordinate() {
        return endCoordinate;
    }

    public ReferenceSequence getReferenceEntity() {
        return referenceEntity;
    }

    public String getReferenceEntityClass() {
        return referenceEntityClass;
    }

    public Integer getStartCoordinate() {
        return startCoordinate;
    }
}
