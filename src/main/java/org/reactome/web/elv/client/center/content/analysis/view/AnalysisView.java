package org.reactome.web.elv.client.center.content.analysis.view;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.elv.client.center.content.analysis.event.AnalysisCompletedEvent;
import org.reactome.web.elv.client.center.content.analysis.event.AnalysisErrorEvent;
import org.reactome.web.elv.client.common.data.model.Species;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface AnalysisView {

    public interface Presenter {
        void analysisCompleted(AnalysisCompletedEvent event);
        void analysisError(AnalysisErrorEvent event);
    }

    Widget asWidget();
    void setPresenter(Presenter presenter);
    void setSpeciesList(List<Species> speciesList);
}
