/**
 * Ext GWT 3.0.0-SNAPSHOT - Ext for GWT
 * Copyright(c) 2007-2011, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://sencha.com/license
 */
package org.reactome.web.elv.client.common;


import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface ReactomeImages extends ClientBundle {

    public ReactomeImages INSTANCE = GWT.create(ReactomeImages.class);

    @Source("images/analysisTools.png")
    ImageResource analysisTool();

    @Source("images/back.png")
    ImageResource back();

    @Source("images/banner.png")
    ImageResource banner();

    @Source("images/beta.png")
    ImageResource beta();

    @Source("images/BlackBoxEvent.png")
    ImageResource blackBoxEvent();

    @Source("images/CandidateSet.png")
    ImageResource candidateSet();

    @Source("images/close.png")
    ImageResource close();

    @Source("images/Complex.png")
    ImageResource complex();

    @Source("images/complex_key.png")
    ImageResource complexKey();

    @Source("images/ConceptualEvent.png")
    ImageResource conceptualEvent();

    @Source("images/DefinedSet.png")
    ImageResource definedSet();

    @Source("images/Depolymerization.png")
    ImageResource depolymerization();

    @Source("images/diagram.png")
    ImageResource diagram();

    @Source("images/diagram_key_new.png")
    ImageResource diagramKey();

    @Source("images/show_diagram_key.png")
    ImageResource diagramKeyIcon();

    @Source("images/downArrow.png")
    ImageResource downArrow();

    @Source("images/download.png")
    ImageResource download();

    @Source("images/download_file.png")
    ImageResource downloadFile();

    @Source("images/EntitySet.png")
    ImageResource entitySet();

    @Source("images/EntityWithAccessionedSequence.png")
    ImageResource entityWithAccessionedSequence();

    @Source("images/EquivalentEventSet.png")
    ImageResource equivalentEventSet();

    @Source("images/exclamation.png")
    ImageResource exclamation();

    @Source("images/details.png")
    ImageResource details();

    @Source("images/eye.png")
    ImageResource eye();

    @Source("images/FailedReaction.gif")
    ImageResource failedReaction();

    @Source("images/GenomeEncodeEntity.png")
    ImageResource genomeEncodeEntity();

    @Source("images/hierarchy.png")
    ImageResource hierarchy();

    @Source("images/information.png")
    ImageResource information();

    @Source("images/isDisease.png")
    ImageResource isDisease();

    @Source("images/isInferred.png")
    ImageResource isInferred();

    @Source("images/loader.gif")
    ImageResource loader();

    @Source("images/next.png")
    ImageResource next();

    @Source("images/NewTag.png")
    ImageResource newTag();

    @Source("images/no.png")
    ImageResource no();

    @Source("images/OpenSet.png")
    ImageResource openSet();

    @Source("images/ORCID.png")
    ImageResource orcid();

    @Source("images/OtherEntity.png")
    ImageResource otherEntity();

    @Source("images/Pathway.png")
    ImageResource pathway();

    @Source("images/Polymer.png")
    ImageResource polymer();

    @Source("images/Polymerization.png")
    ImageResource polymerization();

    @Source("images/protein_key.png")
    ImageResource proteinKey();

    @Source("images/Reaction.png")
    ImageResource reaction();

    @Source("images/ReferenceDNASequence.png")
    ImageResource referenceDNASequence();

    @Source("images/ReferenceGroup.png")
    ImageResource referenceGroup();

    @Source("images/ReferenceRNASequence.png")
    ImageResource referenceRNASequence();

    @Source("images/SimpleEntity.png")
    ImageResource simpleEntity();

    @Source("images/small_molecule_key.png")
    ImageResource smallMoleculeKey();

    @Source("images/summary_panel_example.png")
    ImageResource summaryPanelExample();

    @Source("images/UpdateTag.png")
    ImageResource updatedTag();

    @Source("images/upArrow.png")
    ImageResource upArrow();

    @Source("images/yes.png")
    ImageResource yes();

}