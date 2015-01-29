package org.reactome.web.elv.client.center.view;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.elv.client.center.content.analysis.view.AnalysisView;
import org.reactome.web.elv.client.center.content.diagram.view.DiagramView;
import org.reactome.web.elv.client.center.content.fireworks.view.FireworksView;
import org.reactome.web.elv.client.common.widgets.glass.GlassPanel;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class CenterViewImpl implements CenterView {

    private TabLayoutPanel container;

    /**
     * The glass element.
     */
    private GlassPanel glass = null;

    public CenterViewImpl(DiagramView diagramView, FireworksView fireworksView, AnalysisView analysisView) {
        this.container = new TabLayoutPanel(0, Style.Unit.PX){
            @Override
            public void onResize() {
                super.onResize();
                if(glass!=null){
                    glass.onResize();
                }
            }
        };
        this.container.setAnimationDuration(500);
        this.container.addStyleName("elv-Diagram-Container");

        this.container.add(fireworksView.asWidget());
        this.container.add(diagramView.asWidget());
        this.container.add(analysisView.asWidget());
    }

    @Override
    public Widget asWidget() {
        return this.container;
    }

    @Override
    public void setPresenter(Presenter presenter) {
        //Nothing here
    }

    @Override
    public void showAnalysisTool() {
        this.container.setAnimationDuration(500);
        this.container.selectTab(2);
    }

    @Override
    public void setMainToolToDiagram() {
        this.container.getWidget(1).setVisible(false);
        this.container.selectTab(1);

        this.container.getWidget(0).setVisible(false);

        this.container.setAnimationDuration(0);
    }

    @Override
    public void setMainToolToFireworks() {
        this.container.getWidget(0).setVisible(true);
        this.container.selectTab(0);

        this.container.getWidget(1).setVisible(false);

        this.container.setAnimationDuration(0);
        this.container.getWidget(0);
    }

    @Override
    public void tourFadeIn() {
        if(glass==null){
            glass = new GlassPanel(this.container);
        }
        RootPanel.get().add(glass);
    }

    @Override
    public void tourFadeOut() {
        if(glass!=null){
            RootPanel.get().remove(glass);
        }
    }
}
