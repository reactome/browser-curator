package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectUtils;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ReferenceTherapeutic extends ReferenceEntity {

    private String abbreviation;
    private List<String> approvalSource;
    private Boolean approved;
    private String inn;
    private String type;

    public ReferenceTherapeutic() {
        super(SchemaClass.REFERENCE_THERAPEUTIC);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        if (jsonObject.containsKey("abbreviation")) {
            this.abbreviation = DatabaseObjectUtils.getStringValue(jsonObject, "abbreviation");
        }

        this.approvalSource  = new LinkedList<>();
        this.approvalSource.addAll(DatabaseObjectUtils.getStringList(jsonObject, "approvalSource"));

        if (jsonObject.containsKey("approved")) {
            this.approved = DatabaseObjectUtils.getBooleanValue(jsonObject, "approved");
        }

        if (jsonObject.containsKey("inn")) {
            this.inn = DatabaseObjectUtils.getStringValue(jsonObject, "inn");
        }

        if (jsonObject.containsKey("totalProt")) {
            this.type = DatabaseObjectUtils.getStringValue(jsonObject, "type");
        }
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public List<String> getApprovalSource() {
        return approvalSource;
    }

    public Boolean getApproved() {
        return approved;
    }

    public String getInn() {
        return inn;
    }

    public String getType() {
        return type;
    }
}
