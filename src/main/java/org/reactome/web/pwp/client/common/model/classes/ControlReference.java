package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectFactory;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectUtils;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;

import java.util.LinkedList;
import java.util.List;

public class ControlReference extends DatabaseObject {
    private List<Publication> literatureReferences;

    public ControlReference(SchemaClass schemaClass) {
        super(schemaClass);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        this.literatureReferences = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "literatureReference")) {
            this.literatureReferences.add((Publication) DatabaseObjectFactory.create(object));
        }
    }

    public List<Publication> getLiteratureReferences() {
        return this.literatureReferences;
    }
}
