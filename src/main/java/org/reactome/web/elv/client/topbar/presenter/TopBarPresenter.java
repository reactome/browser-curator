package org.reactome.web.elv.client.topbar.presenter;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.ToggleButton;
import org.reactome.web.elv.client.common.Controller;
import org.reactome.web.elv.client.common.EventBus;
import org.reactome.web.elv.client.common.data.model.Species;
import org.reactome.web.elv.client.common.events.ELVEventType;
import org.reactome.web.elv.client.common.utils.Console;
import org.reactome.web.elv.client.manager.messages.MessageObject;
import org.reactome.web.elv.client.manager.messages.MessageType;
import org.reactome.web.elv.client.manager.tour.TourStage;
import org.reactome.web.elv.client.topbar.view.TopBarView;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class TopBarPresenter extends Controller implements TopBarView.Presenter {

    private TopBarView view;

	public TopBarPresenter(EventBus eventBus, TopBarView view){
        super(eventBus);
		this.view = view;
		this.view.setPresenter(this);
	}

    @Override
    public void analysisToolSelected() {
        this.eventBus.fireELVEvent(ELVEventType.TOPBAR_ANALYSIS_SELECTED);
    }

    @Override
    public void detailsButtonToggled(ToggleButton btn) {
        this.eventBus.fireELVEvent(ELVEventType.TOPBAR_SHOW_DETAILS_BUTTON_TOGGLED, btn);
    }

    @Override
    public void diagramKeyButtonToggled(ToggleButton btn) {
        this.eventBus.fireELVEvent(ELVEventType.TOPBAR_SHOW_DIAGRAM_KEY_BUTTON_TOGGLED, btn);
    }

    @Override
    public void hierarchyButtonToggled(ToggleButton btn) {
        this.eventBus.fireELVEvent(ELVEventType.TOPBAR_SHOW_HIERARCHY_BUTTON_TOGGLED, btn);
    }

    @Override
    public void onDataManagerSpeciesListRetrieved(List<Species> speciesList) {
        this.view.setSpeciesList(speciesList);
        this.eventBus.fireELVEvent(ELVEventType.TOPBAR_SPECIES_LOADED);
    }

    @Override
    public void onDetailPanelResized(Integer size) {
        this.view.setButtonPressed("details", size>0);
    }

    @Override
    public void onDiagramKeyClosed() {
        this.view.setButtonPressed("diagramKey", false);
    }

    @Override
    public void onELVReady() {
        this.view.setButtonPressed("diagramkey", true);
        Timer t = new Timer() {
            @Override
            public void run() {
                view.setButtonPressed("diagramkey",false);
            }
        };
        t.schedule(3000);
        this.eventBus.fireELVEvent(ELVEventType.TOPBAR_SPECIES_LIST_REQUIRED);
    }

    @Override
    public void onHierarchyPanelResized(Integer size) {
        this.view.setButtonPressed("hierarchy", size>0);
    }

    @Override
    public void onStateManagerSpeciesSelected(Species species) {
        try{
            view.selectSpecies(species);
        }catch (Exception ex){
            MessageObject msgObj = new MessageObject("The species '" + species.getDisplayName() +
                    "' could not be selected.\n" +
                    "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_ERROR);
            eventBus.fireELVEvent(ELVEventType.INTERANL_MESSAGE, msgObj);
            Console.error(getClass() + ex.getMessage());
        }
    }

    @Override
    public void onTourManagerTourCancelled() {
        this.view.tourFadeOut();
        this.view.allowBannerRedirection(true);
    }

    @Override
    public void onTourManagerTourFinished() {
        this.view.tourFadeOut();
        this.view.allowBannerRedirection(true);
    }

    @Override
    public void onTourManagerTourProgress(TourStage stage, Integer step) {
        this.view.tourFadeIn();
        if(stage==TourStage.SHOW_MODULES){
            this.view.setButtonPressed("hierarchy", true);
            this.view.setButtonPressed("details", true);
            if(step==1){
                this.view.tourFadeOut();
            }
        }else  if(stage==TourStage.TEST_TOP_BAR){
            this.view.tourFadeOut();
        }else if(stage==TourStage.TEST_HIERARCHY){
            if(step==0){
                this.view.setButtonPressed("hierarchy", true);
                this.view.setButtonPressed("details", true);
            }
        }
    }

    @Override
    public void onTourManagerTourStarted() {
        this.view.setButtonPressed("hierarchy", true);
        this.view.setButtonPressed("details", true);
        this.view.tourFadeIn();
        this.view.allowBannerRedirection(false);
    }

    @Override
    public void speciesSelected(Species species) {
        eventBus.fireELVEvent(ELVEventType.TOPBAR_SPECIES_SELECTED, species);
    }
}

