package org.reactome.web.elv.client.center.view;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface CenterView {

    public interface Presenter {

    }

    Widget asWidget();
    void selectContent(Integer index);
    void setPresenter(Presenter presenter);
    void tourFadeIn();
    void tourFadeOut();
}
