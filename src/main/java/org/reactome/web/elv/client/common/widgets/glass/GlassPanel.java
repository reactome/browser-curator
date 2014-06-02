package org.reactome.web.elv.client.common.widgets.glass;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class GlassPanel extends SimplePanel implements ProvidesResize, RequiresResize {
    Widget target;

    public GlassPanel(Widget target) {
        this.target = target;
        this.setStyleName("gwt-PopupPanelGlass");
        this.setSize();
        this.getElement().getStyle().setZIndex(10);
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        setSize();
    }

    @Override
    public void onResize() {
        setSize();
    }

    private void setSize(){
        Style style = getElement().getStyle();
        style.setPosition(Style.Position.ABSOLUTE);
        style.setLeft(target.getAbsoluteLeft(), Style.Unit.PX);
        style.setTop(target.getAbsoluteTop(), Style.Unit.PX);
        style.setWidth(target.getOffsetWidth(), Style.Unit.PX);
        style.setHeight(target.getOffsetHeight(), Style.Unit.PX);
    }
}
