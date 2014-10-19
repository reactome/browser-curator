package org.reactome.web.elv.client.center.content.fireworks.view;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.elv.client.common.data.model.Pathway;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface FireworksView {

    public interface Presenter {

    }

    Widget asWidget();
    void setPresenter(Presenter presenter);
    void highlightPathway(Pathway pathway);
    void resetHighlight();
}
