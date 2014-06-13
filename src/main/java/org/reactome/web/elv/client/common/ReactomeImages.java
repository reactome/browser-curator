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

    @Source("images/close.png")
    ImageResource close();

    @Source("images/Complex.png")
    ImageResource complex();

    @Source("images/complex_key.png")
    ImageResource complexKey();

    @Source("images/ConceptualEvent.png")
    ImageResource conceptualEvent();

    @Source("images/Depolymerization.png")
    ImageResource depolymerization();

    @Source("images/diagram_key_new.png")
    ImageResource diagramKey();

    @Source("images/downArrow.png")
    ImageResource downArrow();

    @Source("images/download.png")
    ImageResource download();

    @Source("images/download_file.png")
    ImageResource downloadFile();

    @Source("images/CandidateSet.png")
    ImageResource candidateSet();

    @Source("images/DefinedSet.png")
    ImageResource definedSet();

    @Source("images/Entity.png")
    ImageResource entity();

    @Source("images/EquivalentEventSet.png")
    ImageResource equivalentEventSet();

    @Source("images/GenomeEncodeEntity.png")
    ImageResource genomeEncodeEntity();

    @Source("images/SimpleEntity.png")
    ImageResource simpleEntity();

    @Source("images/eye.png")
    ImageResource eye();

    @Source("images/exclamation.png")
    ImageResource exclamation();

    @Source("images/show_diagram_key.png")
    ImageResource diagramKeyIcon();

    @Source("images/isDisease.png")
    ImageResource isDisease();

    @Source("images/isInferred.png")
    ImageResource isInferred();

    @Source("images/ORCID.png")
    ImageResource orcid();

    @Source("images/Pathway.png")
    ImageResource pathway();

    @Source("images/Polymerization.png")
    ImageResource polymerization();

    @Source("images/protein_key.png")
    ImageResource proteinKey();

    @Source("images/Reaction.png")
    ImageResource reaction();

    @Source("images/FailedReaction.gif")
    ImageResource failedReaction();

    @Source("images/NewTag.png")
    ImageResource newTag();

    @Source("images/UpdateTag.png")
    ImageResource updatedTag();

    @Source("images/upArrow.png")
    ImageResource upArrow();

    @Source("images/small_molecule_key.png")
    ImageResource smallMoleculeKey();

    @Source("images/loader.gif")
    ImageResource loader();

    @Source("images/details.png")
    ImageResource details();

    @Source("images/hierarchy.png")
    ImageResource hierarchy();

    @Source("images/diagram.png")
    ImageResource diagram();

    @Source("images/yes.png")
    ImageResource yes();

    @Source("images/next.png")
    ImageResource next();

    @Source("images/no.png")
    ImageResource no();

    @Source("images/summary_panel_example.png")
    ImageResource summaryPanelExample();
}