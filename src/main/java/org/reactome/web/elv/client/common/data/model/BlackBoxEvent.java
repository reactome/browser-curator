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
public class BlackBoxEvent extends ReactionLikeEvent {

    private Event templateEvent;
    private List<Event> hasEvent = new LinkedList<Event>();

    public BlackBoxEvent(JSONObject jsonObject) {
        super(SchemaClass.BLACK_BOX_EVENT, jsonObject);

        if(jsonObject.containsKey("templateEvent")){
            this.templateEvent = (Event) FactoryUtils.getDatabaseObject(jsonObject, "templateEvent");
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "hasEvent")) {
            this.hasEvent.add((Event) ModelFactory.getDatabaseObject(object));
        }
    }

    public Event getTemplateEvent() {
        return templateEvent;
    }

    public List<Event> getHasEvent() {
        return hasEvent;
    }
}
