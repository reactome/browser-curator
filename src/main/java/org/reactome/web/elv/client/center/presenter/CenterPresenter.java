package org.reactome.web.elv.client.center.presenter;

import org.reactome.web.elv.client.center.model.CenterToolType;
import org.reactome.web.elv.client.center.view.CenterView;
import org.reactome.web.elv.client.common.Controller;
import org.reactome.web.elv.client.common.EventBus;
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
    public void onStateManagerToolsInitialStateReached() {
        showTool(null);
    }

    @Override
    public void onStateManagerToolSelected(CenterToolType tool) {
        showTool(tool);
    }

    @Override
    public void onTopBarAnalysisSelected(){
        this.view.selectContent(2);
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


    private void showTool(CenterToolType tool){
        if(tool==null){
            this.view.selectContent(0);
            return;
        }
        switch (tool){
            case DIAGRAM:
                this.view.selectContent(1);
                break;
            case ANALYSIS:
                this.view.selectContent(2);
                break;
        }
    }
}
