package org.reactome.web.elv.client.details.events;


import org.reactome.web.elv.client.details.tabs.molecules.model.data.PhysicalToReferenceEntityMap;

import java.util.List;

/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
public interface MoleculeSelectedHandler {

    public void moleculeSelected(List<PhysicalToReferenceEntityMap> physicalEntityList);
}