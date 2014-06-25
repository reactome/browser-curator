package org.reactome.web.elv.client.common.data.model;


import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.FactoryUtils;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public class FragmentReplacedModification extends FragmentModification {

    private String alteredAminoAcidFragment;

    public FragmentReplacedModification(JSONObject jsonObject) {
        super(SchemaClass.FRAGMENT_REPLACED_MODIFICATION, jsonObject);

        if(jsonObject.containsKey("alteredAminoAcidFragment")){
            this.alteredAminoAcidFragment = FactoryUtils.getStringValue(jsonObject, "alteredAminoAcidFragment");
        }
    }

    public String getAlteredAminoAcidFragment() {
        return alteredAminoAcidFragment;
    }


}
