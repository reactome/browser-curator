package org.reactome.web.elv.client.details.tabs.analysis.presenter.providers;

import com.google.gwt.http.client.*;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import org.reactome.web.elv.client.common.analysis.factory.AnalysisModelException;
import org.reactome.web.elv.client.common.analysis.factory.AnalysisModelFactory;
import org.reactome.web.elv.client.common.analysis.helper.AnalysisHelper;
import org.reactome.web.elv.client.common.analysis.model.PathwayIdentifier;
import org.reactome.web.elv.client.common.analysis.model.PathwayIdentifiers;
import org.reactome.web.elv.client.details.tabs.analysis.view.widgets.common.CustomPager;
import org.reactome.web.elv.client.details.tabs.analysis.view.widgets.found.FoundTable;
import org.reactome.web.elv.client.details.tabs.analysis.view.widgets.notfound.NotFoundTable;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class FoundAsyncDataProvider extends AsyncDataProvider<PathwayIdentifier> {
    private FoundTable table;
    private SimplePager pager;

    private String token;
    private String resource;
    private Long pathwayId;

    public FoundAsyncDataProvider(FoundTable table, CustomPager pager, String token, Long pathwayId, String resource) {
        this.table = table;
        this.pager = pager;
        this.token = token;
        this.resource = resource;
        this.pathwayId = pathwayId;
        this.addDataDisplay(this.table);
    }

    @Override
    protected void onRangeChanged(HasData<PathwayIdentifier> display) {
        final Integer page = this.pager.getPage() + 1;

        String url = AnalysisHelper.URL_PREFIX  + "/token/" + this.token + "/summary/" + this.pathwayId + "?resource=" + this.resource + "&pageSize=" + NotFoundTable.PAGE_SIZE + "&page=" + page;
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    try {
                        PathwayIdentifiers identifiers = AnalysisModelFactory.getModelObject(PathwayIdentifiers.class, response.getText());
                        table.setRowCount(identifiers.getFound());
                        table.setRowData(pager.getPageStart(), identifiers.getIdentifiers());
                    } catch (AnalysisModelException e) {
                        System.err.println(e.getMessage());
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    System.err.println(exception.getMessage());
                }
            });
        }catch (RequestException ex) {
            System.err.println(ex.getMessage());
        }
    }


}
