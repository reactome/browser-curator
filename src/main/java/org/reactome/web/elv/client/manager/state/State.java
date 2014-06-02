package org.reactome.web.elv.client.manager.state;

import org.reactome.web.elv.client.details.model.DetailsTabType;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("RedundantIfStatement")
@Deprecated
public class State {
    private static final String DELIMITER = "&";

    private boolean stateCorrect;

    public static final Long DEFAULT_SPECIES_ID = 48887L; //Homo sapiens
    private Long loadedSpecies;
    private static final String loadedSpeciesKey = "FOCUS_SPECIES_ID";

    private Long loadedDiagram;
    private static final String loadedDiagramKey = "FOCUS_PATHWAY_ID";

    private Long selectedInstance;
    private static final String selectedInstanceKey = "ID";

    private List<Long> loadedPath = new LinkedList<Long>();
    private static final String loadedPathKey = "PATH";

    private DetailsTabType selectedDetailsTab;
    private static final String selectedDetailsTabKey = "DETAILS_TAB";

    private String analysisId; //Something like "combined_analysis_set_btpool0_0_15_03_2013_16_21_39"
    private static final String analysisIdKey = "ANALYSIS_ID";

    public State() {
        initialize();
    }

    public State(String token){
        initialize();
        //if(token==null || token.isEmpty()) return;

        // IMPORTANT: do NOT move this to initialize(), only makes sense when the state is created with a token
        setLoadedSpecies(DEFAULT_SPECIES_ID);

        if(!token.isEmpty()){
            try{
                @SuppressWarnings("NonJREEmulationClassesInClientCode")
                String[] tokens = token.split(DELIMITER);
                for (String t : tokens) {
                    @SuppressWarnings("NonJREEmulationClassesInClientCode")
                    String[] ts = t.split("=");
                    String key = ts[0];
                    String value = ts[1];
                    if(key.equals(loadedSpeciesKey)){
                        setLoadedSpecies(Long.valueOf(value));
                    }else if(key.equals(loadedDiagramKey)){
                        setLoadedDiagram(Long.valueOf(value));
                    }else if(key.equals(selectedInstanceKey)){
                        setSelectedInstance(Long.valueOf(value));
                    }else if(key.equals(loadedPathKey)){
                        for (String v : value.split(",")) {
                            loadedPath.add(Long.valueOf(v));
                        }
                    }else if(key.equals(selectedDetailsTabKey)){
                        setSelectedDetailsTab(DetailsTabType.getByCode(value));
                    }else if(key.equals(analysisIdKey)){
                        setAnalysisId(value);
                    }
                }
            }catch (Exception e){
                stateCorrect = false;
            }
        }
    }

    private void initialize(){
        stateCorrect = true;
        loadedSpecies = null;
        loadedDiagram = null;
        selectedInstance = null;
        selectedDetailsTab = null;
    }

    public boolean isCorrect(){
        boolean correct = true;
        if(selectedInstance!=null)
            correct = loadedDiagram!=null;
        return stateCorrect && correct;
    }

    public void resetInstancesState(){
        loadedDiagram = null;
        selectedInstance = null;
    }

    @SuppressWarnings("UnusedDeclaration")
    public boolean isInstancesInitialState(){
        return loadedDiagram==null && selectedInstance==null; // && selectedDetailsTab==null;
    }

    public boolean hasReachedAnalysisState(State state){
        if (analysisId != null ? !analysisId.equals(state.analysisId) : state.analysisId != null) return false;
        return true;
    }

    public boolean hasReachedSpeciesState(State state){
        return loadedSpecies != null && loadedSpecies.equals(state.loadedSpecies);
    }

    public boolean hasReachedInstancesState(State state){
        if (!Arrays.equals(loadedPath.toArray(), state.loadedPath.toArray()))
          return false;
        if (loadedDiagram != null ? !loadedDiagram.equals(state.loadedDiagram) : state.loadedDiagram != null)
            return false;
        if (selectedInstance != null ? !selectedInstance.equals(state.selectedInstance) : state.selectedInstance != null)
            return false;
        return true;
    }

    public boolean hasReachedTabState(State state){
        return getSelectedDetailsTab().equals(state.getSelectedDetailsTab());
    }

    public Long getLoadedSpecies() {
        return loadedSpecies;
    }

    public String getAnalysisId() {
        return analysisId;
    }

    public Long getLoadedSpeciesOrDefault(){
        return loadedSpecies==null?DEFAULT_SPECIES_ID:loadedSpecies;
    }

    public void setAnalysisId(String analysisId) {
        this.analysisId = analysisId;
    }

    public void setLoadedSpecies(Long loadedSpecies) {
        this.loadedSpecies = loadedSpecies;
    }

