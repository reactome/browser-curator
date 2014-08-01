package org.reactome.web.elv.client.details.tabs.molecules.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import org.reactome.web.elv.client.common.Controller;
import org.reactome.web.elv.client.common.EventBus;
import org.reactome.web.elv.client.common.data.factory.ModelFactory;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Event;
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
import org.reactome.web.elv.client.details.tabs.molecules.model.type.PathwayType;
import org.reactome.web.elv.client.details.tabs.molecules.view.MoleculesView;

import java.util.*;


/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
public class MoleculesPresenter extends Controller implements MoleculesView.Presenter, MoleculeSelectedHandler {
    private static final String PREFIX = "\t\t[MoleculesPres] -> ";
    private final MoleculesView view;
    private DatabaseObject currentDatabaseObject;
    private Pathway currentPathway;
    private final LRUCache<Pathway, Result> cachePathway = new LRUCache<Pathway, Result>(15);
    private final LRUCache<Long, HashSet<Molecule>> cacheDbObj= new LRUCache<Long, HashSet<Molecule>>(10);
    private final LRUCache<List<PhysicalToReferenceEntityMap>, List<Long>> cacheSubPathway
            = new LRUCache<List<PhysicalToReferenceEntityMap>, List<Long>>(15);

    private int count = 0;
    private List<PhysicalToReferenceEntityMap> toHighlight = new ArrayList<PhysicalToReferenceEntityMap>();
    private List<Long> subPWtoHighlight = new ArrayList<Long>();

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

