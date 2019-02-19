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
public class Polymer extends PhysicalEntity {

    private Integer maxUnitCount;
    private Integer minUnitCount;
    private String totalProt;
    private String maxHomologues;
    private String inferredProt;
    private List<PhysicalEntity> repeatedUnits;


    public Polymer() {
        super(SchemaClass.POLYMER);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        if (jsonObject.containsKey("maxUnitCount")) {
            this.maxUnitCount = DatabaseObjectUtils.getIntValue(jsonObject, "maxUnitCount");
        }

        if (jsonObject.containsKey("minUnitCount")) {
            this.minUnitCount = DatabaseObjectUtils.getIntValue(jsonObject, "minUnitCount");
        }

        if (jsonObject.containsKey("totalProt")) {
            this.totalProt = DatabaseObjectUtils.getStringValue(jsonObject, "totalProt");
        }

        if (jsonObject.containsKey("maxHomologues")) {
            this.maxHomologues = DatabaseObjectUtils.getStringValue(jsonObject, "maxHomologues");
        }

        if (jsonObject.containsKey("inferredProt")) {
            this.inferredProt = DatabaseObjectUtils.getStringValue(jsonObject, "inferredProt");
        }

        this.repeatedUnits = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "repeatedUnit")) {
            this.repeatedUnits.add((PhysicalEntity) DatabaseObjectFactory.create(object));
        }
    }

    public Integer getMaxUnitCount() {
        return maxUnitCount;
    }

    public Integer getMinUnitCount() {
        return minUnitCount;
    }

    public String getTotalProt() {
        return totalProt;
    }

    public String getMaxHomologues() {
        return maxHomologues;
    }

    public String getInferredProt() {
        return inferredProt;
    }

    public List<PhysicalEntity> getRepeatedUnits() {
        return repeatedUnits;
    }

    @Override
    public ImageResource getImageResource() {
        return DatabaseObjectImages.INSTANCE.polymer();
    }
}
