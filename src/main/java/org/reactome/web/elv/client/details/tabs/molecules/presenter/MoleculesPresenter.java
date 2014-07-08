package org.reactome.web.elv.client.details.tabs.molecules.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import org.reactome.web.elv.client.common.Controller;
import org.reactome.web.elv.client.common.EventBus;
import org.reactome.web.elv.client.common.data.factory.ModelFactory;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.common.events.ELVEventType;
import org.reactome.web.elv.client.common.model.Pair;
import org.reactome.web.elv.client.common.utils.Console;
import org.reactome.web.elv.client.common.utils.MapSet;
import org.reactome.web.elv.client.details.events.MoleculeSelectedHandler;
import org.reactome.web.elv.client.details.events.MoleculeSelectedListener;
import org.reactome.web.elv.client.details.tabs.DetailsTabView;
import org.reactome.web.elv.client.details.tabs.molecules.model.data.Molecule;
import org.reactome.web.elv.client.details.tabs.molecules.model.data.PhysicalToReferenceEntityMap;
import org.reactome.web.elv.client.details.tabs.molecules.model.data.Result;
import org.reactome.web.elv.client.details.tabs.molecules.view.MoleculesView;

import java.util.*;


/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
public class MoleculesPresenter extends Controller implements MoleculesView.Presenter, MoleculeSelectedHandler {
    private static final String PREFIX = "\t\t[MoleculesPres] -> ";
    private MoleculesView view;
    private DatabaseObject currentDatabaseObject;
    private Pathway currentPathway;
    private LRUCache<Pathway, Result> cachePathway = new LRUCache<Pathway, Result>(10);
    private LRUCache<Long, HashSet<Molecule>> cacheDbObj= new LRUCache<Long, HashSet<Molecule>>(10);

    int count = 0;
    List<PhysicalToReferenceEntityMap> toHighlight = new ArrayList<PhysicalToReferenceEntityMap>();

    public MoleculesPresenter(EventBus eventBus, MoleculesView view) {
        super(eventBus);
        this.view = view;
        this.view.setPresenter(this);

        //Needed because the view implementation will be listening to all the MoleculesPanel for molecules to be selected
        MoleculeSelectedListener.getMoleculeSelectedListener().setMoleculeSelectedHandler(this);
    }

    @Override
    public DetailsTabView getView() {
        return this.view;
    }

    @Override
    public void setInstancesInitialState() {
        currentDatabaseObject = null;
        view.setInitialState();
    }

    @Override
    public void showInstanceDetails(Pathway pathway, DatabaseObject databaseObject) {
        if(databaseObject != null){
            currentDatabaseObject = databaseObject;
            currentPathway = pathway;
        }else{
            currentDatabaseObject = pathway;
            currentPathway = pathway;
        }
        view.showInstanceDetails(currentPathway, currentDatabaseObject);
    }

    @Override
    public void showInstanceDetailsIfExists(Pathway pathway, DatabaseObject databaseObject) {
        this.view.showInstanceDetailsIfExists(pathway, databaseObject);
    }

    /**
     * Getting the data of all participating molecules in a Pathway-with-Diagram,
     * creating the intersection of participants in Pathway and selected Event or PhysicalEntity
     */
    @Override
    public void getMoleculesData() {
//        String urlPathway  = "/ReactomeRESTfulAPI/RESTfulWS/getPhysicalToReferenceEntityMaps/" + currentPathway.getDbId();
        String urlPathway  = "/ReactomeRESTfulAPI/RESTfulWS/getParticipantsToReferenceEntityMaps/" + currentPathway.getDbId();
        String urlReaction = "/ReactomeRESTfulAPI/RESTfulWS/referenceEntity/" + currentDatabaseObject.getDbId();
        if(cachePathway.containsKey(currentPathway)){
            Result result = cachePathway.get(currentPathway);
            if(currentPathway.getDbId().equals(currentDatabaseObject.getDbId())){
                result.highlight();
                view.setMoleculesData(result);
            }else{
                result.undoHighlighting(); //Previous highlighting needs to be undone before new one can be applied.

                if(cacheDbObj.containsKey(currentDatabaseObject.getDbId())){
                    useExistingParticipants(result, false);
                }else{
                    getReactionParticipants(result, urlReaction, false, false);
                }

            }
        }else{
            //Not yet cached data needs to be requested.
            getPathwayParticipants(urlPathway, urlReaction, false);
        }
    }

