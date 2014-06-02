package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.FactoryUtils;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public class ReplacedResidue extends GeneticallyModifiedResidue {

    private Integer coordinate;
    private List<PsiMod> psiMod = new LinkedList<PsiMod>();

    public ReplacedResidue(JSONObject jsonObject) {
        super(SchemaClass.REPLACED_RESIDUE, jsonObject);

        if(jsonObject.containsKey("coordinate")){
            this.coordinate = FactoryUtils.getIntValue(jsonObject, "coordinate");
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "psiMod")) {
            this.psiMod.add(new PsiMod(object));
        }
    }

    public Integer getCoordinate() {
        return coordinate;
    }

    public List<PsiMod> getPsiMod() {
        return psiMod;
    }
}
