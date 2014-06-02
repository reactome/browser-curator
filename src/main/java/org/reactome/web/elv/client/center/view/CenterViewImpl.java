package org.reactome.web.elv.client.center.view;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.elv.client.center.content.analysis.view.AnalysisView;
import org.reactome.web.elv.client.center.content.welcome.WelcomeMessage;
import org.reactome.web.elv.client.common.widgets.glass.GlassPanel;
import org.reactome.web.elv.client.center.content.diagram.view.DiagramView;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class CenterViewImpl implements CenterView {

    private TabLayoutPanel container;
    private Presenter presenter;

    private DiagramView diagramView;
    private AnalysisView analysisView;
    private WelcomeMessage welcomeMessage;

    /**
     * The glass element.
     */
    private GlassPanel glass = null;

    public CenterViewImpl(DiagramView diagramView, AnalysisView analysisView) {
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

        this.welcomeMessage = new WelcomeMessage();
        this.container.add(this.welcomeMessage, "Welcome");

        this.diagramView = diagramView;
        this.container.add(diagramView.asWidget());

        this.analysisView = analysisView;
        this.container.add(analysisView.asWidget());
    }

    @Override
    public Widget asWidget() {
        return this.container;
    }

    @Override
    public void selectContent(Integer index) {
        this.container.selectTab(index);
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
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
