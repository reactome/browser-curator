package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.FactoryUtils;
import org.reactome.web.elv.client.common.data.factory.ModelFactory;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public class InterChainCrosslinkedResidue extends CrosslinkedResidue {

    private List<InterChainCrosslinkedResidue> equivalentTo = new LinkedList<InterChainCrosslinkedResidue>();
    private List<ReferenceSequence> secondReferenceSequence = new LinkedList<ReferenceSequence>();

    public InterChainCrosslinkedResidue(JSONObject jsonObject) {
        super(SchemaClass.INTER_CHAIN_CROSSLINKED_RESIDUE, jsonObject);

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "equivalentTo")) {
            this.equivalentTo.add(new InterChainCrosslinkedResidue(object));
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "secondReferenceSequence")) {
            this.secondReferenceSequence.add((ReferenceSequence) ModelFactory.getDatabaseObject(object));
        }
    }

    public List<InterChainCrosslinkedResidue> getEquivalentTo() {
        return equivalentTo;
    }

    public List<ReferenceSequence> getSecondReferenceSequence() {
        return secondReferenceSequence;
    }
}
