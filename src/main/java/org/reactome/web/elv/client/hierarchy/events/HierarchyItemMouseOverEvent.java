package org.reactome.web.elv.client.hierarchy.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.elv.client.hierarchy.handlers.HierarchyItemMouseOverHandler;
import org.reactome.web.elv.client.hierarchy.model.HierarchyItem;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class HierarchyItemMouseOverEvent extends GwtEvent<HierarchyItemMouseOverHandler> {
    public static Type<HierarchyItemMouseOverHandler> TYPE = new Type<HierarchyItemMouseOverHandler>();

    private HierarchyItem item;

    public HierarchyItemMouseOverEvent(HierarchyItem item) {
        this.item = item;
    }

    @Override
    public Type<HierarchyItemMouseOverHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(HierarchyItemMouseOverHandler handler) {
        handler.onHierarchyItemMouseOver(this);
    }

    public HierarchyItem getItem() {
        return item;
    }
}