    /**
     * Update list of molecules as far as highlighting is concerned
     * in case that Pathway-with-Diagram has already been loaded.
     */
    @Override
    public void updateMoleculesData() {
        String urlReaction = "/ReactomeRESTfulAPI/RESTfulWS/referenceEntity/" + currentDatabaseObject.getDbId();

        Result result = cachePathway.get(currentPathway);
        result.undoHighlighting();

        if(cacheDbObj.containsKey(currentDatabaseObject.getDbId())){
            useExistingParticipants(result, true);
        }else{
            getReactionParticipants(result, urlReaction, true, false);
        }
    }

    /**
     * This method receives molecules data for a pathway and calls the method for reaction if necessary.
     * @param urlPathway Request-URL for RESTfulService
     * @param urlReaction Request-URL for RESTfulService
     */
    private void getPathwayParticipants(final String urlPathway, final String urlReaction, final boolean refreshTitle) {
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, urlPathway);
        requestBuilder.setHeader("Accept", "application/json");

        final MapSet<PhysicalToReferenceEntityMap, Molecule> mapSet = new MapSet<PhysicalToReferenceEntityMap, Molecule>();
        final ArrayList<Molecule> resultList = new ArrayList<Molecule>();

        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    JSONArray jsonArray = JSONParser.parseStrict(response.getText()).isArray();
                    for(int i=0; i< jsonArray.size(); ++i){
                        //Splitting necessary to get the set of referenceEntities that map to specific peID.
                        String[] split = jsonArray.get(i).toString().split("\"refEntities\":");

                        //Getting the key.
                        String subKey = split[0];
                        subKey = subKey.substring(0, subKey.length()-2) + "}";
                        JSONObject keyObj = JSONParser.parseStrict(subKey).isObject();
                        PhysicalToReferenceEntityMap physicalEntity = new PhysicalToReferenceEntityMap(keyObj);

                        //Getting the set of referenceEntities.
                        String subSet = split[split.length - 1].subSequence(0, split[split.length - 1].length()-1).toString();
                        JSONArray jsonArray2 = JSONParser.parseStrict(subSet).isArray();
                        ArrayList<Molecule> refEntitySet = new ArrayList<Molecule>();
                        for(int j = 0; j < jsonArray2.size(); ++j){
                            JSONObject object = jsonArray2.get(j).isObject();
                            refEntitySet.add(new Molecule(ModelFactory.getDatabaseObject(object).getSchemaClass(), object));
                        }
                        resultList.addAll(refEntitySet);
                        mapSet.add(physicalEntity, refEntitySet);
                    }

                    //Counting the occurrences of each molecule.
                    HashMap<Molecule, Integer> count = new HashMap<Molecule, Integer>();
                    for(Molecule m : resultList){
                        int value = 1;
                        if(count.containsKey(m)){
                            value = count.get(m) + 1;
                        }
                        count.put(m, value);
                    }

                    resultList.clear();

                    //Storing the occurrence-information in attribute of molecule.
                    for (Map.Entry<Molecule, Integer> entry : count.entrySet()) {
                        Molecule key = entry.getKey();
                        Integer value = entry.getValue();
                        key.setOccurrenceInPathway(value);
                        resultList.add(key);
                    }

