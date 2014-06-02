package org.reactome.web.elv.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import org.reactome.web.elv.client.common.EventBus;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 *
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class PathwayBrowser implements EntryPoint {
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
        EventBus eventBus = new EventBus(null);
        AppController appController = new AppController(eventBus);
        appController.go(RootLayoutPanel.get());
        this.removeStaticMessages();
	}

    private void removeStaticMessages(){
        DOM.getElementById("appLoadMessage").removeFromParent();
    }
}
