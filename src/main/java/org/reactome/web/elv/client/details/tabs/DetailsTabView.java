package org.reactome.web.elv.client.details.tabs;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.details.model.DetailsTabType;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface DetailsTabView<P> {

    public interface Presenter {
        DetailsTabView getView();
        void setInstancesInitialState();
        void showInstanceDetails(Pathway pathway, DatabaseObject databaseObject);
        void showInstanceDetailsIfExists(Pathway pathway, DatabaseObject databaseObject);
    }

    Widget asWidget();
    DetailsTabType getDetailTabType();
    HTMLPanel getTitle();
    void setInitialState();
    void setPresenter(P presenter);
    void showInstanceDetails(Pathway pathway, DatabaseObject databaseObject);
    boolean showInstanceDetailsIfExists(Pathway pathway, DatabaseObject databaseObject);
}