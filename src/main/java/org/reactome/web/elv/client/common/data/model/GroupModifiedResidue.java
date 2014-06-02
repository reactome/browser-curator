package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.FactoryUtils;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public class GroupModifiedResidue extends TranslationalModification {

    private DatabaseObject modification;

    public GroupModifiedResidue(JSONObject jsonObject) {
        super(SchemaClass.GROUP_MODIFIED_RESIDUE, jsonObject);

        if(jsonObject.containsKey("modification")){
            this.modification = FactoryUtils.getDatabaseObject(jsonObject, "modification");
        }
    }

    public DatabaseObject getModification() {
        return modification;
    }
}
