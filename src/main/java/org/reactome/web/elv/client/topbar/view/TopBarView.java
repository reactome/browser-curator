package org.reactome.web.elv.client.topbar.view;

import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.elv.client.common.data.model.Species;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface TopBarView {
	
	public interface Presenter {
        void analysisToolSelected();
        void detailsButtonToggled(ToggleButton btn);
        void diagramKeyButtonToggled(ToggleButton btn);
        void hierarchyButtonToggled(ToggleButton btn);
        void speciesSelected(Species species);
	}

    Widget asWidget();
    void allowBannerRedirection(boolean allow);
    void selectSpecies(Species species);
    void setButtonPressed(String btn, boolean pressed);
    void setPresenter(Presenter presenter);
    void setSpeciesList(List<Species> speciesList);
    void tourFadeIn();
    void tourFadeOut();
}