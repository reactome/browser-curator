package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.FactoryUtils;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public class SequenceDomain extends Domain {
    private Integer endCoordinate;
    private ReferenceSequence referenceEntity;
    private String referenceEntityClass;
    private Integer startCoordinate;

    public SequenceDomain(JSONObject jsonObject) {
        super(SchemaClass.SEQUENCE_DOMAIN, jsonObject);

        if(jsonObject.containsKey("endCoordinate")){
            this.endCoordinate = FactoryUtils.getIntValue(jsonObject, "endCoordinate");
        }

        if(jsonObject.containsKey("referenceEntity")){
            this.referenceEntity = (ReferenceSequence) FactoryUtils.getDatabaseObject(jsonObject, "referenceEntity");
        }

        if(jsonObject.containsKey("referenceEntityClass")){
            this.referenceEntityClass = FactoryUtils.getStringValue(jsonObject, "referenceEntityClass");
        }

        if(jsonObject.containsKey("startCoordinate")){
            this.startCoordinate = FactoryUtils.getIntValue(jsonObject, "startCoordinate");
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
