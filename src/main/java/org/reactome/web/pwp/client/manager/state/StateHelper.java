package org.reactome.web.pwp.client.manager.state;

import org.reactome.web.pwp.client.common.model.classes.DatabaseObject;
import org.reactome.web.pwp.client.common.model.classes.Event;
import org.reactome.web.pwp.client.common.model.classes.Pathway;
import org.reactome.web.pwp.client.common.model.client.RESTFulClient;
import org.reactome.web.pwp.client.common.model.client.handlers.AncestorsCreatedHandler;
import org.reactome.web.pwp.client.common.model.handlers.DatabaseObjectLoadedHandler;
import org.reactome.web.pwp.client.common.model.util.Ancestors;
import org.reactome.web.pwp.client.common.model.util.Path;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public abstract class StateHelper {

    public static List<Event> getEvents(String[] identifiers, Map<String, DatabaseObject> map) {
        List<Event> rtn = new LinkedList<>();
        for (String identifier : identifiers) {
            DatabaseObject databaseObject = map.get(identifier);
            if (databaseObject instanceof Event) {
                rtn.add((Event) databaseObject);
            }
        }
        return rtn;
    }

    public interface EventWithDiagramHandler {
        void setEventWithDiagram(Event event, Path path);
        void onEventWithDiagramRetrievalError(Throwable throwable);
    }

    public static void getEventWithDiagram(Event event, Path path, final EventWithDiagramHandler handler){
        // Trying first to figure out the diagram from the provided path (if there is any)
        if(path != null && !path.isEmpty()){
            Event diagram = path.getLastEventWithDiagram();
            if(diagram != null){
                handler.setEventWithDiagram(diagram, path);
                return;
            }
        }

        RESTFulClient.getAncestors(event, new AncestorsCreatedHandler() {
            @Override
            public void onAncestorsLoaded(Ancestors ancestors) {
                for (final Path ancestor : ancestors) {
                    Event diagram = ancestor.getLastEventWithDiagram();
                    if (diagram != null) { // The event with diagram object needs to be filled before sending it back
                        diagram.load(new DatabaseObjectLoadedHandler() {
                            @Override
                            public void onDatabaseObjectLoaded(DatabaseObject databaseObject) {
                                handler.setEventWithDiagram((Pathway) databaseObject, ancestor);
                            }

                            @Override
                            public void onDatabaseObjectError(Throwable trThrowable) {
                                handler.onEventWithDiagramRetrievalError(trThrowable);
                            }
                        });
                        return;
                    }
                }
                handler.setEventWithDiagram(null, new Path());
            }

            @Override
            public void onAncestorsError(Throwable exception) {
                handler.onEventWithDiagramRetrievalError(exception);
            }
        });
    }
}
