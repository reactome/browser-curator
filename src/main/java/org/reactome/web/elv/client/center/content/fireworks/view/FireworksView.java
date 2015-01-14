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
        void resetPathwayHighlighting();
    }

    Widget asWidget();
    void setPresenter(Presenter presenter);

    void loadSpeciesFireworks(String speciesJson);

    void highlightPathway(Pathway pathway);
    void resetHighlight();

    void selectPathway(Pathway pathway);
    void resetSelection();
}
