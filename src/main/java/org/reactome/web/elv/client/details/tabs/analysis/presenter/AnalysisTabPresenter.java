package org.reactome.web.elv.client.details.tabs.analysis.presenter;

import com.google.gwt.http.client.*;
import org.reactome.web.elv.client.common.Controller;
import org.reactome.web.elv.client.common.EventBus;
import org.reactome.web.elv.client.common.analysis.factory.AnalysisModelException;
import org.reactome.web.elv.client.common.analysis.factory.AnalysisModelFactory;
import org.reactome.web.elv.client.common.analysis.helper.AnalysisHelper;
import org.reactome.web.elv.client.common.analysis.model.AnalysisResult;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Event;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.common.data.model.Species;
import org.reactome.web.elv.client.common.events.ELVEventType;
import org.reactome.web.elv.client.common.utils.Console;
import org.reactome.web.elv.client.details.tabs.DetailsTabView;
import org.reactome.web.elv.client.details.tabs.analysis.events.AnalysisTabPathwaySelected;
import org.reactome.web.elv.client.details.tabs.analysis.view.AnalysisTabView;
import org.reactome.web.elv.client.manager.state.AdvancedState;
import org.reactome.web.elv.client.manager.state.StableIdentifierLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class AnalysisTabPresenter extends Controller implements AnalysisTabView.Presenter, AnalysisHelper.ResourceChosenHandler {

    private Map<Long, String> speciesMap = new HashMap<Long, String>();

    private AnalysisTabView view;
    private String token;
    private Pathway selected;

    public AnalysisTabPresenter(EventBus eventBus, AnalysisTabView view) {
        super(eventBus);
        this.view = view;
        this.view.setPresenter(this);
        this.selected = null;
    }

    @Override
    public DetailsTabView getView() {
        return this.view;
    }

    @Override
    public void onDataManagerSpeciesListRetrieved(List<Species> speciesList) {
        for (Species species : speciesList) {
            this.speciesMap.put(species.getDbId(), species.getDisplayName());
        }
    }

    @Override
    public void onStateManagerAnalysisTokenSelected(String token) {
        this.token = token;
        this.view.showWaitingMessage();
        AnalysisHelper.chooseResource(token, this);
    }


    @Override
    public void onStateManagerAnalysisTokenReset() {
        this.token = null;
//        this.selected = null;  //DO NOT SET this.selected TO NULL
        this.view.setInitialState();
    }

    @Override
    public void setInstancesInitialState() {
        this.selected = null;
        this.view.clearSelection();
    }

    @Override
    public void showInstanceDetails(Pathway pathway, DatabaseObject databaseObject) {
        view.showInstanceDetails(pathway, databaseObject);
    }

    @Override
    public void showInstanceDetailsIfExists(Pathway pathway, DatabaseObject databaseObject) {
        view.showInstanceDetailsIfExists(pathway, databaseObject);
    }

    @Override
    public void onStateManagerDatabaseObjectsSelected(List<Event> path, Pathway pathway, DatabaseObject databaseObject) {
        Pathway toSelect = databaseObject instanceof Pathway ? (Pathway) databaseObject : pathway;
        if (!toSelect.equals(selected)) {
            selected = toSelect;
            this.view.selectPathway(toSelect);
        }
    }

    @Override
    public void onResourceSelected(String resource) {
        this.onResourceChosen(resource);
        eventBus.fireELVEvent(ELVEventType.ANALYSIS_TAB_RESOURCE_SELECTED, resource);
    }

    @Override
    public void onPathwaySelected(Long dbId) {
        if(selected!=null && dbId.equals(selected.getDbId())) return;

        new StableIdentifierLoader(dbId.toString(), new StableIdentifierLoader.StableIdentifierLoadedHandler() {
            @Override
            public void onStableIdentifierLoaded(AdvancedState advancedState) {
                AnalysisTabPathwaySelected sel = new AnalysisTabPathwaySelected(
                        advancedState.getSpecies(),
                        advancedState.getPathway(),
                        (Pathway) advancedState.getInstance()
                );
                selected = (sel.getPathway() != null)? sel.getPathway() : sel.getDiagram();
                eventBus.fireELVEvent(ELVEventType.ANALYSIS_TAB_PATHWAY_SELECTED, sel);
            }
        });
    }

    @Override
    public void onResourceChosen(final String resource){
        String url = AnalysisHelper.URL_PREFIX + "/token/" + token + "?page=1&resource=" + resource;
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    try {
                        AnalysisResult result = AnalysisModelFactory.getModelObject(AnalysisResult.class, response.getText());
                        //We have to do this here because there is no name coming from the RESTFul service
                        result.getSummary().setSpeciesName(speciesMap.get(result.getSummary().getSpecies()));
                        view.clearSelection();
                        view.showResult(result, resource);
                        view.selectPathway(selected);
                    } catch (AnalysisModelException e) {
                        Console.error(e.getMessage());
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    Console.error(exception.getMessage());
                }
            });
        }catch (RequestException ex) {
            Console.error(ex.getMessage());
        }
    }
}