    /**
     * Show instance details for one selected item in context of the lowest pathway with diagram.
     * Selected item and pathway can be the same.
     * @param pathway lowest pathway with diagram in hierachy
     * @param databaseObject selected item
     */
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
                    useExistingReactionParticipants(result, false);
                }else{
                    getReactionParticipants(result, urlReaction, false, false);
                }

            }
        }else{
            //Not yet cached data needs to be requested.
            getPathwayParticipants(urlPathway, urlReaction);
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
            useExistingReactionParticipants(result, true);
        }else{
            getReactionParticipants(result, urlReaction, true, false);
        }
    }

    /**
     * This method receives molecules data for a pathway and calls the method for reaction if necessary.
     * @param urlPathway Request-URL for RESTfulService
     * @param urlReaction Request-URL for RESTfulService
     */
    private void getPathwayParticipants(final String urlPathway, final String urlReaction) {
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
                            useExistingReactionParticipants(result, false);
                        }else{
                            getReactionParticipants(result, urlReaction, false, false);
                        }
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    //TODO
                    Console.error(PREFIX + "Sorry, getPathwayParticipants received an error instead of a response");
                    if(!GWT.isScript()){
                        Console.error(getClass() + exception.getMessage());
                    }
                }
            });
        } catch (RequestException ex) {
            //TODO
            Console.error(PREFIX + "Sorry, getPathwayParticipants caught an error!" );
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
                    Console.error(PREFIX + "Sorry, getReactionParticipants received an error instead of a response");
                    if(!GWT.isScript()){
                        Console.error(getClass() + exception.getMessage());
                    }
                }
            });
        } catch (RequestException ex) {
            //TODO
            Console.error(PREFIX + "Sorry, getReactionParticipants caught an error!" );
        }
    }

    /**
     * If a molecule is selected, meaning its icon was clicked, then the corresponding entity in the diagram needs to
     * be selected. Further clicks allow circling through all entities that are or contain this specific molecule.
     * @param physicalEntityList list of physical entities in diagram that are or contain this specific molecule.
     */
    @Override
    public void moleculeSelected(List<PhysicalToReferenceEntityMap> physicalEntityList) {
        view.setLoadingMsg(null);

        if(physicalEntityList != null){
            for(int i = 0; i < physicalEntityList.size() && i < toHighlight.size(); ++i){
                if(!physicalEntityList.get(i).getPeDbId().equals(toHighlight.get(i).getPeDbId())){
                    count = 0;
                    break;
                }
            }
            toHighlight = physicalEntityList;
        }

        if(count >= toHighlight.size()){
            count = 0; // Circling through all entities in toHighlight.
        }

        PathwayType type = determineType();
        if(type.equals(PathwayType.NGB)){
            selectEntity();
        }else if (type.equals(PathwayType.OGB)){
            if(!cacheSubPathway.containsKey(toHighlight)){
                pathwaysForEntities();
            }else{
                onSelectSubpathway();
            }
        }else{ //CGB
            String msg = "Functionality not yet available for this Diagram";
            view.setLoadingMsg(msg);

            com.google.gwt.user.client.Timer timer = new com.google.gwt.user.client.Timer(){
                @Override
                public void run() {
                    GWT.runAsync(new RunAsyncCallback() {
                        public void onFailure(Throwable caught) {
                            Console.warn("Ups, something went wrong in Molecules Tab. Take a look at MoleculesPresenter > moleculeSelected.");
                        }

                        public void onSuccess() {
                            view.clearLoadingMsg();
                        }
                    });
                }
            };
            timer.schedule(5000);
//            try{
//                pathwaysForEntities();
//            }catch (Exception e){
//                selectEntity();
//            }
        }
    }

    /**
     * Method to determine the type of the currently displayed diagram
     * @return PathwayType (OGB, NGB, CGB)
     */
    private PathwayType determineType() {
        List<Event> events = this.currentPathway.getHasEvent();
        PathwayType pt = null;
        for(Event e : events){
            if (e.getSchemaClass() == SchemaClass.PATHWAY){
                Pathway p = (Pathway) e;

                if(pt == null){
                    if(p.getHasDiagram()){
                        pt = PathwayType.OGB; //only green boxes
                    }else{
                        pt = PathwayType.NGB; //no green boxes
                    }
                }else{
                    if(p.getHasDiagram() && pt == PathwayType.NGB || !p.getHasDiagram() && pt == PathwayType.OGB){
                        return PathwayType.CGB; //diagram that contains green boxes
                    }
                }
            }
        }
        return pt;
    }

    /**
     * Post all pDbIds from toHighlight to get a list of pathways that contain all elements.
     */
    private void pathwaysForEntities() {
        String url = "/ReactomeRESTfulAPI/RESTfulWS/pathwaysForEntities/";
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, url);
        requestBuilder.setHeader("Content-Type", "text/plain");
        requestBuilder.setHeader("Accept", "application/json");

        try {
            String post = "ID=";
            for(PhysicalToReferenceEntityMap entity : toHighlight){
                post += entity.getPeDbId() + ",";
            }
            requestBuilder.sendRequest(post, new RequestCallback() {

                @Override
                public void onResponseReceived(Request request, Response response) {
                    JSONArray list = JSONParser.parseStrict(response.getText()).isArray();

                    ArrayList<Pathway> pathways = new ArrayList<Pathway>();
                    for(int i=0; i<list.size(); ++i){
                        JSONObject object = list.get(i).isObject();
                        pathways.add(new Pathway(object));
                    }

//                    TODO: How about handling molecules that exist in gb as well as in currently displayed diagram?!
                    for (Pathway pathway : pathways) {
                        queryEventAncestors(pathway.getDbId());
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    //TODO
                    Console.error(PREFIX + "Sorry, pathwaysForEntities received an error instead of a response");
                    if(!GWT.isScript()){
                        Console.error(getClass() + exception.getMessage());
                    }
                }

            });
        } catch (RequestException ex) {
            //TODO
            Console.error(PREFIX + "Sorry, pathwaysForEntities received an error instead of a response");
        }
    }

    /**
     * Get a partial hierachry for a specific (sub)pathway. If a (sub)pathway in hierarchy that is one level
     * below the currently displayed one exists than highlight it in the Diagram. If it does not exist the
     * toHighlight elements should be found in the currently displayed Diagram.
     * @param dbId pDbId of subpathway
     */
    private void queryEventAncestors(Long dbId) {
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, "/ReactomeRESTfulAPI/RESTfulWS/queryEventAncestors/" + dbId);
        requestBuilder.setHeader("Accept", "application/json");

        try {
            requestBuilder.sendRequest(null, new RequestCallback() {

                public void onResponseReceived(Request request, Response response) {
                    JSONArray list = JSONParser.parseStrict(response.getText()).isArray();

                    //Creating a list of pathways (that are above the one that contains all toHighlight)
                    ArrayList<Pathway> pathways = new ArrayList<Pathway>();
                    for(int i=0; i<list.size(); ++i){
                        JSONObject object = list.get(i).isObject();
                        JSONArray array = (JSONArray) object.get("databaseObject");

                        for(int j=0; j<array.size(); ++j){
                            JSONObject pw = array.get(j).isObject();
                            pathways.add(new Pathway(pw));
                        }
                    }

//                    if(pathways.toString().contains(currentPathway.getDbId().toString())){
                    for(int i = 0; i < pathways.size(); i++){
                        if(currentPathway.getDbId().equals(pathways.get(i).getDbId())){
                            if(i+1 < pathways.size() && pathways.get(i+1).getHasDiagram()){
                                subPWtoHighlight.add(pathways.get(i + 1).getDbId());
                                cacheSubPathway.put(toHighlight, subPWtoHighlight);
                                try {
                                    onSelectSubpathway();
                                }catch (Exception e){
                                    selectEntity();
                                }
                            }else{
                                //Entity in CUGB diagram
                                selectEntity();
//                                return;
                            }
                        }
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    //TODO
                    Console.error(PREFIX + "Sorry, queryEventAncestors received an error instead of a response");
                    if(!GWT.isScript()){
                        Console.error(getClass() + exception.getMessage());
                    }
                }
            });
        } catch (RequestException ex) {
            //TODO
            Console.error(PREFIX + "Sorry, queryEventAncestors caught an error!" );
        }
    }

    /**
     * Select a specific entity (identified by its peDbId) in the Diagram.
     */
    private void selectEntity() {
        if(toHighlight.size() <= count){
            count = 0; // for circling through
        }
        Long peDbId = toHighlight.get(count).getPeDbId();
        ++count;

        //Delegation mechanism
        Pair<Long, ELVEventType> tuple = new Pair<Long, ELVEventType>(peDbId, ELVEventType.MOLECULES_ITEM_SELECTED);
        this.eventBus.fireELVEvent(ELVEventType.DATABASE_OBJECT_REQUIRED, tuple);
        view.clearLoadingMsg();
    }

    /**
     * Select a specific subpathway (which contains all toHighlight) in the Diagram.
     */
    private void onSelectSubpathway() {
        //After all queryEventAncestors have been found => highlightSubPW();
        if(cacheSubPathway.containsKey(toHighlight) && cacheSubPathway.get(toHighlight).size() > count){
            Pair<Long, ELVEventType> tuple = new Pair<Long, ELVEventType>(cacheSubPathway.get(toHighlight).get(count), ELVEventType.DIAGRAM_ENTITY_SELECTED);
            eventBus.fireELVEvent(ELVEventType.DATABASE_OBJECT_REQUIRED, tuple);
            ++count;
        }
        view.clearLoadingMsg();
    }

    /**
     * Enables tracking the frequency users use available download.
     */
    @Override
    public void moleculeDownloadStarted() {
        this.eventBus.fireELVEvent(ELVEventType.MOLECULES_DOWNLOAD_STARTED);
    }

    /**
     * Get molecules numbers for headline of tab.
     * @param pathway lowest pathway with diagram in hierachy
     * @param databaseObject selected item
     */
    @Override
    public void getMoleculeNumbers(DatabaseObject pathway, DatabaseObject databaseObject) {
        currentPathway = (Pathway) pathway;
        currentDatabaseObject = databaseObject;
        //String urlPathway  = "/ReactomeRESTfulAPI/RESTfulWS/getParticipantsToReferenceEntityMaps/" + pathway.getDbId();
        String urlReaction = "/ReactomeRESTfulAPI/RESTfulWS/referenceEntity/" + databaseObject.getDbId();

        if(!cachePathway.containsKey(pathway)){
            /* Pathway has not been loaded yet => don't show any numbers. */
            view.refreshTitle(null, null);
        }else{
            Result result = cachePathway.get(pathway);

            if(pathway.getDbId().equals(databaseObject.getDbId())){
                result.highlight();
                //view.setMoleculesData(result);
            }else{
                result.undoHighlighting(); //Previous highlighting needs to be undone before new one can be applied.

                /* Instead of using useExistingReactionParticipants(result, false); this has to be done here manually
                 * to avoid unnecessary reload of view and to keep download view if active. */
                 if(cacheDbObj.containsKey(databaseObject.getDbId())){
                    HashSet<Molecule> molecules = new HashSet<Molecule>(cacheDbObj.get(currentDatabaseObject.getDbId()));
                    for(Molecule molecule : molecules){
                        result.highlight(molecule);
                    }

                }else{
                    getReactionParticipants(result, urlReaction, false, true);
                }
            }
            view.refreshTitle(result.getNumberOfHighlightedMolecules(), result.getNumberOfMolecules());
        }

    }

    /**
     * If ReactionParticipants have already been loaded and are still stored in the cach then those should be used
     * to avoid requesting them again from the RESTful.
     * @param result current result
     * @param update decides whether view should be updated (true) or newly set (false)
     */
    private void useExistingReactionParticipants(Result result, boolean update){
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

    /**
     * Used if DownloadTab requires MoleculesDownload
     * @param pathway current selected pathway
     */
    @Override
    public void onMoleculesDownloadRequired(Pathway pathway){
        if(!cachePathway.containsKey(pathway)){
            this.currentPathway = pathway;
            this.currentDatabaseObject = pathway;
            view.setCurrentPanel(pathway, true);
        }else{
            view.moleculesDownloadRequired();
        }
    }

}