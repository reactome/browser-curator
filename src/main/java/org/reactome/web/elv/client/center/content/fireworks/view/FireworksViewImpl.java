package org.reactome.web.elv.client.center.content.fireworks.view;

import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.elv.client.center.content.fireworks.util.Resources;
import org.reactome.web.fireworks.client.FireworksFactory;
import org.reactome.web.fireworks.client.FireworksViewer;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class FireworksViewImpl implements FireworksView {
    private Presenter presenter;
    private FireworksViewer fireworks;


    public FireworksViewImpl() {
        //For the time being the json is retrieved from a static file in resources
        TextResource json = Resources.INSTANCE.getFireworks();
        this.fireworks = FireworksFactory.createFireworksViewer(json);
    }

    @Override
    public Widget asWidget() {
        return this.fireworks.asWidget();
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }
}
