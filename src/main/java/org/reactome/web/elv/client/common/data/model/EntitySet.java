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
public class EntitySet extends PhysicalEntity {
    private String totalProt;
    private String inferredProt;
    private String maxHomologues;
    private List<PhysicalEntity> hasMember = new LinkedList<PhysicalEntity>();

    public EntitySet(JSONObject jsonObject) {
        this(SchemaClass.ENTITY_SET, jsonObject);
    }

    public EntitySet(SchemaClass schemaClass, JSONObject jsonObject) {
        super(schemaClass, jsonObject);

        if(jsonObject.containsKey("totalProt")){
            this.totalProt = FactoryUtils.getStringValue(jsonObject, "totalProt");
        }

        if(jsonObject.containsKey("inferredProt")){
            this.inferredProt = FactoryUtils.getStringValue(jsonObject, "inferredProt");
        }

        if(jsonObject.containsKey("maxHomologues")){
            this.maxHomologues = FactoryUtils.getStringValue(jsonObject, "maxHomologues");
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "hasMember")) {
            this.hasMember.add((PhysicalEntity) ModelFactory.getDatabaseObject(object));
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
}
