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
public class Pathway extends Event {
    private String doi;
    private String isCanonical;
    private List<Event> hasEvent;
    private Boolean hasDiagram;
    private org.reactome.web.pwp.client.common.model.classes.Pathway normalPathway;

    public Pathway() {
        super(SchemaClass.PATHWAY);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        if (jsonObject.containsKey("doi")) {
            this.doi = DatabaseObjectUtils.getStringValue(jsonObject, "doi");
        }

        if (jsonObject.containsKey("isCanonical")) {
            this.isCanonical = DatabaseObjectUtils.getStringValue(jsonObject, "isCanonical");
        }

        this.hasEvent = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "hasEvent")) {
            this.hasEvent.add((Event) DatabaseObjectFactory.create(object));
        }

        if (jsonObject.containsKey("hasDiagram")) {
            this.hasDiagram = DatabaseObjectUtils.getBooleanValue(jsonObject, "hasDiagram");
        } else {
            this.hasDiagram = false;
        }

        if (jsonObject.containsKey("normalPathway")) {
            this.normalPathway = DatabaseObjectUtils.getDatabaseObject(jsonObject, "normalPathway");
        }
    }

    public String getDoi() {
        return doi;
    }

    public String getCanonical() {
        return isCanonical;
    }

    public List<Event> getHasEvent() {
        return hasEvent;
    }

    public Boolean getHasDiagram() {
        return hasDiagram;
    }

    public org.reactome.web.pwp.client.common.model.classes.Pathway getNormalPathway() {
        return normalPathway;
    }

    @Override
    public ImageResource getImageResource() {
        return DatabaseObjectImages.INSTANCE.pathway();
    }
}
