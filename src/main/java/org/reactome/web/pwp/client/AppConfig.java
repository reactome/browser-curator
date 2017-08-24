package org.reactome.web.pwp.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class AppConfig extends JavaScriptObject {

    protected AppConfig() {
    }

    public static native boolean getEventBusVerbose() /*-{
        return $wnd.CURATOR != null ? $wnd.EVENT_BUS_VERBOSE : false;
    }-*/;
}
