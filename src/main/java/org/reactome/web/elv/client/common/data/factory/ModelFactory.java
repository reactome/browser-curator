package org.reactome.web.elv.client.common.data.factory;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.model.*;
import org.reactome.web.elv.client.common.utils.Console;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public abstract class ModelFactory {

    public static DatabaseObject getDatabaseObject(JSONObject jsonObject) {
        SchemaClass schemaClass = FactoryUtils.getSchemaClass(jsonObject);

        if(schemaClass==null){
            String msg = "WRONG SCHEMA CLASS. Schema class is empty for " + jsonObject.toString();
            if(GWT.isScript()) Console.error(msg);
            throw new ModelFactoryException(msg);
        }

        switch (schemaClass){
            //case ABSTRACT_MODIFIED_RESIDUE:  //NOT USED HERE
            case BLACK_BOX_EVENT:
                return new BlackBoxEvent(jsonObject);
            case BOOK:
                return new Book(jsonObject);
            case CANDIDATE_SET:
                return new CandidateSet(jsonObject);
            case CATALYST_ACTIVITY:
                return new CatalystActivity(jsonObject);
            case CELL_TYPE:
                return new CellType(jsonObject);
            case COMPARTMENT:
                return new Compartment(jsonObject);
            case COMPLEX:
                return new Complex(jsonObject);
            case COMPLEX_DOMAIN:
                return new ComplexDomain(jsonObject);
            //case CROSS_LINKED_RESIDUE: //NOT USED HERE
            case DATABASE_IDENTIFIER:
                return new DatabaseIdentifier(jsonObject);
            //case DATABASE_OBJECT: //NOT USED HERE
            case DEFINED_SET:
                return  new DefinedSet(jsonObject);
            case DEPOLYMERISATION:
                return new Depolymerisation(jsonObject);
            case DISEASE:
                return new Disease(jsonObject);
            //case DOMAIN:  //NOT USED HERE
            case ENTITY_COMPARTMENT:
                return new EntityCompartment(jsonObject);
            case ENTITY_FUNCTIONAL_STATUS:
                return new EntityFunctionalStatus(jsonObject);
            case ENTITY_SET:
                return new EntitySet(jsonObject);
            case ENTITY_WITH_ACCESSIONED_SEQUENCE:
                return new EntityWithAccessionedSequence(jsonObject);
            case EVIDENCE_TYPE:
                return new EvidenceType(jsonObject);
            //case EXTERNAL_ONTOLOGY:   //NOT USED HERE
            case FAILED_REACTION:
                return new FailedReaction(jsonObject);
            case FIGURE:
                return new Figure(jsonObject);
            case FRAGMENT_DELETION_MODIFICATION:
                return new FragmentDeletionModification(jsonObject);
            case FRAGMENT_INSERTION_MODIFICATION:
                return new FragmentInsertionModification(jsonObject);
            //case FRAGMENT_MODIFICATION:  //NOT USED HERE
            case FRONT_PAGE:
                return new FrontPage(jsonObject);
            case FUNCTIONAL_STATUS:
                return new FunctionalStatus(jsonObject);
            case FUNCTIONAL_STATUS_TYPE:
                return new FunctionalStatusType(jsonObject);
            case GENERIC_DOMAIN:
                return new GenericDomain(jsonObject);
            //case GENETICALLY_MODIFIED_RESIDUE:  //NOT USED HERE
            case GENOME_ENCODED_ENTITY:
                return new GenomeEncodedEntity(jsonObject);
            case GO_BIOLOGICAL_PROCESS:
                return new GO_BiologicalProcess(jsonObject);
            case GO_BIOLOGICAL_FUNCTION:
                return new GO_MolecularFunction(jsonObject);
            case GO_CELLULAR_COMPONENT:
                return new GO_CellularComponent(jsonObject);
            case GROUP_MODIFIED_RESIDUE:
                return new GroupModifiedResidue(jsonObject);
            case INSTANCE_EDIT:
                return new InstanceEdit(jsonObject);
            case INTER_CHAIN_CROSSLINKED_RESIDUE:
                return new InterChainCrosslinkedResidue(jsonObject);
            case INTRA_CHAIN_CROSSLINKED_RESIDUE:
                return new IntraChainCrosslinkedResidue(jsonObject);
            case LITERATURE_REFERENCE:
                return new LiteratureReference(jsonObject);
            case MODIFIED_RESIDUE:
                return new ModifiedResidue(jsonObject);
            case NEGATIVE_REGULATION:
                return new NegativeRegulation(jsonObject);
            case OPEN_SET:
                return new OpenSet(jsonObject);
            case OTHER_ENTITY:
                return new OtherEntity(jsonObject);
            case PATHWAY:
                return new Pathway(jsonObject);
            case PERSON:
                return new Person(jsonObject);
            //case PHYSICAL_ENTITY: //NOT USED HERE
            case POLYMER:
                return new Polymer(jsonObject);
            case POLYMERISATION:
                return new Polymerisation(jsonObject);
            case POSITIVE_REGULATION:
                return new PositiveRegulation(jsonObject);
            case PSI_MOD:
                return new PsiMod(jsonObject);
            //case PUBLICATION: //NOT USED HERE
            case REACTION:
                return new Reaction(jsonObject);
            //case REACTION_LIKE_EVENT: //NOT USED HERE
            case REFERENCE_DATABASE:
                return new ReferenceDatabase(jsonObject);
            case REFERENCE_DNA_SEQUENCE:
                return new ReferenceDNASequence(jsonObject);
            //case REFERENCE_ENTITY:  //NOT USED HERE
            case REFERENCE_GENE_PRODUCT:
                return new ReferenceGeneProduct(jsonObject);
            case REFERENCE_GROUP:
                return new ReferenceGroup(jsonObject);
            case REFERENCE_ISOFORM:
                return new ReferenceIsoform(jsonObject);
            case REFERENCE_MOLECULE:
                return new ReferenceMolecule(jsonObject);
            case REFERENCE_RNA_SEQUENCE:
                return new ReferenceRNASequence(jsonObject);
            //case REFERENCE_SEQUENCE:  //NOT USED HERE
            case REGULATION:
                return new Regulation(jsonObject);
            case REGULATION_TYPE:
                return new RegulationType(jsonObject);
            case REPLACED_RESIDUE:
                return new ReplacedResidue(jsonObject);
            case REQUIREMENT:
                return new Requirement(jsonObject);
            case SEQUENCE_DOMAIN:
                return new SequenceDomain(jsonObject);
            case SEQUENCE_ONTOLOGY:
                return new SequenceOntology(jsonObject);
            case SIMPLE_ENTITY:
                return new SimpleEntity(jsonObject);
            case SPECIES:
                return new Species(jsonObject);
            case STABLE_IDENTIFIER:
                return new StableIdentifier(jsonObject);
            case SUMMATION:
                return new Summation(jsonObject);
            case TAXON:
                return new Taxon(jsonObject);
            //case TRANSLATIONAL_MODIFICATION:   //NOT USED HERE
            case URL:
                return new Url(jsonObject);
            default:
                String msg = "[Model Factory] -> Was impossible to instantiate " + jsonObject.toString();
                Console.error(msg);
                throw new ModelFactoryException(msg);
        }
    }
}
