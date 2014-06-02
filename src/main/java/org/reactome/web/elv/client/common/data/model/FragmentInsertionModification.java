package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.FactoryUtils;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public class FragmentInsertionModification extends FragmentModification {

    private Integer coordinate;

    public FragmentInsertionModification(JSONObject jsonObject) {
        super(SchemaClass.FRAGMENT_INSERTION_MODIFICATION, jsonObject);

        if(jsonObject.containsKey("coordinate")){
            this.coordinate = FactoryUtils.getIntValue(jsonObject, "coordinate");
        }
    }

    public Integer getCoordinate() {
        return coordinate;
    }
}
