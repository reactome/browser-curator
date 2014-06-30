package org.reactome.web.elv.client.details.presenter;

import org.reactome.web.elv.client.common.EventBus;
import org.reactome.web.elv.client.details.model.DetailsTabType;
import org.reactome.web.elv.client.details.tabs.DetailsTabView;
import org.reactome.web.elv.client.details.tabs.analysis.presenter.AnalysisTabPresenter;
import org.reactome.web.elv.client.details.tabs.analysis.view.AnalysisTabViewImpl;
import org.reactome.web.elv.client.details.tabs.downloads.presenter.DownloadsPresenter;
import org.reactome.web.elv.client.details.tabs.downloads.view.DownloadsViewImpl;
import org.reactome.web.elv.client.details.tabs.expression.presenter.ExpressionPresenter;
import org.reactome.web.elv.client.details.tabs.expression.view.ExpressionViewImpl;
import org.reactome.web.elv.client.details.tabs.molecules.presenter.MoleculePresenter;
import org.reactome.web.elv.client.details.tabs.molecules.view.MoleculeViewImpl;
import org.reactome.web.elv.client.details.tabs.overview.presenter.OverviewPresenter;
import org.reactome.web.elv.client.details.tabs.overview.view.OverviewViewImpl;
import org.reactome.web.elv.client.details.tabs.processes.presenter.ProcessesPresenter;
import org.reactome.web.elv.client.details.tabs.processes.view.ProcessesViewImpl;
import org.reactome.web.elv.client.details.tabs.structures.presenter.StructuresPresenter;
import org.reactome.web.elv.client.details.tabs.structures.view.StructuresViewImpl;

import java.util.ArrayList;
import java.util.List;

abstract class TabsFactory {

    static List<DetailsTabView.Presenter> getDetailsTabs(EventBus eventBus){
        List<DetailsTabView.Presenter> list = new ArrayList<DetailsTabView.Presenter>();

        //The tabs will be added to the tab following the DetailsTabType order this is
        //just for keep the order in DetailsTabType (at the very beginning both seems
        //to be the same, but later on, if you decide to change the order, will only
        //have to change change it in "DetailsTabType", not here
        for (DetailsTabType type : DetailsTabType.values(true)) {
            switch (type){
                case OVERVIEW:
                    list.add(new OverviewPresenter(eventBus, new OverviewViewImpl()));
                    break;
                case PARTICIPATING_PROCESSES:
                    list.add(new ProcessesPresenter(eventBus, new ProcessesViewImpl()));
                    break;
                case PARTICIPATING_MOLECULES:
                    list.add(new MoleculePresenter(eventBus, new MoleculeViewImpl()));
                    break;
                case STRUCTURES:
                    list.add(new StructuresPresenter(eventBus, new StructuresViewImpl()));
                    break;
                case EXPRESSION:
                    list.add(new ExpressionPresenter(eventBus, new ExpressionViewImpl()));
                    break;
                case ANALYSIS:
                    list.add(new AnalysisTabPresenter(eventBus, new AnalysisTabViewImpl()));
                    break;
                case DOWNLOADS:
                    list.add(new DownloadsPresenter(eventBus, new DownloadsViewImpl()));
                    break;
            }
        }
        return list;
    }
}