                    Result result = new Result(resultList); //resultList is now duplicate-free.
                    result.setPhyEntityToRefEntitySet(mapSet);
                    cachePathway.put(currentPathway, result);
                    if(currentPathway.getDbId().equals(currentDatabaseObject.getDbId())){
                        result.highlight();
                        view.setMoleculesData(result);
                    }else{
                        if(cacheDbObj.containsKey(currentDatabaseObject.getDbId())){
                            useExistingParticipants(result, false);
                        }else{
                            getReactionParticipants(result, urlReaction, false, refreshTitle);
                        }
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    //TODO
                    Console.error(PREFIX + "sry, getPhysicalToReferenceEntityMaps received an error instead of a response");
                    if(!GWT.isScript()){
                        Console.error(getClass() + exception.getMessage());
                    }
                }
            });
        } catch (RequestException ex) {
            //TODO
            Console.error(PREFIX + "sry, getPhysicalToReferenceEntityMaps caught an error!" );
        }
    }

    /**
     * This method receives molecules data for a reaction and either sets or updates it in the view.
     * @param result previous result, needed for intersection
     * @param urlReaction Request-URL for RESTfulService
     * @param update boolean if update necessary
     */
    private void getReactionParticipants(final Result result, String urlReaction, final boolean update, final boolean refreshTitle) {
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, urlReaction);
        requestBuilder.setHeader("Accept", "application/json");

        try {
            requestBuilder.sendRequest(null, new RequestCallback() {

                public void onResponseReceived(Request request, Response response) {
                    // Parsing the result text to a JSONArray because a reaction can contain many elements.
                    JSONArray jsonArray = JSONParser.parseStrict(response.getText()).isArray();

                    Molecule molecule;
                    HashSet<Molecule> molecules = new HashSet<Molecule>();
                    for(int i=0; i<jsonArray.size(); ++i){
                        JSONObject object = jsonArray.get(i).isObject();
                        molecule = new Molecule(ModelFactory.getDatabaseObject(object).getSchemaClass(), object);
                        result.highlight(molecule);
                        molecules.add(molecule);
                    }
                    cacheDbObj.put(currentDatabaseObject.getDbId(), molecules);

                    if(update){
                        view.updateMoleculesData(result);
                    }else if(refreshTitle){
                        view.refreshTitle(result.getNumberOfHighlightedMolecules(), result.getNumberOfMolecules());
                    }else {
                        view.setMoleculesData(result);
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    //TODO
                    Console.error(PREFIX + "sry, getPathwayParticipants received an error instead of a response");
                    if(!GWT.isScript()){
                        Console.error(getClass() + exception.getMessage());
                    }
                }
            });
        } catch (RequestException ex) {
            //TODO
            Console.error(PREFIX + "sry, getPathwayParticipants caught an error!" );
        }
    }

    @Override
    public void moleculeSelected(List<PhysicalToReferenceEntityMap> physicalEntityList) {
        if(physicalEntityList != null){
            for(int i = 0; i < physicalEntityList.size() && i < toHighlight.size(); ++i){
                if(!physicalEntityList.get(i).getPeDbId().equals(toHighlight.get(i).getPeDbId())){
                    count = 0;
                    break;
                }
            }
            toHighlight = physicalEntityList;
        }

        Long peDbId;
        if(count >= toHighlight.size()){
            count = 0; // Circling through all entities in toHighlight.
        }
        peDbId = toHighlight.get(count).getPeDbId();
        ++count;

        //Delegation mechanism
        Pair<Long, ELVEventType> tuple = new Pair<Long, ELVEventType>(peDbId, ELVEventType.MOLECULES_ITEM_SELECTED);
        this.eventBus.fireELVEvent(ELVEventType.DATABASE_OBJECT_REQUIRED, tuple);
    }

    @Override
    public void moleculeDownloadStarted() {
        this.eventBus.fireELVEvent(ELVEventType.MOLECULES_DOWNLOAD_STARTED);
    }

    @Override
    public void getMoleculeNumbers(DatabaseObject pathway, DatabaseObject databaseObject) {
        currentPathway = (Pathway) pathway;
        currentDatabaseObject = databaseObject;
        String urlPathway  = "/ReactomeRESTfulAPI/RESTfulWS/getParticipantsToReferenceEntityMaps/" + pathway.getDbId();
        String urlReaction = "/ReactomeRESTfulAPI/RESTfulWS/referenceEntity/" + databaseObject.getDbId();

        if(!cachePathway.containsKey(pathway)){
            /* Boolean to avoid complete reset of view, but function call necessary to get numbers of highlighted and
            * total number of molecules. Results are cached anyway.*/
            getPathwayParticipants(urlPathway, urlReaction, true);
        }else{
            Result result = cachePathway.get(pathway);

            if(pathway.getDbId().equals(databaseObject.getDbId())){
                result.highlight();
                view.setMoleculesData(result);
            }else{
                result.undoHighlighting(); //Previous highlighting needs to be undone before new one can be applied.

                if(cacheDbObj.containsKey(databaseObject.getDbId())){
                    useExistingParticipants(result, false);
                }else{
                    getReactionParticipants(result, urlReaction, false, true);
                }
            }

            view.refreshTitle(result.getNumberOfHighlightedMolecules(), result.getNumberOfMolecules());
        }

    }

    private void useExistingParticipants(Result result, boolean update){
        HashSet<Molecule> molecules = new HashSet<Molecule>(cacheDbObj.get(currentDatabaseObject.getDbId()));
        for(Molecule molecule : molecules){
            result.highlight(molecule);
        }

        if(update){
            view.updateMoleculesData(result);
        }else{
            view.setMoleculesData(result);
        }
    }
}