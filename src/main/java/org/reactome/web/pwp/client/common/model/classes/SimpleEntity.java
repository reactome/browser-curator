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
public class SimpleEntity extends PhysicalEntity {

    private List<ReferenceMolecule> referenceEntity;

    public SimpleEntity() {
        super(SchemaClass.SIMPLE_ENTITY);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        this.referenceEntity = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "referenceEntity")) {
            this.referenceEntity.add((ReferenceMolecule) DatabaseObjectFactory.create(object));
        }
    }

    public List<ReferenceMolecule> getReferenceEntity() {
        return referenceEntity;
    }

    @Override
    public ImageResource getImageResource() {
        return DatabaseObjectImages.INSTANCE.simpleEntity();
    }
}
