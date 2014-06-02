package org.reactome.web.elv.client.center.content.analysis.presenter;

import org.reactome.web.elv.client.center.content.analysis.event.AnalysisCompletedEvent;
import org.reactome.web.elv.client.center.content.analysis.event.AnalysisErrorEvent;
import org.reactome.web.elv.client.center.content.analysis.handler.AnalysisCompletedEventHandler;
import org.reactome.web.elv.client.center.content.analysis.handler.AnalysisErrorEventHandler;
import org.reactome.web.elv.client.center.content.analysis.view.AnalysisView;
import org.reactome.web.elv.client.common.Controller;
import org.reactome.web.elv.client.common.EventBus;
import org.reactome.web.elv.client.common.data.model.Species;
import org.reactome.web.elv.client.common.events.ELVEventType;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class AnalysisPresenter extends Controller implements AnalysisView.Presenter {

    AnalysisView view;

    public AnalysisPresenter(EventBus eventBus, AnalysisView view) {
        super(eventBus);
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void analysisCompleted(AnalysisCompletedEvent event) {
        eventBus.fireELVEvent(ELVEventType.ANALYSIS_COMPLETED, event);
    }

    @Override
    public void analysisError(AnalysisErrorEvent event) {
        eventBus.fireELVEvent(ELVEventType.ANALYSIS_ERROR, event);
    }

    @Override
    public void onDataManagerSpeciesListRetrieved(List<Species> speciesList) {
        this.view.setSpeciesList(speciesList);
        this.eventBus.fireELVEvent(ELVEventType.TOPBAR_SPECIES_LOADED);
    }
}
