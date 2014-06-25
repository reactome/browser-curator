package org.reactome.web.elv.client.details.tabs.molecules.view;

import org.reactome.web.elv.client.details.tabs.DetailsTabView;
import org.reactome.web.elv.client.details.tabs.molecules.model.data.Result;

/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
public interface MoleculesView extends DetailsTabView<MoleculesView.Presenter> {

    public interface Presenter extends DetailsTabView.Presenter {
        void getMoleculesData();
        void updateMoleculesData();
    }

    void setMoleculesData(Result result);
    void updateMoleculesData(Result result);
    void refreshTitle(Integer loadedMolecules);
}
