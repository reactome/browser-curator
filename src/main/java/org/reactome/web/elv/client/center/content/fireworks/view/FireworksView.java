package org.reactome.web.elv.client.center.content.fireworks.view;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.elv.client.common.data.model.Pathway;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface FireworksView {

    public interface Presenter {
        void selectPathway(Long dbId);
        void resetPathwaySelection();
        void highlightPathway(Long dbId);
        void resetAnalysis();
        void resetPathwayHighlighting();
        void showPathwayDiagram(Long dbId);
    }

    Widget asWidget();
    void setPresenter(Presenter presenter);

    void loadSpeciesFireworks(String speciesJson);

    void highlightPathway(Pathway pathway);
    void resetHighlight();

    void setAnalysisToken(String token);
    void setAnalysisResource(String resource);
    void resetAnalysisToken();

    void selectPathway(Pathway pathway);
    void resetSelection();

}
