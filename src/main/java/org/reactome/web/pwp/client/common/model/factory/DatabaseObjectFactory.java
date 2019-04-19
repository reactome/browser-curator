package org.reactome.web.pwp.client.common.model.factory;

import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import org.reactome.web.pwp.client.common.model.classes.*;
import org.reactome.web.pwp.client.common.model.client.RESTFulClient;
import org.reactome.web.pwp.client.common.model.handlers.DatabaseObjectCreatedHandler;
import org.reactome.web.pwp.client.common.model.handlers.DatabaseObjectLoadedHandler;
import org.reactome.web.pwp.client.common.model.handlers.DatabaseObjectsCreatedHandler;
import org.reactome.web.pwp.client.common.model.util.LruCache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public abstract class DatabaseObjectFactory {

    private static final LruCache<String, DatabaseObject> cache = new LruCache<>(150);

    public static void get(Long dbId, DatabaseObjectCreatedHandler handler) {
        get("" + dbId, handler);
    }

    public static void get(String identifier, final DatabaseObjectCreatedHandler handler) {
        DatabaseObject databaseObject = cache.get(identifier);
        if(databaseObject!=null){
            handler.onDatabaseObjectLoaded(databaseObject);
            return;
        }
        String url = RESTFulClient.SERVER + RESTFulClient.RESTFUL_API_PATH + "detailedView/DatabaseObject/" + identifier;
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()){
                        case Response.SC_OK:
                            JSONObject json = JSONParser.parseStrict(response.getText()).isObject();
                            DatabaseObject databaseObject = create(json);
                            cache.put(databaseObject.getIdentifier(), databaseObject);
                            cache.put(databaseObject.getDbId().toString(), databaseObject); //TODO: Under test
                            handler.onDatabaseObjectLoaded(databaseObject);
                            break;
                        default:
                            handler.onDatabaseObjectError(new Exception(response.getStatusText()));
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    handler.onDatabaseObjectError(exception);
                }
            });
        } catch (RequestException e) {
            handler.onDatabaseObjectError(e);
        }
    }

    public static void get(Collection<?> identifiers, final DatabaseObjectsCreatedHandler handler){
        String url = RESTFulClient.SERVER + RESTFulClient.RESTFUL_API_PATH + "mapByIds/DatabaseObject";
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, url);
        requestBuilder.setHeader("Content-Type", "text/plain");
        requestBuilder.setHeader("Accept", "application/json");

        //In case the call is made without elements to query
        if(identifiers==null || identifiers.isEmpty()){
            handler.onDatabaseObjectsLoaded(new HashMap<String, DatabaseObject>());
            return;
        }

        StringBuilder sb = new StringBuilder("ID=");
        for (Object s : identifiers) {
            if(s instanceof String || s instanceof Long || s instanceof Integer) {
                sb.append(s.toString()).append(",");
            }else if(s instanceof DatabaseObject){
                sb.append(((DatabaseObject) s).getDbId()).append(",");
            }
        }
        sb.delete(sb.length() - 1, sb.length());
        String post = sb.toString();

        try {
            requestBuilder.sendRequest(post, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()) {
                        case Response.SC_OK:
                            Map<String, DatabaseObject> map = new HashMap<>();
                            JSONObject object = JSONParser.parseStrict(response.getText()).isObject();
                            for (String key : object.keySet()) {
                                DatabaseObject dbObject = create(object.get(key).isObject());
                                map.put(key, dbObject);
                            }
                            handler.onDatabaseObjectsLoaded(map);
                            break;
                        default:
                            handler.onDatabaseObjectError(new Exception(response.getStatusText()));
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    handler.onDatabaseObjectError(exception);
                }
            });
        } catch (RequestException e) {
            handler.onDatabaseObjectError(e);
        }
    }

    public static void load(final DatabaseObject databaseObject, final DatabaseObjectLoadedHandler handler){
        String url = RESTFulClient.SERVER + RESTFulClient.RESTFUL_API_PATH + "detailedView/DatabaseObject/" +  databaseObject.getDbId();
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()) {
                        case Response.SC_OK:
                            JSONObject json = JSONParser.parseStrict(response.getText()).isObject();
                            databaseObject.load(json);
                            cache.put(databaseObject.getIdentifier(), databaseObject);
                            handler.onDatabaseObjectLoaded(databaseObject);
                            break;
                        default:
                            handler.onDatabaseObjectError(new Exception(response.getStatusText()));
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    handler.onDatabaseObjectError(exception);
                }
            });
        } catch (RequestException e) {
            handler.onDatabaseObjectError(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static DatabaseObject create(JSONObject jsonObject) {
        SchemaClass schemaClass = DatabaseObjectUtils.getSchemaClass(jsonObject);

        if (schemaClass == null) {
            String msg = "WRONG SCHEMA CLASS. Schema class is empty for " + jsonObject.toString();
            throw new ModelFactoryException(msg);
        }

        DatabaseObject rtn;
        switch (schemaClass) {
            case AFFILIATION:
                rtn = new Affiliation();
                break;
            //case ABSTRACT_MODIFIED_RESIDUE:  //NOT USED HERE
            case BLACK_BOX_EVENT:
                rtn = new BlackBoxEvent();
                break;
            case BOOK:
                rtn = new Book();
                break;
            case CANDIDATE_SET:
                rtn = new CandidateSet();
                break;
            case CATALYST_ACTIVITY:
                rtn = new CatalystActivity();
                break;
            case CELL_TYPE:
                rtn = new CellType();
                break;
            case CHEMICAL_DRUG:
                rtn = new ChemicalDrug();
                break;
            case PROTEIN_DRUG:
                rtn = new ProteinDrug();
                break;
            case RNA_DRUG:
                rtn = new RNADrug();
                break;
            case COMPARTMENT:
                rtn = new Compartment();
                break;
            case COMPLEX:
                rtn = new Complex();
                break;
            case COMPLEX_DOMAIN:
                rtn = new ComplexDomain();
                break;
            //case CROSS_LINKED_RESIDUE: //NOT USED HERE
            case DATABASE_IDENTIFIER:
                rtn = new DatabaseIdentifier();
                break;
            //case DATABASE_OBJECT: //NOT USED HERE
            case DEFINED_SET:
                rtn = new DefinedSet();
                break;
            case DEPOLYMERISATION:
                rtn = new Depolymerisation();
                break;
            case DISEASE:
                rtn = new Disease();
                break;
            //case DOMAIN:  //NOT USED HERE
            case ENTITY_FUNCTIONAL_STATUS:
                rtn = new EntityFunctionalStatus();
                break;
            case ENTITY_SET:
                rtn = new EntitySet();
                break;
            case ENTITY_WITH_ACCESSIONED_SEQUENCE:
                rtn = new EntityWithAccessionedSequence();
                break;
            case EVIDENCE_TYPE:
                rtn = new EvidenceType();
                break;
            //case EXTERNAL_ONTOLOGY:   //NOT USED HERE
            case FAILED_REACTION:
                rtn = new FailedReaction();
                break;
            case FIGURE:
                rtn = new Figure();
                break;
            case FRAGMENT_DELETION_MODIFICATION:
                rtn = new FragmentDeletionModification();
                break;
            case FRAGMENT_INSERTION_MODIFICATION:
                rtn = new FragmentInsertionModification();
                break;
            case FRAGMENT_REPLACED_MODIFICATION:
                rtn = new FragmentReplacedModification();
                break;
            //case FRAGMENT_MODIFICATION:  //NOT USED HERE
            case FRONT_PAGE:
                rtn = new FrontPage();
                break;
            case FUNCTIONAL_STATUS:
                rtn = new FunctionalStatus();
                break;
            case FUNCTIONAL_STATUS_TYPE:
                rtn = new FunctionalStatusType();
                break;
            case GENERIC_DOMAIN:
                rtn = new GenericDomain();
                break;
            //case GENETICALLY_MODIFIED_RESIDUE:  //NOT USED HERE
            case GENOME_ENCODED_ENTITY:
                rtn = new GenomeEncodedEntity();
                break;
            case GO_BIOLOGICAL_PROCESS:
                rtn = new GO_BiologicalProcess();
                break;
            case GO_BIOLOGICAL_FUNCTION:
                rtn = new GO_MolecularFunction();
                break;
            case GO_CELLULAR_COMPONENT:
                rtn = new GO_CellularComponent();
                break;
            case GROUP_MODIFIED_RESIDUE:
                rtn = new GroupModifiedResidue();
                break;
            case INSTANCE_EDIT:
                rtn = new InstanceEdit();
                break;
            case INTER_CHAIN_CROSSLINKED_RESIDUE:
                rtn = new InterChainCrosslinkedResidue();
                break;
            case INTRA_CHAIN_CROSSLINKED_RESIDUE:
                rtn = new IntraChainCrosslinkedResidue();
                break;
            case LITERATURE_REFERENCE:
                rtn = new LiteratureReference();
                break;
            case MODIFIED_RESIDUE:
                rtn = new ModifiedResidue();
                break;
            case NEGATIVE_GENE_EXPRESSION_REGULATION:
                rtn = new NegativeGeneExpressionRegulation();
                break;
            case NEGATIVE_REGULATION:
                rtn = new NegativeRegulation();
                break;
            case OPEN_SET:
                rtn = new OpenSet();
                break;
            case OTHER_ENTITY:
                rtn = new OtherEntity();
                break;
            case PATHWAY:
                rtn = new Pathway();
                break;
            case PERSON:
                rtn = new Person();
                break;
            //case PHYSICAL_ENTITY: //NOT USED HERE
            case POLYMER:
                rtn = new Polymer();
                break;
            case POLYMERISATION:
                rtn = new Polymerisation();
                break;
            case POSITIVE_GENE_EXPRESSION_REGULATION:
                rtn = new PositiveGeneExpressionRegulation();
                break;
            case POSITIVE_REGULATION:
                rtn = new PositiveRegulation();
                break;
            case PSI_MOD:
                rtn = new PsiMod();
                break;
            //case PUBLICATION: //NOT USED HERE
            case REACTION:
                rtn = new Reaction();
                //case REACTION_LIKE_EVENT: //NOT USED HERE
                break;
            case REFERENCE_DATABASE:
                rtn = new ReferenceDatabase();
                break;
            case REFERENCE_DNA_SEQUENCE:
                rtn = new ReferenceDNASequence();
                //case REFERENCE_ENTITY:  //NOT USED HERE
                break;
            case REFERENCE_GENE_PRODUCT:
                rtn = new ReferenceGeneProduct();
                break;
            case REFERENCE_GROUP:
                rtn = new ReferenceGroup();
                break;
            case REFERENCE_ISOFORM:
                rtn = new ReferenceIsoform();
                break;
            case REFERENCE_MOLECULE:
                rtn = new ReferenceMolecule();
                break;
            case REFERENCE_RNA_SEQUENCE:
                rtn = new ReferenceRNASequence();
                break;
            case REFERENCE_THERAPEUTIC:
                rtn = new ReferenceTherapeutic();
                break;
            //case REFERENCE_SEQUENCE:  //NOT USED HERE
            case REGULATION:
                rtn = new Regulation();
                break;
            case REGULATION_TYPE:
                rtn = new RegulationType();
                break;
            case REPLACED_RESIDUE:
                rtn = new ReplacedResidue();
                break;
            case REQUIREMENT:
                rtn = new Requirement();
                break;
            case SEQUENCE_DOMAIN:
                rtn = new SequenceDomain();
                break;
            case SEQUENCE_ONTOLOGY:
                rtn = new SequenceOntology();
                break;
            case SIMPLE_ENTITY:
                rtn = new SimpleEntity();
                break;
            case SPECIES:
                rtn = new Species();
                break;
            case STABLE_IDENTIFIER:
                rtn = new StableIdentifier();
                break;
            case SUMMATION:
                rtn = new Summation();
                break;
            case TAXON:
                rtn = new Taxon();
                break;
            //case TRANSLATIONAL_MODIFICATION:   //NOT USED HERE
            case URL:
                rtn = new Url();
                break;
            default:
                String msg = "[Model Factory] -> Was impossible to instantiate " + jsonObject.toString();
                throw new ModelFactoryException(msg);
        }
        rtn.load(jsonObject);
        return rtn;
    }
}
