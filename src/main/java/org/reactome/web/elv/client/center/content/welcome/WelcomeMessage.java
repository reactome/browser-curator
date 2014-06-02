package org.reactome.web.elv.client.center.content.welcome;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import org.reactome.web.elv.client.center.content.welcome.resource.ReactomeHelp;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class WelcomeMessage extends ScrollPanel {

    public WelcomeMessage() {
        super(new HTMLPanel(ReactomeHelp.HELP_RESOURCE.getHelp().getText()));
        this.setStyleName("elv-Diagram-Manual");
    }

}
