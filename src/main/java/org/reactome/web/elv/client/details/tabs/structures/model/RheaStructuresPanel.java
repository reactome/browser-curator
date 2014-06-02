package org.reactome.web.elv.client.details.tabs.structures.model;

import com.google.gwt.user.client.ui.HTMLPanel;
import org.reactome.web.elv.client.common.data.model.DatabaseIdentifier;
import org.reactome.web.elv.client.common.data.model.Event;
import org.reactome.web.elv.client.details.tabs.structures.events.StructureLoadedEvent;
import uk.ac.ebi.pwp.widgets.rhea.client.RheaViewer;
import uk.ac.ebi.pwp.widgets.rhea.events.ReactionStructureLoadedEvent;
import uk.ac.ebi.pwp.widgets.rhea.handlers.ReactionStructureLoadedHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class RheaStructuresPanel extends StructuresPanel<Event> implements ReactionStructureLoadedHandler {
    private Map<Event, RheaViewer> eventViewerMap = new HashMap<Event, RheaViewer>();

    @Override
    public void add(Event element) {
        if(eventViewerMap.keySet().isEmpty()){
            this.container.clear();
        }

        int rheaRefs = 0;
        if(!this.eventViewerMap.keySet().contains(element)){
            for (DatabaseIdentifier databaseIdentifier : element.getCrossReference()) {
                String[] aux = databaseIdentifier.getDisplayName().split(":");
                if(aux[0].toLowerCase().equals("rhea")){
                    RheaViewer rheaPanel = new RheaViewer(aux[1]);
                    rheaPanel.addStructureLoadedHandler(this);
                    this.eventViewerMap.put(element, rheaPanel);
                    this.structuresRequired++;
                    this.container.add(rheaPanel);
                    rheaRefs++;
                }
            }
        }

        if(rheaRefs==0){
            this.setEmpty();
        }
    }

    @Override
    public void onReactionStructureLoaded(ReactionStructureLoadedEvent event) {
        this.structuresLoaded++;
        fireEvent(new StructureLoadedEvent());
    }

    @Override
    public void setEmpty(){
        this.container.clear();
        this.container.add(new HTMLPanel("This event does not contain Rhea structures to be shown"));
    }
}