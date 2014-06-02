package org.reactome.web.elv.client.details.tabs.processes.events;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.elv.client.common.data.model.EntityWithAccessionedSequence;
import org.reactome.web.elv.client.common.data.model.Event;
import org.reactome.web.elv.client.common.data.model.PhysicalEntity;
import org.reactome.web.elv.client.details.tabs.processes.model.widgets.container.*;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface ProcessesDataHandler extends EventHandler {

    void onComplexesRequired(ComplexesContainer container, PhysicalEntity physicalEntity);

    void onEntitySetRequired(EntitySetContainer container, PhysicalEntity physicalEntity);

    void onPathwaysForEntitiesRequired(InvolvedPathwaysContainer container, PhysicalEntity physicalEntity);

    void onPathwaysForEventsRequired(InvolvedPathwaysContainer container, Event event);

    void onReactionsWhereInputRequired(ReactionsAsInputContainer container, PhysicalEntity physicalEntity);

    void onReactionsWhereOutputRequired(ReactionsAsOutputContainer container, PhysicalEntity physicalEntity);

    void onOtherFormsOfEWASRequired(OtherFormsForEwasContainer container, EntityWithAccessionedSequence ewas);
}
