package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectUtils;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public class GroupModifiedResidue extends TranslationalModification {

    private DatabaseObject modification;

    public GroupModifiedResidue() {
        super(SchemaClass.GROUP_MODIFIED_RESIDUE);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        if (jsonObject.containsKey("modification")) {
            this.modification = DatabaseObjectUtils.getDatabaseObject(jsonObject, "modification");
        }
    }

    public DatabaseObject getModification() {
        return modification;
    }
}
