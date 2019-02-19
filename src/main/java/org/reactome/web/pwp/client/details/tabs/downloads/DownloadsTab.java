package org.reactome.web.pwp.client.details.tabs.downloads;

import org.reactome.web.pwp.client.common.model.classes.DatabaseObject;
import org.reactome.web.pwp.client.common.model.classes.Pathway;
import org.reactome.web.pwp.client.details.tabs.DetailsTab;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface DownloadsTab {

    interface Presenter extends DetailsTab.Presenter {
        void swapToMolecules(Pathway pathway);
    }

    interface Display extends DetailsTab.Display<Presenter> {
        void setDbName(String dbName);

        void showDetails(DatabaseObject databaseObject);
    }
}
