package org.reactome.web.pwp.client.common.model.handlers;

import java.util.Map;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface MapLoadedHandler<S,T> {
    void onMapLoaded(Map<S, T> databaseObject);
    void onMapError(Throwable throwable);
}
