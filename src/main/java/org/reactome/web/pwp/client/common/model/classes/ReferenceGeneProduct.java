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
public class ReferenceGeneProduct extends ReferenceSequence {
    private List<ReferenceDNASequence> referenceGene;
    private List<ReferenceRNASequence> referenceTranscript;

    public ReferenceGeneProduct() {
        this(SchemaClass.REFERENCE_GENE_PRODUCT);
    }

    public ReferenceGeneProduct(SchemaClass schemaClass) {
        super(schemaClass);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        this.referenceGene = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "referenceGene")) {
            this.referenceGene.add((ReferenceDNASequence) DatabaseObjectFactory.create(object));
        }

        this.referenceTranscript = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "referenceTranscript")) {
            this.referenceTranscript.add((ReferenceRNASequence) DatabaseObjectFactory.create(object));
        }
    }

    public List<ReferenceDNASequence> getReferenceGene() {
        return referenceGene;
    }

    public List<ReferenceRNASequence> getReferenceTranscript() {
        return referenceTranscript;
    }
}
