package org.reactome.web.elv.client.details.events;

import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Event;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.common.model.Path;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface InstanceSelectedHandler {

    public void eventSelected(Path path, Pathway pathway, Event event);

    public void instanceSelected(DatabaseObject databaseObject);
}