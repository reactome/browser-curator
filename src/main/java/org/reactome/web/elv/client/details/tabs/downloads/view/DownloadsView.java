package org.reactome.web.elv.client.details.tabs.downloads.view;

import org.reactome.web.elv.client.details.tabs.DetailsTabView;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface DownloadsView extends DetailsTabView<DownloadsView.Presenter> {

    public interface Presenter extends DetailsTabView.Presenter {

    }

    public void setDbName(String dbName);
}
