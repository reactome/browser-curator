package org.reactome.web.elv.client.popups;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.elv.client.common.ReactomeImages;

/**
 * This object shows the diagram for the pathway browser key and keeps it in the preferred position
 *
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DiagramKey extends PopupPanel implements CloseHandler<PopupPanel>, ResizeHandler, PopupPanel.PositionCallback {

    PopupManager manager;

    class CloseButton extends Button implements ClickHandler {
        CloseButton() {
            super("Close diagram key");
            addClickHandler(this);
        }

        public void onClick(ClickEvent event) {
            manager.closeDiagramKey();
        }
    }

    public DiagramKey(PopupManager manager) {
        // PopupPanel's constructor takes 'auto-hide' as its boolean parameter.
        // If this is set, the panel closes itself automatically when the user
        // clicks outside of it.
        super(false);

        this.manager = manager;
        setAnimationEnabled(true);

        // PopupPanel is a SimplePanel, so you have to set it's widget property to
        // whatever you want its contents to be.
        setWidget(getPopupContent());
        getElement().getStyle().setZIndex(100);

        setPopupPositionAndShow(this);
        addCloseHandler(this);
        Window.addResizeHandler(this);
    }

    // Calculates and set the popup preferred position -> top right
    private void setPreferredPosition() {
        if (isShowing()) {
            int left = Window.getClientWidth() - this.getOffsetWidth();
            setPopupPosition(left, 45);
        }
    }

    @Override
    public void show() {
        super.show();
        // after show it, it is necessary to reposition the popup to
        // the preferred position because it is possible the user has
        // changed the window size while the popup was hidden
        setPreferredPosition();
    }



    public void setPosition(int offsetWidth, int offsetHeight) {
        setPreferredPosition();
    }

    public void onResize(ResizeEvent event) {
        setPreferredPosition();
    }

    private Widget getPopupContent() {
        VerticalPanel vp = new VerticalPanel();
        vp.add(new Image(ReactomeImages.INSTANCE.diagramKey()));

        HTMLPanel moreDetail = getMoreDetailsLink();
        vp.add(moreDetail);
        vp.setCellHorizontalAlignment(moreDetail, HasHorizontalAlignment.ALIGN_CENTER);

        CloseButton close = new CloseButton();
        vp.add(close);
        vp.setCellHorizontalAlignment(close, HasHorizontalAlignment.ALIGN_RIGHT);

        return vp;
    }

    private HTMLPanel getMoreDetailsLink() {
        String moreDetailURL = "/userguide/Usersguide.html#Pathway_Diagrams";
        String moreDetailTITLE = "Click here for more detailed diagram key";
        String moreDetailHTML = "<a target=\"_blank\" href=\"" + moreDetailURL + "\">" + moreDetailTITLE + "</a>";
        HTMLPanel moreDetail = new HTMLPanel(moreDetailHTML);
        moreDetail.getElement().getStyle().setMarginBottom(15, Style.Unit.PX);
        return moreDetail;
    }

    @Override
    public void onClose(CloseEvent<PopupPanel> popupPanelCloseEvent) {
        manager.diagramKeyClosed();
    }
}