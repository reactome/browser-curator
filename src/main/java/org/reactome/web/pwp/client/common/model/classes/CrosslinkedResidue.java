package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectUtils;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class CrosslinkedResidue extends TranslationalModification {

    private DatabaseObject modification;
    private Integer secondCoordinate;

    public CrosslinkedResidue(SchemaClass schemaClass) {
        super(schemaClass);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        if (jsonObject.containsKey("modification")) {
            this.modification = DatabaseObjectUtils.getDatabaseObject(jsonObject, "modification");
        }

        if (jsonObject.containsKey("secondCoordinate")) {
            this.secondCoordinate = DatabaseObjectUtils.getIntValue(jsonObject, "secondCoordinate");
        }

    }

    public DatabaseObject getModification() {
        return modification;
    }

    public Integer getSecondCoordinate() {
        return secondCoordinate;
    }
}
