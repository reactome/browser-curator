package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectFactory;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectUtils;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public class FrontPage extends DatabaseObject {

    private List<Event> frontPageItem;

    public FrontPage() {
        super(SchemaClass.FRONT_PAGE);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        this.frontPageItem = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "frontPageItem")) {
            this.frontPageItem.add((Event) DatabaseObjectFactory.create(object));
        }
    }

    public List<Event> getFrontPageItem() {
        return frontPageItem;
    }
}
