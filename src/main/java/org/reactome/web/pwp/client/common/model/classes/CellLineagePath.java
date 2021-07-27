package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.resources.client.ImageResource;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectFactory;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectUtils;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;
import org.reactome.web.pwp.client.common.model.images.DatabaseObjectImages;

import java.util.LinkedList;
import java.util.List;

public class CellLineagePath extends Event {
    private String tissue;
    private String tissueClass;
    private List<Event> hasEvent;
    private boolean hasDiagram;

    public CellLineagePath() {
        super(SchemaClass.CELL_LINEAGE_PATH);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        this.hasEvent = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "hasEvent")) {
            this.hasEvent.add((Event) DatabaseObjectFactory.create(object));
        }

        if (jsonObject.containsKey("tissue")) {
            this.tissue = DatabaseObjectUtils.getStringValue(jsonObject, "tissue");
        }

        if (jsonObject.containsKey("tissueClass")) {
            this.tissueClass = DatabaseObjectUtils.getStringValue(jsonObject, "tissueClass");
        }

        if (jsonObject.containsKey("hasDiagram")) {
            this.hasDiagram = DatabaseObjectUtils.getBooleanValue(jsonObject, "hasDiagram");
        } else {
            this.hasDiagram = false;
        }

    }

    @Override
    public ImageResource getImageResource() {
        return DatabaseObjectImages.INSTANCE.cellLineagePath();
    }

    public String getTissue() {
        return this.tissue;
    }

    public String getTissueClass() {
        return this.tissueClass;
    }

    public List<Event> getHasEvent() {
        return this.hasEvent;
    }

    public boolean getHasDiagram() {
        return this.hasDiagram;
    }
}
