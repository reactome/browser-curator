package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.FactoryUtils;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class FragmentModification extends GeneticallyModifiedResidue {

    private Integer endPositionInReferenceSequence;
    private Integer startPositionInReferenceSequence;

    public FragmentModification(SchemaClass schemaClass, JSONObject jsonObject) {
        super(schemaClass, jsonObject);

        if(jsonObject.containsKey("endPositionInReferenceSequence")){
            this.endPositionInReferenceSequence = FactoryUtils.getIntValue(jsonObject, "endPositionInReferenceSequence");
        }

        if(jsonObject.containsKey("startPositionInReferenceSequence")){
            this.startPositionInReferenceSequence = FactoryUtils.getIntValue(jsonObject, "startPositionInReferenceSequence");
        }
    }

    public Integer getEndPositionInReferenceSequence() {
        return endPositionInReferenceSequence;
    }

    public Integer getStartPositionInReferenceSequence() {
        return startPositionInReferenceSequence;
    }
}
