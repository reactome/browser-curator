package org.reactome.web.elv.client.manager.state;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;
import org.reactome.web.elv.client.center.model.CenterToolType;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Event;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.common.data.model.Species;
import org.reactome.web.elv.client.details.model.DetailsTabType;

import java.util.*;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class AdvancedState {

    public interface AdvancedStateLoadedHandler {
        void onAdvancedStateLoaded(AdvancedState state);
    }

    public static final Long DEFAULT_SPECIES_ID = 48887L; //Homo sapiens
    private static final String DELIMITER = "&";
    private boolean stateCorrect;

    private Map<AdvancedStateKey, String> toLoad;
    private AdvancedStateHelper helper;
    private AdvancedStateLoadedHandler handler;

    private Species species = null;
    private Pathway pathway = null;
    private DatabaseObject instance = null;
    private List<Event> path;
    private DetailsTabType detailsTab;
    private CenterToolType tool;
    private String analysisToken = null;

    public static void create(String token, AdvancedStateLoadedHandler handler){
        new AdvancedState(token, handler);
    }

    public AdvancedState() {
        this.toLoad = new HashMap<AdvancedStateKey, String>();
        this.stateCorrect = true;
        this.helper = new AdvancedStateHelper(this);
        this.path = new LinkedList<Event>();
        this.detailsTab = DetailsTabType.getDefault();
        this.tool = null; //Initially there is no tool selected. The help view is shown
    }

    public AdvancedState(AdvancedState state){
        this();
        this.species = state.species;
        this.pathway = state.pathway;
        this.instance = state.instance;
        this.path = state.path;
        this.detailsTab = state.detailsTab;
        this.tool = state.tool;
        this.analysisToken = state.analysisToken;
    }

    private AdvancedState(String token, AdvancedStateLoadedHandler handler) {
        this();
        this.handler = handler;

        // DO NOT MOVE THIS TO THE CONSTRUCTOR WITHOUT PARAMETERS
        // DO NOT USE THE HELPER TO LOAD THE DATA HERE
        toLoad.put(AdvancedStateKey.SPECIES, String.valueOf(DEFAULT_SPECIES_ID));

        if(!token.isEmpty()){
            try{
                @SuppressWarnings("NonJREEmulationClassesInClientCode")
                String[] tokens = token.split(DELIMITER);
                for (String t : tokens) {
                    @SuppressWarnings("NonJREEmulationClassesInClientCode")
                    String[] ts = t.split("=");
                    AdvancedStateKey key = AdvancedStateKey.getAdvancedStateKey(ts[0]);
                    if(key!=null){
                        toLoad.put(key, ts[1]);
                    }
                }
            }catch (Exception e){
                stateCorrect = false;
            }
        }

        if(toLoad.isEmpty()){
            setLoaded();
        }else{
            //the keySet needs to be kept separately to avoid a ConcurrentModificationException
            Set<AdvancedStateKey> keys = new HashSet<AdvancedStateKey>(toLoad.keySet());
            for (AdvancedStateKey key : keys) {
                String identifier = toLoad.get(key);
                switch (key){
                    case DETAILS_TAB:
                        this.setDetailsTab(DetailsTabType.getByCode(identifier));
                        break;
                    case TOOL:
                        this.setCenterTool(CenterToolType.getByCode(identifier));
                        break;
                    case ANALYSIS:
                        this.setAnalysisToken(identifier);
                        break;
                    case PATH:
                        helper.setPathEvents(identifier.split(","));
                        break;
                    default:
                        helper.setDatabaseObject(key, identifier);
                        break;
                }
            }
        }
    }

    public boolean isCorrect(){
        boolean correct = true;
        if(this.instance!=null)
            correct = this.pathway!=null;
        return this.stateCorrect && correct;
    }

    public boolean isInstancesInitialState(){
        return pathway==null && instance ==null;
    }

    public boolean isToolInitialState(){
        return isInstancesInitialState() && (tool==CenterToolType.getDefault() || tool==null);
    }

    public void resetInstancesState(){
        this.pathway = null;
        this.instance = null;
    }

    //Since the center contains more than the DIAGRAM, it has become a TOOL to load by default
    //when a pathway is selected. It has to be check here to ensure it is displayed
    protected void changeCenterToolIfNeeded(boolean force){
        if(force && this.pathway!=null){
            this.setCenterTool(CenterToolType.getDefault());
        }else{
            if(this.pathway!=null && tool==null){
                this.setCenterTool(CenterToolType.getDefault());
            }
        }
    }

    private void checkComplete(AdvancedStateKey loadedKey){
        if(!this.toLoad.isEmpty() && this.handler!=null){
            this.toLoad.remove(loadedKey);
            if(this.toLoad.isEmpty()){
                setLoaded();
            }
        }
    }

    public Species getSpecies() {
        return species;
    }

    public Pathway getPathway() {
        return pathway;
    }

    public DatabaseObject getInstance() {
        return instance;
    }

    public List<Event> getPath() {
        return path;
    }

    public DetailsTabType getDetailsTab() {
        return detailsTab;
    }

    public CenterToolType getCenterTool(){
        return tool;
    }

    public String getAnalysisToken() {
        return analysisToken;
    }

    public boolean hasReachToolState(AdvancedState state){
        if(this.tool==null){
            return state.getCenterTool() == null;
        }else{
            return tool.equals(state.getCenterTool());
        }
    }

    public boolean hasReachedAnalysisState(AdvancedState state){
        return !(analysisToken != null ? !analysisToken.equals(state.analysisToken) : state.analysisToken != null);
    }

    public boolean hasReachedSpeciesState(AdvancedState state){
        return species != null && species.equals(state.species);
    }

    @SuppressWarnings("RedundantIfStatement")
    public boolean hasReachedInstancesState(AdvancedState state){
        if (!Arrays.equals(path.toArray(), state.path.toArray()))
            return false;
        if (pathway != null ? !pathway.equals(state.pathway) : state.pathway != null)
            return false;
        if (instance != null ? !instance.equals(state.instance) : state.instance != null)
            return false;
        return true;
    }

    public boolean hasReachedTabState(AdvancedState state){
        return detailsTab.equals(state.detailsTab);
    }

    @SuppressWarnings("UnusedDeclaration")
    public boolean isStateCorrect() {
        return stateCorrect;
    }

    private void setLoaded(){
        final AdvancedState state = this;
        Scheduler.get().scheduleDeferred(new Command() {
            public void execute () {
                handler.onAdvancedStateLoaded(state);
            }
        });
    }

    public void setSpecies(Species species) {
        this.species = species;
        this.checkComplete(AdvancedStateKey.SPECIES);
    }

    public void setPathway(Pathway pathway) {
        this.pathway = pathway;
        this.checkComplete(AdvancedStateKey.DIAGRAM);
    }

    public void setInstance(DatabaseObject instance) {
        if(this.pathway!=null && this.pathway.equals(instance)){
            this.instance = null;
        }else{
            this.instance = instance;
        }
        this.checkComplete(AdvancedStateKey.INSTANCE);
    }

    public void setPath(List<Event> path) {
        this.path = path;
        this.checkComplete(AdvancedStateKey.PATH);
    }

    public void setDetailsTab(DetailsTabType detailsTab) {
        this.detailsTab = detailsTab;
        this.checkComplete(AdvancedStateKey.DETAILS_TAB);
    }

    public void setCenterTool(CenterToolType centerTool){
        this.tool = centerTool;
        this.checkComplete(AdvancedStateKey.TOOL);
    }

    public void setAnalysisToken(String analysisToken) {
        this.analysisToken = analysisToken;
        this.checkComplete(AdvancedStateKey.ANALYSIS);
    }

    /**
     * Returns the path until the loaded diagram (without including it)
     *
     * @return the path until the loaded diagram (without including it)
     */
    private List<Event> getPrunedPath() {
        List<Event> prunedPath = new LinkedList<Event>();
        for (Event event : this.path) {
            if (event.equals(this.pathway)) {
                break;
            }
            prunedPath.add(event);
        }
        return prunedPath;
    }

    @Override
    public String toString() {
        StringBuilder token = new StringBuilder();
        boolean addDelimiter = false;
        if (species != null && !species.getDbId().equals(DEFAULT_SPECIES_ID)) {
            token.append(AdvancedStateKey.SPECIES.getDefaultKey());
            token.append("=");
            token.append(species.getIdentifier());
            addDelimiter = true;
        }
        if (pathway != null) {
            if (addDelimiter) token.append(DELIMITER);
            token.append(AdvancedStateKey.DIAGRAM.getDefaultKey());
            token.append("=");
            token.append(pathway.getIdentifier());
            addDelimiter = true;
        }
        if (instance != null) {
            if (addDelimiter) token.append(DELIMITER);
            token.append(AdvancedStateKey.INSTANCE.getDefaultKey());
            token.append("=");
            token.append(instance.getIdentifier());
            addDelimiter = true;
        }
        List<Event> prunePath = this.getPrunedPath();
        if (!prunePath.isEmpty()) {
            if (addDelimiter) token.append(DELIMITER);
            token.append(AdvancedStateKey.PATH.getDefaultKey());
            token.append("=");
            for (Event event : prunePath) {
                token.append(event.getIdentifier());
                token.append(",");
            }
            token.deleteCharAt(token.length() - 1);
            addDelimiter = true;
        }
        if (detailsTab != null && !detailsTab.equals(DetailsTabType.getDefault())) {
            if (addDelimiter) token.append(DELIMITER);
            token.append(AdvancedStateKey.DETAILS_TAB.getDefaultKey());
            token.append("=");
            token.append(detailsTab.getCode());
            addDelimiter = true;
        }
        if(tool != null && !tool.equals(CenterToolType.getDefault())){
            if(addDelimiter) token.append(DELIMITER);
            token.append(AdvancedStateKey.TOOL.getDefaultKey());
            token.append("=").append(tool.getCode());
            addDelimiter = true;
        }

        if (analysisToken != null && !analysisToken.isEmpty()) {
            if (addDelimiter) token.append(DELIMITER);
            token.append(AdvancedStateKey.ANALYSIS.getDefaultKey());
            token.append("=");
            token.append(analysisToken);
            //addDelimiter=true;
        }
        return token.toString();
    }
}
