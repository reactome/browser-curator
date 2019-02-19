package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectUtils;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class TranslationalModification extends AbstractModifiedResidue {

    private Integer coordinate;
    private PsiMod psiMod;

    public TranslationalModification(SchemaClass schemaClass) {
        super(schemaClass);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        if (jsonObject.containsKey("coordinate")) {
            this.coordinate = DatabaseObjectUtils.getIntValue(jsonObject, "coordinate");
        }

        if (jsonObject.containsKey("psiMod")) {
            this.psiMod = DatabaseObjectUtils.getDatabaseObject(jsonObject, "psiMod");
        }
    }

    public Integer getCoordinate() {
        return coordinate;
    }

    public PsiMod getPsiMod() {
        return psiMod;
    }
}
