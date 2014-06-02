package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.FactoryUtils;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class CrosslinkedResidue extends TranslationalModification {

    private DatabaseObject modification;
    private Integer secondCoordinate;

    public CrosslinkedResidue(SchemaClass schemaClass, JSONObject jsonObject) {
        super(schemaClass, jsonObject);

        if(jsonObject.containsKey("modification")){
            this.modification = FactoryUtils.getDatabaseObject(jsonObject, "modification");
        }

        if(jsonObject.containsKey("secondCoordinate")){
            this.secondCoordinate = FactoryUtils.getIntValue(jsonObject, "secondCoordinate");
        }

    }

    public DatabaseObject getModification() {
        return modification;
    }

    public Integer getSecondCoordinate() {
        return secondCoordinate;
    }
}
