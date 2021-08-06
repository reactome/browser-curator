package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectFactory;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectUtils;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;

import java.util.LinkedList;
import java.util.List;

public class MarkerReference extends ControlReference {
    private List<EntityWithAccessionedSequence> markers;

    public MarkerReference() {
        super(SchemaClass.MARKER_REFERENCE);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        this.markers = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "marker")) {
            this.markers.add((EntityWithAccessionedSequence) DatabaseObjectFactory.create(object));
        }
    }

    public List<EntityWithAccessionedSequence> getMarkers() {
        return this.markers;
    }
}
