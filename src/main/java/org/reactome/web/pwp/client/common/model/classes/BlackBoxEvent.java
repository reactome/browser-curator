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
public class BlackBoxEvent extends ReactionLikeEvent {

    private Event templateEvent;
    private List<Event> hasEvent;

    public BlackBoxEvent() {
        super(SchemaClass.BLACK_BOX_EVENT);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        if (jsonObject.containsKey("templateEvent")) {
            this.templateEvent = DatabaseObjectUtils.getDatabaseObject(jsonObject, "templateEvent");
        }

        this.hasEvent = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "hasEvent")) {
            this.hasEvent.add((Event) DatabaseObjectFactory.create(object));
        }
    }

    public Event getTemplateEvent() {
        return templateEvent;
    }

    public List<Event> getHasEvent() {
        return hasEvent;
    }

    @Override
    public ImageResource getImageResource() {
        return DatabaseObjectImages.INSTANCE.blackBoxEvent();
    }
}
