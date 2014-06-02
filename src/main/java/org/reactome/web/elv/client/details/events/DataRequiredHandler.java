package org.reactome.web.elv.client.details.events;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Event;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface DataRequiredHandler extends EventHandler {

    void onDataRequired(DatabaseObject databaseObject);

    void onPathRequired(Event event);

    void onReferencesRequired(Long dbId);
}
