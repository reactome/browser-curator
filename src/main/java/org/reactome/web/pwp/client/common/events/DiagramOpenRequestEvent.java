package org.reactome.web.pwp.client.common.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.pwp.client.common.handlers.DiagramOpenRequestHandler;
import org.reactome.web.pwp.client.common.model.classes.CellLineagePath;
import org.reactome.web.pwp.client.common.model.classes.Event;
import org.reactome.web.pwp.client.common.model.classes.Pathway;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DiagramOpenRequestEvent extends GwtEvent<DiagramOpenRequestHandler> {
    public static final Type<DiagramOpenRequestHandler> TYPE = new Type<>();

    private Event pathwayOrCellLineagePath;

    public DiagramOpenRequestEvent(Pathway pathway) {
        this.pathwayOrCellLineagePath = pathway;
    }

    public DiagramOpenRequestEvent(CellLineagePath cellLineagePath) {
        this.pathwayOrCellLineagePath = cellLineagePath;
    }

    @Override
    public Type<DiagramOpenRequestHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(DiagramOpenRequestHandler handler) {
        handler.onDiagramOpenRequest(this);
    }

    public Event getEvent() {
        return this.pathwayOrCellLineagePath;
    }

    @Override
    public String toString() {
        return "DiagramOpenRequestEvent{" +
                "pathwayOrCellLineagePath=" + getEvent() +
                '}';
    }
}
