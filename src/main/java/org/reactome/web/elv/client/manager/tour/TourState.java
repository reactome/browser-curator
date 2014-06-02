package org.reactome.web.elv.client.manager.tour;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class TourState {
    private TourStage stage;
    private int step;

    public TourState(TourStage stage, int step) {
        this.stage = stage;
        this.step = step;
    }

    public TourStage getStage() {
        return stage;
    }

    public void setStage(TourStage stage) {
        this.stage = stage;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int incStep(){
        return ++this.step;
    }

    public boolean isStage(TourStage stage){
        return this.stage.equals(stage);
    }
}
