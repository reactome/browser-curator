package org.reactome.web.elv.client.details.tabs.analysis.events;

import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.common.data.model.Species;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class AnalysisTabPathwaySelected {
    private Species species;
    private Pathway diagram;
    private Pathway pathway;

    public AnalysisTabPathwaySelected(Species species, Pathway diagram, Pathway pathway) {
        this.species = species;
        this.diagram = diagram;
        this.pathway = pathway;
    }

    public Species getSpecies() {
        return species;
    }

    public Pathway getDiagram() {
        return diagram;
    }

    public Pathway getPathway() {
        return pathway;
    }
}
