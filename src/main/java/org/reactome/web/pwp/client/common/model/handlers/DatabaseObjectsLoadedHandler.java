package org.reactome.web.pwp.client.common.model.handlers;

import org.reactome.web.pwp.client.common.model.classes.DatabaseObject;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface DatabaseObjectsLoadedHandler<T extends DatabaseObject> {
    void onDatabaseObjectLoaded(List<T> objects);
    void onDatabaseObjectError(Throwable trThrowable);
}
