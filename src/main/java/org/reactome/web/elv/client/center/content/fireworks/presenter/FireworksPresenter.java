package org.reactome.web.elv.client.center.content.fireworks.presenter;

import org.reactome.web.elv.client.center.content.fireworks.view.FireworksView;
import org.reactome.web.elv.client.common.Controller;
import org.reactome.web.elv.client.common.EventBus;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class FireworksPresenter extends Controller implements FireworksView.Presenter {

    private FireworksView view;

    public FireworksPresenter(EventBus eventBus, FireworksView view) {
        super(eventBus);
        this.view = view;
        this.view.setPresenter(this);
    }
}
