package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.FactoryUtils;
import org.reactome.web.elv.client.common.data.factory.ModelFactory;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public class EntityWithAccessionedSequence extends GenomeEncodedEntity {

    private Integer endCoordinate;
    private ReferenceSequence referenceEntity;
    private Integer startCoordinate;
    private List<AbstractModifiedResidue> hasModifiedResidue = new LinkedList<AbstractModifiedResidue>();

    public EntityWithAccessionedSequence(JSONObject jsonObject) {
        super(SchemaClass.ENTITY_WITH_ACCESSIONED_SEQUENCE, jsonObject);

        if(jsonObject.containsKey("endCoordinate")){
            this.endCoordinate = FactoryUtils.getIntValue(jsonObject, "endCoordinate");
        }

        if(jsonObject.containsKey("referenceEntity")){
            this.referenceEntity = (ReferenceSequence) FactoryUtils.getDatabaseObject(jsonObject, "referenceEntity");
        }

        if(jsonObject.containsKey("startCoordinate")){
            this.startCoordinate = FactoryUtils.getIntValue(jsonObject, "startCoordinate");
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "hasModifiedResidue")) {
            this.hasModifiedResidue.add((AbstractModifiedResidue) ModelFactory.getDatabaseObject(object));
        }
    }

    public Integer getEndCoordinate() {
        return endCoordinate;
    }

    public ReferenceSequence getReferenceEntity() {
        return referenceEntity;
    }

    public Integer getStartCoordinate() {
        return startCoordinate;
    }

    public List<AbstractModifiedResidue> getHasModifiedResidue() {
        return hasModifiedResidue;
    }
}
