package org.reactome.web.pwp.client.common.model.classes;


import com.google.gwt.json.client.JSONObject;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectUtils;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public class FragmentReplacedModification extends FragmentModification {

    private String alteredAminoAcidFragment;

    public FragmentReplacedModification() {
        super(SchemaClass.FRAGMENT_REPLACED_MODIFICATION);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        if (jsonObject.containsKey("alteredAminoAcidFragment")) {
            this.alteredAminoAcidFragment = DatabaseObjectUtils.getStringValue(jsonObject, "alteredAminoAcidFragment");
        }
    }

    public String getAlteredAminoAcidFragment() {
        return alteredAminoAcidFragment;
    }
}
