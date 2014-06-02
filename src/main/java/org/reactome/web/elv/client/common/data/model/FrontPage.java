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
public class FrontPage extends DatabaseObject {

    private List<Event> frontPageItem = new LinkedList<Event>();

    public FrontPage(JSONObject jsonObject) {
        super(SchemaClass.FRONT_PAGE, jsonObject);

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "frontPageItem")) {
            this.frontPageItem.add((Event) ModelFactory.getDatabaseObject(object));
        }
    }

    public List<Event> getFrontPageItem() {
        return frontPageItem;
    }
}
