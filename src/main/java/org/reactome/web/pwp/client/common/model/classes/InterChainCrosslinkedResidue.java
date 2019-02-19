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
public class InterChainCrosslinkedResidue extends CrosslinkedResidue {

    private List<org.reactome.web.pwp.client.common.model.classes.InterChainCrosslinkedResidue> equivalentTo;
    private List<ReferenceSequence> secondReferenceSequence;

    public InterChainCrosslinkedResidue() {
        super(SchemaClass.INTER_CHAIN_CROSSLINKED_RESIDUE);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        this.equivalentTo = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "equivalentTo")) {
            this.equivalentTo.add((org.reactome.web.pwp.client.common.model.classes.InterChainCrosslinkedResidue) DatabaseObjectFactory.create(object));
        }

        this.secondReferenceSequence = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "secondReferenceSequence")) {
            this.secondReferenceSequence.add((ReferenceSequence) DatabaseObjectFactory.create(object));
        }
    }

    public List<org.reactome.web.pwp.client.common.model.classes.InterChainCrosslinkedResidue> getEquivalentTo() {
        return equivalentTo;
    }

    public List<ReferenceSequence> getSecondReferenceSequence() {
        return secondReferenceSequence;
    }
}
