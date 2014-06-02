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
public class ReferenceGeneProduct extends ReferenceSequence {
    private List<ReferenceDNASequence> referenceGene = new LinkedList<ReferenceDNASequence>();
    private List<ReferenceRNASequence> referenceTranscript = new LinkedList<ReferenceRNASequence>();

    public ReferenceGeneProduct(JSONObject jsonObject) {
        this(SchemaClass.REFERENCE_GENE_PRODUCT, jsonObject);
    }

    public ReferenceGeneProduct(SchemaClass schemaClass, JSONObject jsonObject) {
        super(schemaClass, jsonObject);

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "referenceGene")) {
            this.referenceGene.add(new ReferenceDNASequence(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "referenceTranscript")) {
            this.referenceTranscript.add(new ReferenceRNASequence(object));
        }
    }

    public List<ReferenceDNASequence> getReferenceGene() {
        return referenceGene;
    }

    public List<ReferenceRNASequence> getReferenceTranscript() {
        return referenceTranscript;
    }
}
