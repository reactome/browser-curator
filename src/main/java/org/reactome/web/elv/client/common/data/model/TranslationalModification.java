package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.FactoryUtils;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class TranslationalModification extends AbstractModifiedResidue {

    private Integer coordinate;
    private PsiMod psiMod;

    public TranslationalModification(SchemaClass schemaClass, JSONObject jsonObject) {
        super(schemaClass, jsonObject);

        if(jsonObject.containsKey("coordinate")){
            this.coordinate = FactoryUtils.getIntValue(jsonObject, "coordinate");
        }

        if(jsonObject.containsKey("psiMod")){
            this.psiMod = (PsiMod) FactoryUtils.getDatabaseObject(jsonObject, "psiMod");
        }
    }

    public Integer getCoordinate() {
        return coordinate;
    }

    public PsiMod getPsiMod() {
        return psiMod;
    }
}
