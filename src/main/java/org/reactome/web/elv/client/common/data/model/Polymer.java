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
public class Polymer extends PhysicalEntity {

    private Integer maxUnitCount;
    private Integer minUnitCount;
    private String totalProt;
    private String maxHomologues;
    private String inferredProt;
    private List<PhysicalEntity> repeatedUnits = new LinkedList<PhysicalEntity>();


    public Polymer(JSONObject jsonObject) {
        super(SchemaClass.POLYMER, jsonObject);

        if(jsonObject.containsKey("maxUnitCount")){
            this.maxUnitCount = FactoryUtils.getIntValue(jsonObject, "maxUnitCount");
        }

        if(jsonObject.containsKey("minUnitCount")){
            this.minUnitCount = FactoryUtils.getIntValue(jsonObject, "minUnitCount");
        }

        if(jsonObject.containsKey("totalProt")){
            this.totalProt = FactoryUtils.getStringValue(jsonObject, "totalProt");
        }

        if(jsonObject.containsKey("maxHomologues")){
            this.maxHomologues = FactoryUtils.getStringValue(jsonObject, "maxHomologues");
        }

        if(jsonObject.containsKey("inferredProt")){
            this.inferredProt = FactoryUtils.getStringValue(jsonObject, "inferredProt");
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "repeatedUnit")) {
            this.repeatedUnits.add((PhysicalEntity) ModelFactory.getDatabaseObject(object));
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
}
