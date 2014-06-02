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
public class SimpleEntity extends PhysicalEntity {

    private List<ReferenceMolecule> referenceEntity = new LinkedList<ReferenceMolecule>();

    public SimpleEntity(JSONObject jsonObject) {
        super(SchemaClass.SIMPLE_ENTITY, jsonObject);

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "referenceEntity")) {
            this.referenceEntity.add(new ReferenceMolecule(object));
        }
    }

    public List<ReferenceMolecule> getReferenceEntity() {
        return referenceEntity;
    }
}
