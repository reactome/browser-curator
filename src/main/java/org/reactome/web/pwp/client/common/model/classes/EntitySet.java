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
public class EntitySet extends PhysicalEntity {
    private String totalProt;
    private String inferredProt;
    private String maxHomologues;
    private List<PhysicalEntity> hasMember;

    public EntitySet() {
        this(SchemaClass.ENTITY_SET);
    }

    public EntitySet(SchemaClass schemaClass) {
        super(schemaClass);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        if (jsonObject.containsKey("totalProt")) {
            this.totalProt = DatabaseObjectUtils.getStringValue(jsonObject, "totalProt");
        }

        if (jsonObject.containsKey("inferredProt")) {
            this.inferredProt = DatabaseObjectUtils.getStringValue(jsonObject, "inferredProt");
        }

        if (jsonObject.containsKey("maxHomologues")) {
            this.maxHomologues = DatabaseObjectUtils.getStringValue(jsonObject, "maxHomologues");
        }

        this.hasMember = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "hasMember")) {
            this.hasMember.add((PhysicalEntity) DatabaseObjectFactory.create(object));
        }
    }

    public String getTotalProt() {
        return totalProt;
    }

    public String getInferredProt() {
        return inferredProt;
    }

    public String getMaxHomologues() {
        return maxHomologues;
    }

    public List<PhysicalEntity> getHasMember() {
        return hasMember;
    }

    @Override
    public ImageResource getImageResource() {
        return DatabaseObjectImages.INSTANCE.entitySet();
    }
}
