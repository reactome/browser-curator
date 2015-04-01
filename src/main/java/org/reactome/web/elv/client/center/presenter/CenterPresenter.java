package org.reactome.web.elv.client.center.presenter;

import org.reactome.web.elv.client.center.model.CenterToolType;
import org.reactome.web.elv.client.center.view.CenterView;
import org.reactome.web.elv.client.common.Controller;
import org.reactome.web.elv.client.common.EventBus;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.common.data.model.Species;
import org.reactome.web.elv.client.manager.tour.TourStage;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class CenterPresenter extends Controller implements CenterView.Presenter {

    //In some computer Fireworks takes a while to load. That is the reason of the
    //following mechanism to ensure Fireworks is loaded
    private Long currentSpeciesId;
    private Long fireworksSpecies;
    private boolean ANALYSIS_TOOL_PENDENT_TO_OPEN = false;

    public enum Display { FIREWORKS, DIAGRAM }

    private CenterView view;
    public static Display CURRENT_DISPLAY = Display.FIREWORKS;

    public CenterPresenter(EventBus eventBus, CenterView view) {
        super(eventBus);
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void onDiagramFireworksRequired(Pathway pathway) {
        CURRENT_DISPLAY = Display.FIREWORKS;
        this.view.setMainToolToFireworks();
    }

    @Override
    public void onFireworksLoaded(Long speciesId) {
        fireworksSpecies = speciesId;
        if(currentSpeciesId.equals(fireworksSpecies) && ANALYSIS_TOOL_PENDENT_TO_OPEN){
            ANALYSIS_TOOL_PENDENT_TO_OPEN = false;
            this.view.showAnalysisTool();
        }
    }

    @Override
    public void onFireworksPathwayOpened(Pathway pathway) {
        CURRENT_DISPLAY = Display.DIAGRAM;
        this.view.setMainToolToDiagram();
    }

    @Override
    public void onStateManagerToolsInitialStateReached() {
        CURRENT_DISPLAY = Display.FIREWORKS;
        this.view.setMainToolToFireworks();
    }

    @Override
    public void onStateManagerToolSelected(CenterToolType tool) {
        if(tool.equals(CenterToolType.ANALYSIS)){
            if(currentSpeciesId.equals(fireworksSpecies)) {
                this.view.showAnalysisTool();
            }else{
                ANALYSIS_TOOL_PENDENT_TO_OPEN = true;
            }
        }else{
            switch (CURRENT_DISPLAY){
                case FIREWORKS:
                    this.view.setMainToolToFireworks();
                    break;
                case DIAGRAM:
                    this.view.setMainToolToDiagram();
                    break;
            }
        }
    }

    @Override
    public void onStateManagerSpeciesSelected(Species species) {
        this.currentSpeciesId = species.getDbId();
    }

    @Override
    public void onTopBarAnalysisSelected(){
        this.view.showAnalysisTool();
    }

    @Override
    public void onTourManagerTourCancelled() {
        this.view.tourFadeOut();
    }

    @Override
    public void onTourManagerTourFinished() {
        this.view.tourFadeOut();
    }

    @Override
    public void onTourManagerTourProgress(TourStage stage, Integer step) {
        if(stage==TourStage.SHOW_MODULES){
            if(step==3){
                this.view.tourFadeOut();
            }else{
                this.view.tourFadeIn();
            }
        }else  if(stage==TourStage.TEST_DIAGRAM){
            this.view.tourFadeOut();
        }else{
            this.view.tourFadeIn();
        }
    }

    @Override
    public void onTourManagerTourStarted() {
        this.view.tourFadeIn();
    }

}
