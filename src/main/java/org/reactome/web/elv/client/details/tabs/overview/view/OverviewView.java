package org.reactome.web.elv.client.details.tabs.overview.view;

import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.details.tabs.DetailsTabView;

public interface OverviewView extends DetailsTabView<OverviewView.Presenter> {

    public interface Presenter extends DetailsTabView.Presenter{
        void getOverviewData();
    }

    void setOverviewData(DatabaseObject databaseObject);
}