package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.resources.client.ImageResource;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectFactory;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectUtils;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;
import org.reactome.web.pwp.client.common.model.images.DatabaseObjectImages;
import org.reactome.web.pwp.client.common.utils.Console;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Joel Weiser (joel.weiser@oicr.on.ca)
 */
public class CellLineagePath extends Event {
    private Anatomy tissue;
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
            this.tissue = DatabaseObjectUtils.getDatabaseObject(jsonObject, "tissue");
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

    public Anatomy getTissue() {
        return this.tissue;
    }

    public List<Event> getHasEvent() {
        return this.hasEvent;
    }

    public boolean getHasDiagram() {
        return this.hasDiagram;
    }
}
