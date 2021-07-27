package org.reactome.web.pwp.client.details.tabs.molecules;

import org.reactome.web.pwp.client.common.model.classes.DatabaseObject;
import org.reactome.web.pwp.client.common.model.classes.Event;
import org.reactome.web.pwp.client.common.model.classes.Pathway;
import org.reactome.web.pwp.client.details.tabs.DetailsTab;
import org.reactome.web.pwp.client.details.tabs.molecules.model.data.Result;

/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
public interface MoleculesTab {

    interface Presenter extends DetailsTab.Presenter {
        void getMoleculesData();
        void updateMoleculesData();
        void moleculeDownloadStarted();
    }

    interface Display extends DetailsTab.Display<Presenter> {
        void showDetails(Event event, DatabaseObject databaseObject);
        void updateDetailsIfLoaded(Event event, DatabaseObject databaseObject);

        void setMoleculesData(Result result);
        void updateMoleculesData(Result result);

        void refreshTitle(Integer highlightedMolecules, Integer loadedMolecules);
        void moleculesDownloadRequired();

        void setLoadingMsg(String msg);
        void clearLoadingMsg();
    }
}
