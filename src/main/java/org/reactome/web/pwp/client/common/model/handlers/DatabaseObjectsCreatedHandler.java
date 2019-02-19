package org.reactome.web.pwp.client.common.model.handlers;

import org.reactome.web.pwp.client.common.model.classes.DatabaseObject;

import java.util.Map;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface DatabaseObjectsCreatedHandler {
    void onDatabaseObjectsLoaded(Map<String, DatabaseObject> databaseObjects);
    void onDatabaseObjectError(Throwable exception);
}
