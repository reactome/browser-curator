package org.reactome.web.elv.client.common.provider;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import org.reactome.web.elv.client.common.ReactomeImages;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;
import org.reactome.web.elv.client.common.data.model.EventStatus;
import org.reactome.web.elv.client.common.utils.Console;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public abstract class InstanceTypeIconProvider {

    public static ImageResource getItemIcon(SchemaClass schemaClass) {
        ImageResource img;

        switch (schemaClass) {
            case BLACK_BOX_EVENT:
                img = ReactomeImages.INSTANCE.blackBoxEvent();
                break;
            case CANDIDATE_SET:
                img = ReactomeImages.INSTANCE.candidateSet();
                break;
            case COMPLEX:
                img = ReactomeImages.INSTANCE.complex();
                break;
            /*case CONCEPTUAL_EVENT:
                img = ReactomeImages.INSTANCE.conceptualEvent();
                break;*/
            case DEFINED_SET:
                img = ReactomeImages.INSTANCE.definedSet();
                break;
            case DEPOLYMERISATION:
                img = ReactomeImages.INSTANCE.depolymerization();
                break;
//            case ENTITY_SET:
//                img = ReactomeImages.INSTANCE.set();
//                break;
            case REFERENCE_GENE_PRODUCT: //This is a hack to get the "proper icons" in the PM (need to be revisited)
            case REFERENCE_ISOFORM:
            case ENTITY_WITH_ACCESSIONED_SEQUENCE:
                img = ReactomeImages.INSTANCE.entityWithAccessionedSequence();
                break;
            /*case EQUIVALENT_EVENT_SET:
                img = ReactomeImages.INSTANCE.equivalentEventSet();
                break;*/
            case FAILED_REACTION:
                img = ReactomeImages.INSTANCE.failedReaction();
                break;
            case GENOME_ENCODED_ENTITY:
                img = ReactomeImages.INSTANCE.genomeEncodeEntity();
                break;
            case OPEN_SET:
                img = ReactomeImages.INSTANCE.openSet();
                break;
            case OTHER_ENTITY:
                img = ReactomeImages.INSTANCE.otherEntity();
                break;
            case PATHWAY:
                img = ReactomeImages.INSTANCE.pathway();
                break;
            case POLYMER:
                img = ReactomeImages.INSTANCE.polymer();
                break;
            case POLYMERISATION:
                img = ReactomeImages.INSTANCE.polymerization();
                break;
            case REACTION:
                img = ReactomeImages.INSTANCE.reaction();
                break;
            case REFERENCE_DNA_SEQUENCE:
                img = ReactomeImages.INSTANCE.referenceDNASequence();
                break;
            case REFERENCE_RNA_SEQUENCE:
                img = ReactomeImages.INSTANCE.referenceRNASequence();
                break;
            case REFERENCE_MOLECULE: //This is a hack to get the "proper icons" in the PM (need to be revisited)
            case SIMPLE_ENTITY:
                img = ReactomeImages.INSTANCE.simpleEntity();
                break;
            default:
                if(!GWT.isScript())
                    Console.error("InstanceTypeIconProvider -> Image not defined for " + schemaClass.toString());
                img = ReactomeImages.INSTANCE.exclamation();
        }

        return img;
    }

    public static ImageResource getItemStatusIcon(EventStatus status) {
        ImageResource img = null;
        switch (status){
            case NEW:
                img = ReactomeImages.INSTANCE.newTag();
                break;
            case UPDATED:
                img = ReactomeImages.INSTANCE.updatedTag();
                break;
        }
        return img;
    }
}
