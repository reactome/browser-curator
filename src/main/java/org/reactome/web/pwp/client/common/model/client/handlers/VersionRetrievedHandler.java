package org.reactome.web.pwp.client.common.model.client.handlers;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface VersionRetrievedHandler {
    void onVersionRetrieved(String version);
    void onVersionRetrievedError(Throwable ex);
}