    public Long getLoadedDiagram() {
        return loadedDiagram;
    }

    public void setLoadedDiagram(Long loadedDiagram) {
        this.loadedDiagram = loadedDiagram;
    }

    public Long getSelectedInstance() {
        return selectedInstance;
    }

    public List<Long> getLoadedPath() {
        //it also avoids modifying the content here when changed somewhere else
        //because is a new instance of the list what is returned ;)
        return this.getPrunedPath();
    }

    /**
     * Returns the path until the loaded diagram (without including it)
     *
     * @return the path until the loaded diagram (without including it)
     */
    private List<Long> getPrunedPath(){
        List<Long> prunedPath = new LinkedList<Long>();
        for (Long dbId : this.loadedPath) {
            if(dbId.equals(this.loadedDiagram)){
                break;
            }
            prunedPath.add(dbId);
        }
        return prunedPath;
    }

    public void setLoadedPath(List<Long> loadedPath) {
        //The path can not be pruned here because the order of setting the state variables can not be predefined
        //so a different approach like pruning it when needs to be used has to be taken
        this.loadedPath = loadedPath;
    }

    public void setSelectedInstance(Long selectedInstance) {
        if(this.loadedDiagram!=null && this.loadedDiagram.equals(selectedInstance)){
            this.selectedInstance = null;
        }else{
            this.selectedInstance = selectedInstance;
        }
    }

    public DetailsTabType getSelectedDetailsTab() {
        if(selectedDetailsTab==null)
            return DetailsTabType.getDefault();
        else
            return selectedDetailsTab;
    }

    public void setSelectedDetailsTab(DetailsTabType tab) {
        if(tab.equals(DetailsTabType.getDefault()))
            this.selectedDetailsTab = null;
        else
            this.selectedDetailsTab = tab;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State state = (State) o;

        if (analysisId != null ? !analysisId.equals(state.analysisId) : state.analysisId != null) return false;
        if (loadedDiagram != null ? !loadedDiagram.equals(state.loadedDiagram) : state.loadedDiagram != null)
            return false;
        if (loadedPath != null ? !loadedPath.equals(state.loadedPath) : state.loadedPath != null) return false;
        if (loadedSpecies != null ? !loadedSpecies.equals(state.loadedSpecies) : state.loadedSpecies != null)
            return false;
        if (selectedDetailsTab != state.selectedDetailsTab) return false;
        if (selectedInstance != null ? !selectedInstance.equals(state.selectedInstance) : state.selectedInstance != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = loadedSpecies != null ? loadedSpecies.hashCode() : 0;
        result = 31 * result + (loadedDiagram != null ? loadedDiagram.hashCode() : 0);
        result = 31 * result + (selectedInstance != null ? selectedInstance.hashCode() : 0);
        result = 31 * result + (loadedPath != null ? loadedPath.hashCode() : 0);
        result = 31 * result + (selectedDetailsTab != null ? selectedDetailsTab.hashCode() : 0);
        result = 31 * result + (analysisId != null ? analysisId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder token = new StringBuilder();
        boolean addDelimiter = false;
        if(loadedSpecies!=null && !loadedSpecies.equals(DEFAULT_SPECIES_ID)){
            token.append(loadedSpeciesKey);
            token.append("=");
            token.append(loadedSpecies);
            addDelimiter=true;
        }if(loadedDiagram!=null){
            if(addDelimiter) token.append(DELIMITER);
            token.append(loadedDiagramKey);
            token.append("=");
            token.append(loadedDiagram);
            addDelimiter=true;
        }
        if(selectedInstance!=null){
            if(addDelimiter) token.append(DELIMITER);
            token.append(selectedInstanceKey);
            token.append("=");
            token.append(selectedInstance);
            addDelimiter=true;
        }
        List<Long> prunePath = this.getPrunedPath();
        if(!prunePath.isEmpty()){
            if(addDelimiter) token.append(DELIMITER);
            token.append(loadedPathKey);
            token.append("=");
            for (Long value : prunePath) {
                token.append(value);
                token.append(",");
            }
            token.deleteCharAt(token.length()-1);
            addDelimiter=true;
        }
        if(selectedDetailsTab!=null){
            if(addDelimiter) token.append(DELIMITER);
            token.append(selectedDetailsTabKey);
            token.append("=");
            token.append(selectedDetailsTab.getCode());
            addDelimiter=true;
        }

        if(analysisId!=null && !analysisId.isEmpty()){
            if(addDelimiter) token.append(DELIMITER);
            token.append(analysisIdKey);
            token.append("=");
            token.append(analysisId);
            //addDelimiter=true;
        }
        return token.toString();
    }
}
