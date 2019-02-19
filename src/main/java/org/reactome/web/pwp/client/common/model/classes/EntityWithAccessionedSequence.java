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
public class EntityWithAccessionedSequence extends GenomeEncodedEntity {

    private Integer endCoordinate;
    private ReferenceSequence referenceEntity;
    private Integer startCoordinate;
    private List<AbstractModifiedResidue> hasModifiedResidue;

    public EntityWithAccessionedSequence() {
        super(SchemaClass.ENTITY_WITH_ACCESSIONED_SEQUENCE);
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

        if (jsonObject.containsKey("startCoordinate")) {
            this.startCoordinate = DatabaseObjectUtils.getIntValue(jsonObject, "startCoordinate");
        }

        this.hasModifiedResidue = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "hasModifiedResidue")) {
            this.hasModifiedResidue.add((AbstractModifiedResidue) DatabaseObjectFactory.create(object));
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

    @Override
    public ImageResource getImageResource() {
        return DatabaseObjectImages.INSTANCE.protein();
    }
}
