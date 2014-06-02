package org.reactome.web.elv.client.center.content.welcome.resource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface ReactomeHelp extends ClientBundle {

    public ReactomeHelp HELP_RESOURCE = GWT.create(ReactomeHelp.class);

    @Source("help.html")
    TextResource getHelp();
}
