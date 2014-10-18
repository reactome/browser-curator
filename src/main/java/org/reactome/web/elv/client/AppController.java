package org.reactome.web.elv.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import org.reactome.web.elv.client.center.content.analysis.presenter.AnalysisPresenter;
import org.reactome.web.elv.client.center.content.analysis.view.AnalysisView;
import org.reactome.web.elv.client.center.content.analysis.view.AnalysisViewImpl;
import org.reactome.web.elv.client.center.content.diagram.presenter.DiagramPresenter;
import org.reactome.web.elv.client.center.content.diagram.view.DiagramView;
import org.reactome.web.elv.client.center.content.diagram.view.DiagramViewImpl;
import org.reactome.web.elv.client.center.content.fireworks.presenter.FireworksPresenter;
import org.reactome.web.elv.client.center.content.fireworks.view.FireworksView;
import org.reactome.web.elv.client.center.content.fireworks.view.FireworksViewImpl;
import org.reactome.web.elv.client.center.presenter.CenterPresenter;
import org.reactome.web.elv.client.center.view.CenterView;
import org.reactome.web.elv.client.center.view.CenterViewImpl;
import org.reactome.web.elv.client.common.Controller;
import org.reactome.web.elv.client.common.EventBus;
import org.reactome.web.elv.client.common.events.ELVEventType;
import org.reactome.web.elv.client.details.presenter.DetailsPresenter;
import org.reactome.web.elv.client.details.view.DetailsView;
import org.reactome.web.elv.client.details.view.DetailsViewImpl;
import org.reactome.web.elv.client.hierarchy.presenter.HierarchyPresenter;
import org.reactome.web.elv.client.hierarchy.view.HierarchyView;
import org.reactome.web.elv.client.hierarchy.view.HierarchyViewImpl;
import org.reactome.web.elv.client.main.presenter.MainPresenter;
import org.reactome.web.elv.client.main.view.MainViewImpl;
import org.reactome.web.elv.client.manager.data.DataManager;
import org.reactome.web.elv.client.manager.ga.GAManager;
import org.reactome.web.elv.client.manager.messages.MessagesManager;
import org.reactome.web.elv.client.manager.orthology.OrthologyManager;
import org.reactome.web.elv.client.manager.state.StateManager;
import org.reactome.web.elv.client.manager.tour.TourManager;
import org.reactome.web.elv.client.popups.PopupManager;
import org.reactome.web.elv.client.topbar.presenter.TopBarPresenter;
import org.reactome.web.elv.client.topbar.view.TopBarView;
import org.reactome.web.elv.client.topbar.view.TopBarViewImpl;

/**
 * Main controller without associated view which creates all the necessary artifacts
 *
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class AppController extends Controller {

    private MainViewImpl mainView = null;

    public AppController(EventBus eventBus) {
        super(eventBus);
    }

    /**
     * Initializes the managers, views and presenters and runs the web application
     * @param container the widget to put the web application view in
     */
    public void go(final HasWidgets container) {
        GWT.runAsync(new RunAsyncCallback() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error: " + caught.getMessage());
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onSuccess() {
                // lazily initialize our views, and keep them around to be reused
                if (mainView == null) {

                    loadManagerModules();   //Managers have to be load before the presenters

                    HierarchyView treeView = new HierarchyViewImpl();
                    new HierarchyPresenter(eventBus, treeView);

                    DetailsView detailsView = new DetailsViewImpl();
                    new DetailsPresenter(eventBus, detailsView);

                    //############ CENTER CONTENT ################
                    DiagramView diagramView = new DiagramViewImpl();
                    new DiagramPresenter(eventBus, diagramView);

                    AnalysisView analysisView = new AnalysisViewImpl();
                    new AnalysisPresenter(eventBus, analysisView);

                    FireworksView fireworksView = new FireworksViewImpl();
                    new FireworksPresenter(eventBus, fireworksView);

                    CenterView centerView = new CenterViewImpl(diagramView, fireworksView, analysisView);
                    new CenterPresenter(eventBus, centerView);
                    //############ /CENTER CONTENT ###############

                    TopBarView topBarView = new TopBarViewImpl();
                    new TopBarPresenter(eventBus, topBarView);

                    mainView = new MainViewImpl(treeView, detailsView, centerView, topBarView);

                    eventBus.fireELVEvent(ELVEventType.ELV_READY);
                }
                new MainPresenter(eventBus, mainView).go(container);
            }

        });
    }

    /**
     * Loads the managers to run the web application
     */
    private void loadManagerModules() {
        new GAManager(eventBus);
        new DataManager(eventBus);
        new OrthologyManager(eventBus);
        new StateManager(eventBus);
        new PopupManager(eventBus);
        new MessagesManager(eventBus);
        new TourManager(eventBus);
    }
}
