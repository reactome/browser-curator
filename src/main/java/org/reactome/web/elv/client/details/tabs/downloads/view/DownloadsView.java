package org.reactome.web.elv.client.details.tabs.downloads.view;

import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.details.tabs.DetailsTabView;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface DownloadsView extends DetailsTabView<DownloadsView.Presenter> {

    public interface Presenter extends DetailsTabView.Presenter {

        void swapToMolecules(Pathway pathway);
    }

    public void setDbName(String dbName);
}
