package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectFactory;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectUtils;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public class ReplacedResidue extends GeneticallyModifiedResidue {

    private Integer coordinate;
    private List<PsiMod> psiMod;

    public ReplacedResidue() {
        super(SchemaClass.REPLACED_RESIDUE);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        if (jsonObject.containsKey("coordinate")) {
            this.coordinate = DatabaseObjectUtils.getIntValue(jsonObject, "coordinate");
        }

        this.psiMod = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "psiMod")) {
            this.psiMod.add((PsiMod) DatabaseObjectFactory.create(object));
        }
    }

    public Integer getCoordinate() {
        return coordinate;
    }

    public List<PsiMod> getPsiMod() {
        return psiMod;
    }
}
