package org.reactome.web.elv.client.center.presenter;

import org.reactome.web.elv.client.center.model.CenterToolType;
import org.reactome.web.elv.client.center.view.CenterView;
import org.reactome.web.elv.client.common.Controller;
import org.reactome.web.elv.client.common.EventBus;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.manager.tour.TourStage;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class CenterPresenter extends Controller implements CenterView.Presenter {
    private CenterView view;

    public CenterPresenter(EventBus eventBus, CenterView view) {
        super(eventBus);
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void onDiagramFireworksRequired(Pathway pathway) {
        this.view.setMainToolToFireworks();
    }

    @Override
    public void onFireworksPathwayOpened(Pathway pathway) {
        this.view.setMainToolToDiagram();
    }

    @Override
    public void onStateManagerToolsInitialStateReached() {
        this.view.setMainToolToFireworks();
    }

    @Override
    public void onStateManagerToolSelected(CenterToolType tool) {
        switch (tool){
            case ANALYSIS:
                this.view.showAnalysisTool();
                break;
            default:
                this.view.setMainToolToFireworks();
        }
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
