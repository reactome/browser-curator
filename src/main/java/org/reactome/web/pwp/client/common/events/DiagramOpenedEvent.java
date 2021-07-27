package org.reactome.web.pwp.client.common.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.common.handlers.DiagramOpenedHandler;
import org.reactome.web.pwp.client.common.model.classes.CellLineagePath;
import org.reactome.web.pwp.client.common.model.classes.Event;
import org.reactome.web.pwp.client.common.model.classes.Pathway;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DiagramOpenedEvent extends GwtEvent<DiagramOpenedHandler> {
    public static final Type<DiagramOpenedHandler> TYPE = new Type<>();

    private Event pathwayOrCellLineagePath;

    public DiagramOpenedEvent(Pathway pathway) {
        this.pathwayOrCellLineagePath = pathway;
    }

    public DiagramOpenedEvent(CellLineagePath cellLineagePath) {
        this.pathwayOrCellLineagePath = cellLineagePath;
    }

    @Override
    public Type<DiagramOpenedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(DiagramOpenedHandler handler) {
        handler.onDiagramOpened(this);
    }

    public Event getEvent() {
        return this.pathwayOrCellLineagePath;
    }

    @Override
    public String toString() {
        return "DiagramOpenedEvent{" +
                "pathwayOrCellLineagePath=" + pathwayOrCellLineagePath +
                '}';
    }
}
