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
public class ReferenceRNASequence extends ReferenceSequence {
    private List<ReferenceDNASequence> referenceGene = new LinkedList<ReferenceDNASequence>();

    public ReferenceRNASequence(JSONObject jsonObject) {
        super(SchemaClass.REFERENCE_RNA_SEQUENCE, jsonObject);

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "referenceGene")) {
            this.referenceGene.add(new ReferenceDNASequence(object));
        }
    }

    public List<ReferenceDNASequence> getReferenceGene() {
        return referenceGene;
    }
}
