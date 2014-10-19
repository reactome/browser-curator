package org.reactome.web.elv.client.details.tabs.downloads.presenter;

import com.google.gwt.http.client.*;
import org.reactome.web.elv.client.common.Controller;
import org.reactome.web.elv.client.common.EventBus;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.common.events.ELVEventType;
import org.reactome.web.elv.client.common.utils.Console;
import org.reactome.web.elv.client.details.model.DetailsTabType;
import org.reactome.web.elv.client.details.tabs.DetailsTabView;
import org.reactome.web.elv.client.details.tabs.downloads.view.DownloadsView;
import org.reactome.web.elv.client.manager.messages.MessageObject;
import org.reactome.web.elv.client.manager.messages.MessageType;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DownloadsPresenter extends Controller implements DownloadsView.Presenter {

    private DownloadsView view;

    public DownloadsPresenter(final EventBus eventBus, final DownloadsView view) {
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
                    MessageObject msgObj = new MessageObject("The required data about the used DB could not be received.\n" +
                            "There might be problems if a download is started.\n" +
                            "ERROR: " + exception.getMessage(), getClass(), MessageType.INTERNAL_WARNING);
                    eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
                    Console.error(getClass() + " ERROR: " + exception.getMessage());
                    view.setInitialState();
                }
            });
        }
        catch (RequestException ex) {
            view.setDbName("NO_DB_RETRIEVED");
            MessageObject msgObj = new MessageObject("The required data about the used DB could not be received.\n" +
                    "There might be problems if a download is started.\n" +
                    "ERROR: " + ex.getMessage(), getClass(), MessageType.INTERNAL_WARNING);
            eventBus.fireELVEvent(ELVEventType.INTERNAL_MESSAGE, msgObj);
            Console.error(getClass() + " ERROR: " + ex.getMessage());
            view.setInitialState();
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

    @Override
    public void swapToMolecules(Pathway pathway) {
        //Molecules Tab needs to be loaded so this is necessary here, eventhough it is not the best way to do it
        eventBus.fireELVEvent(ELVEventType.STATE_MANAGER_DETAILS_TAB_SELECTED, DetailsTabType.PARTICIPATING_MOLECULES);
        eventBus.fireELVEvent(ELVEventType.MOLECULES_DOWNLOAD_REQUIRED, pathway);
    }
}
