package org.reactome.web.elv.client.center.content.analysis.event;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.elv.client.center.content.analysis.handler.AnalysisCompletedEventHandler;
import org.reactome.web.elv.client.common.analysis.model.AnalysisResult;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class AnalysisCompletedEvent extends GwtEvent<AnalysisCompletedEventHandler> {
    public static Type<AnalysisCompletedEventHandler> TYPE = new GwtEvent.Type<AnalysisCompletedEventHandler>();

    AnalysisResult analysisResult;

    public AnalysisCompletedEvent(AnalysisResult analysisResult) {
        this.analysisResult = analysisResult;
    }

    @Override
    public Type<AnalysisCompletedEventHandler> getAssociatedType() {
        return TYPE;
    }

    public AnalysisResult getAnalysisResult() {
        return analysisResult;
    }

    @Override
    protected void dispatch(AnalysisCompletedEventHandler handler) {
        handler.onAnalysisCompleted(this);
    }
}
