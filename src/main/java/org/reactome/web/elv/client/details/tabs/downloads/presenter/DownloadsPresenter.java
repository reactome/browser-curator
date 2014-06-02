package org.reactome.web.elv.client.details.tabs.downloads.presenter;

import com.google.gwt.http.client.*;
import org.reactome.web.elv.client.common.Controller;
import org.reactome.web.elv.client.common.EventBus;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.details.tabs.DetailsTabView;
import org.reactome.web.elv.client.details.tabs.downloads.view.DownloadsView;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DownloadsPresenter extends Controller implements DownloadsView.Presenter {

    private DownloadsView view;

    public DownloadsPresenter(EventBus eventBus, final DownloadsView view) {
        super(eventBus);
        this.view = view;
        this.view.setPresenter(this);

        //THIS IS A SPECIFIC USE CASE WHERE WE NEED TO KNOW THE DB WE ARE WORKING WITH
        String url = "/ReactomeRESTfulAPI/RESTfulWS/getDBName";
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    view.setDbName(response.getText().trim());
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    view.setDbName("NO_DB_RETRIEVED");
                }
            });
        }
        catch (RequestException ex) {
            view.setDbName("NO_DB_RETRIEVED");
        }
    }

    @Override
    public DetailsTabView getView() {
        return view;
    }

    @Override
    public void setInstancesInitialState() {
        view.setInitialState();
    }

    @Override
    public void showInstanceDetails(Pathway pathway, DatabaseObject databaseObject) {
        view.showInstanceDetails(pathway, databaseObject);
    }

    @Override
    public void showInstanceDetailsIfExists(Pathway pathway, DatabaseObject databaseObject) {
        view.showInstanceDetailsIfExists(pathway, databaseObject);
    }
}
