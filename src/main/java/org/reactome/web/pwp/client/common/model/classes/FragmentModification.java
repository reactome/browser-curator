package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectUtils;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class FragmentModification extends GeneticallyModifiedResidue {

    private Integer endPositionInReferenceSequence;
    private Integer startPositionInReferenceSequence;

    public FragmentModification(SchemaClass schemaClass) {
        super(schemaClass);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        if (jsonObject.containsKey("endPositionInReferenceSequence")) {
            this.endPositionInReferenceSequence = DatabaseObjectUtils.getIntValue(jsonObject, "endPositionInReferenceSequence");
        }

        if (jsonObject.containsKey("startPositionInReferenceSequence")) {
            this.startPositionInReferenceSequence = DatabaseObjectUtils.getIntValue(jsonObject, "startPositionInReferenceSequence");
        }
    }

    public Integer getEndPositionInReferenceSequence() {
        return endPositionInReferenceSequence;
    }

    public Integer getStartPositionInReferenceSequence() {
        return startPositionInReferenceSequence;
    }
}
