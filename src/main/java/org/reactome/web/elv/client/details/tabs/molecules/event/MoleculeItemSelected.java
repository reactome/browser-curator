package org.reactome.web.elv.client.details.tabs.molecules.event;

import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.details.tabs.molecules.model.data.PhysicalToReferenceEntityMap;

/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
public class MoleculeItemSelected {
    private Pathway pathway;
    private PhysicalToReferenceEntityMap pe2RefEntityMap;

    public MoleculeItemSelected(Pathway pathway, PhysicalToReferenceEntityMap pe2RefEntityMap) {
        this.pathway = pathway;
        this.pe2RefEntityMap = pe2RefEntityMap;
    }

    public Pathway getPathway() {
        return pathway;
    }

    public PhysicalToReferenceEntityMap getPE2RefEntityMap() {
        return pe2RefEntityMap;
    }
}
