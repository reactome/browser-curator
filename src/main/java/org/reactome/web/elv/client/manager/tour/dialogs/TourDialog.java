package org.reactome.web.elv.client.manager.tour.dialogs;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.elv.client.common.ReactomeImages;
import org.reactome.web.elv.client.manager.tour.events.TourQuestionEvent;
import org.reactome.web.elv.client.manager.tour.handlers.TourQuestionEventHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@Deprecated
public class TourDialog extends DialogBox implements ResizeHandler {

    public TourDialog() {
        this.setWidth("320px");
        this.getElement().getStyle().setZIndex(20);

        this.setTitle("Pathway Browser guided tour");

        // Set the dialog box's caption.
        this.setText("Would you like to have a guided tour across the different features of this Pathway Browser?");

        // Enable animation.
        this.setAnimationEnabled(true);

        // Enable glass background.
        this.setGlassEnabled(true);

        Button ok = this.getButton(ReactomeImages.INSTANCE.yes(), "Yes, show me what is new!");
        ok.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                yes();
            }
        });

        Button nop = this.getButton(ReactomeImages.INSTANCE.no(), "No, I already know how it works");
        nop.setWidth("300px");
        nop.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                no();
            }
        });

        VerticalPanel vp = new VerticalPanel();
        vp.add(ok); vp.add(nop);
        setWidget(vp);

        this.center();

        Window.addResizeHandler(this);
    }

    public HandlerRegistration addTourDialogEventHandler(TourQuestionEventHandler handler){
        return this.addHandler(handler, TourQuestionEvent.TYPE);
    }

    protected void yes(){
        this.hide();
        this.fireEvent(new TourQuestionEvent(TourQuestionEvent.Answer.YES));
    }

    protected void no(){
        this.hide();
        this.fireEvent(new TourQuestionEvent(TourQuestionEvent.Answer.NO));
    }

    @Override
    protected void beginDragging(MouseDownEvent event) {
        event.stopPropagation();
    }

    @Override
    public void onResize(ResizeEvent event) {
        if(isShowing()){
            this.center();
        }
    }

    private Button getButton(ImageResource imgResource, String text){
        Image img = new Image(imgResource);
        img.getElement().getStyle().setMarginRight(10, Style.Unit.PX);

        FlowPanel fp = new FlowPanel();
        fp.add(img);
        fp.add(new InlineLabel(text));

        Button btn = new Button(fp.toString());
        btn.setWidth("300px");
        return btn;
    }
}
