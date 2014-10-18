package org.reactome.web.elv.client.center.content.fireworks.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface Resources extends ClientBundle {

    Resources INSTANCE = (Resources) GWT.create(Resources.class);

    @Source("fireworks.json")
    TextResource getFireworks();
}
