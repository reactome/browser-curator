package org.reactome.web.elv.client.details.events;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Event;
import org.reactome.web.elv.client.details.tabs.molecules.model.data.Molecule;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
public interface DataRequiredHandler extends EventHandler {

    void onDataRequired(DatabaseObject databaseObject);

    void onMoleculeDataRequired(final Molecule molecule);

    void onPathRequired(Event event);

    void onReferencesRequired(Long dbId);
}
