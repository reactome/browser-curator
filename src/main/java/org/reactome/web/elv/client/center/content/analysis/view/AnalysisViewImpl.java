package org.reactome.web.elv.client.center.content.analysis.view;

import com.google.gwt.user.client.ui.*;
import org.reactome.web.elv.client.center.content.analysis.event.AnalysisCompletedEvent;
import org.reactome.web.elv.client.center.content.analysis.event.AnalysisErrorEvent;
import org.reactome.web.elv.client.center.content.analysis.handler.AnalysisCompletedEventHandler;
import org.reactome.web.elv.client.center.content.analysis.handler.AnalysisErrorEventHandler;
import org.reactome.web.elv.client.center.content.analysis.model.FileSubmitter;
import org.reactome.web.elv.client.center.content.analysis.model.PostSubmitter;
import org.reactome.web.elv.client.center.content.analysis.model.SpeciesSubmitter;
import org.reactome.web.elv.client.center.content.analysis.style.AnalysisStyleFactory;
import org.reactome.web.elv.client.common.data.model.Species;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class AnalysisViewImpl implements AnalysisView, AnalysisCompletedEventHandler, AnalysisErrorEventHandler {
    Presenter presenter;

    private ScrollPanel container;

    private SpeciesSubmitter speciesSubmitter;

    public AnalysisViewImpl() {
        FlowPanel vp = new FlowPanel();
        vp.addStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisContainer());

        PostSubmitter postSubmitter = new PostSubmitter();
        postSubmitter.addAnalysisCompletedEventHandler(this);
        postSubmitter.addAnalysisErrorEventHandler(this);

        FileSubmitter fileSubmitter = new FileSubmitter(postSubmitter);
        fileSubmitter.addAnalysisCompletedEventHandler(this);
        fileSubmitter.addAnalysisErrorEventHandler(this);
        vp.add(fileSubmitter);

        this.speciesSubmitter = new SpeciesSubmitter();
        this.speciesSubmitter.addAnalysisCompletedEventHandler(this);
        this.speciesSubmitter.addAnalysisErrorEventHandler(this);
        vp.add(speciesSubmitter);

        this.container = new ScrollPanel();
        this.container.getElement().setAttribute("min-width", "600px");
        this.container.add(vp);
    }

    @Override
    public Widget asWidget() {
        return this.container;
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setSpeciesList(List<Species> speciesList) {
        this.speciesSubmitter.setSpeciesList(speciesList);
    }

    @Override
    public void onAnalysisCompleted(AnalysisCompletedEvent event) {
        presenter.analysisCompleted(event);
    }

    @Override
    public void onAnalysisError(AnalysisErrorEvent event) {
        presenter.analysisError(event);
    }
}
