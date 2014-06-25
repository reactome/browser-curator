package org.reactome.web.elv.client.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public abstract class LocationHelper {
    public enum Location {
        PRODUCTION,
        DEV,
        CURATOR,
        OTHER
    }

    public static Location getLocation(){
        if(GWT.isScript()){
            String hostName = Window.Location.getHostName();
            if(hostName.equals("www.reactome.org") || hostName.equals("reactome.org")){
                return Location.PRODUCTION;
            }
            if(hostName.equals("reactomedev.oicr.on.ca")){
                return Location.DEV;
            }
            if(hostName.equals("reactomecurator.oicr.on.ca")){
                return Location.CURATOR;
            }
        }
        return Location.OTHER;
    }
}
