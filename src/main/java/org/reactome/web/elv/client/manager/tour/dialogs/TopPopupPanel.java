package org.reactome.web.elv.client.manager.tour.dialogs;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.elv.client.common.ReactomeImages;
import org.reactome.web.elv.client.manager.tour.events.TourCancelledEvent;
import org.reactome.web.elv.client.manager.tour.events.TourStepFinishedEvent;
import org.reactome.web.elv.client.manager.tour.handlers.TourCancelledEventHandler;
import org.reactome.web.elv.client.manager.tour.handlers.TourStepFinishedEventHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class TopPopupPanel extends DialogBox implements HasHandlers {

    public static final String IMAGE_TOKEN = "__image__";

    public TopPopupPanel() {
        this(false);
    }

    public TopPopupPanel(boolean autoHide) {
        super(autoHide);
        this.setModal(false);
        this.setAnimationEnabled(true);
        this.setText("Pathway Browser Tour");
        this.setStyleName("app-Tour-TopPanel");
        this.getCaption().asWidget().getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
    }

    public HandlerRegistration addTourStepFinishedEventHandler(TourStepFinishedEventHandler handler){
        return addHandler(handler, TourStepFinishedEvent.TYPE);
    }

    public HandlerRegistration addTourCancelledEventHandler(TourCancelledEventHandler handler){
        return addHandler(handler, TourCancelledEvent.TYPE);
    }

    public void setContent(String content, ImageResource... images){
        setContent(content, true, true, images);
    }

    public void setContent(String content, boolean hasNext, ImageResource... images){
        setContent(content, true, hasNext, images);
    }

    public void setContent(String content, boolean hasExit, boolean hasNext, ImageResource... images){
        VerticalPanel vp = new VerticalPanel(); vp.setSpacing(5);
        vp.setWidth("300px");

        int imgIndex = 0;
        String[] lines = content.split("\\n");
        for (String line : lines) {
            if(!line.contains(IMAGE_TOKEN)){
                vp.add(new InlineLabel(line));
            }else if(images.length>0){
                FlowPanel fp = new FlowPanel();
                String aux[] = line.split(IMAGE_TOKEN);
                for (int i = 0; i < aux.length-1; i++) {
                    String s = aux[i];
                    fp.add(new InlineLabel(s));
                    fp.add(new Image(images[imgIndex++]));
                }
                fp.add(new InlineLabel(aux[aux.length-1]));
                vp.add(fp);
            }
        }

        FlowPanel controls = new FlowPanel();
        controls.getElement().getStyle().setMarginTop(10, Style.Unit.PX);
        if(hasExit){
            controls.add(getExitButton());
        }
        if(hasNext){
            controls.add(getNextButton());
        }
        vp.add(controls);

        this.setWidget(vp);
        this.show();
    }

    @Override
    public void show() {
        super.show();
        Style style = this.getElement().getStyle();
        style.setTop(0, Style.Unit.PX);
        style.setLeft(650, Style.Unit.PX);
    }

    private Widget getNextButton(){
        FlowPanel caption = new FlowPanel();
        caption.add(new InlineLabel("Next"));
        Image img = new Image(ReactomeImages.INSTANCE.next());
        img.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        caption.add(img);


        Button next = new Button(caption.toString());
        next.setStyleName("app-Tour-Next-Button");
        next.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                event.stopPropagation();
                hide();
                fireEvent(new TourStepFinishedEvent());
            }
        });
        return next;
    }

    private Widget getExitButton(){
        FlowPanel caption = new FlowPanel();
        Image img = new Image(ReactomeImages.INSTANCE.close());
        img.getElement().getStyle().setMarginRight(5, Style.Unit.PX);
        caption.add(img);
        caption.add(new InlineLabel("Exit Tour"));
        Button exit = new Button(caption.toString());
        exit.sinkEvents(Event.ONCLICK);
        exit.setStyleName("app-Tour-Close-Button");
        exit.addHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                event.stopPropagation();
                hide();
                fireEvent(new TourCancelledEvent());
            }
        }, ClickEvent.getType());
        return exit;
    }
}