package org.reactome.web.pwp.client.common.model.images;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface DatabaseObjectImages extends ClientBundle {

    org.reactome.web.pwp.client.common.model.images.DatabaseObjectImages INSTANCE = GWT.create(org.reactome.web.pwp.client.common.model.images.DatabaseObjectImages.class);

    @Source("BlackBoxEvent.png")
    ImageResource blackBoxEvent();

    @Source("CandidateSet.png")
    ImageResource candidateSet();

    @Source("ChemicalDrug.png")
    ImageResource chemicalDrug();

    @Source("Complex.png")
    ImageResource complex();

    @Source("ConceptualEvent.png")
    ImageResource conceptualEvent();

    @Source("DefinedSet.png")
    ImageResource definedSet();

    @Source("Depolymerization.png")
    ImageResource depolymerization();

    @Source("DNASequence.png")
    ImageResource dnaSequence();

    @Source("EntitySet.png")
    ImageResource entitySet();

    @Source("Protein.png")
    ImageResource protein();

    @Source("ProteinDrug.png")
    ImageResource proteinDrug();

    @Source("EquivalentEventSet.png")
    ImageResource equivalentEventSet();

    @Source("exclamation.png")
    ImageResource exclamation();

    @Source("FailedReaction.gif")
    ImageResource failedReaction();

    @Source("GenomeEncodeEntity.png")
    ImageResource genomeEncodeEntity();

    @Source("isDisease.png")
    ImageResource isDisease();

    @Source("isInferred.png")
    ImageResource isInferred();

    @Source("NewTag.png")
    ImageResource isNew();

    @Source("UpdateTag.png")
    ImageResource isUpdated();

    @Source("OpenSet.png")
    ImageResource openSet();

    @Source("OtherEntity.png")
    ImageResource otherEntity();

    @Source("Pathway.png")
    ImageResource pathway();

    @Source("Polymer.png")
    ImageResource polymer();

    @Source("Polymerization.png")
    ImageResource polymerization();

    @Source("Reaction.png")
    ImageResource reaction();

    @Source("ReferenceGroup.png")
    ImageResource referenceGroup();

    @Source("RNASequence.png")
    ImageResource rnaSequence();

    @Source("RNASequenceDrug.png")
    ImageResource rnaSequenceDrug();

    @Source("Regulator.png")
    ImageResource regulator();

    @Source("SimpleEntity.png")
    ImageResource simpleEntity();

}
