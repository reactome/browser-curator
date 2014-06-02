package org.reactome.web.elv.client.manager.tour.dialogs;


import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import org.reactome.web.elv.client.manager.tour.events.TourQuestionEvent;
import org.reactome.web.elv.client.manager.tour.handlers.TourQuestionEventHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class TourInitialQuestion extends PopupPanel implements ClickHandler {
    private Anchor tour;
    private Anchor hide;

    public TourInitialQuestion() {
        this.tour = new Anchor("Tour");
        this.tour.addClickHandler(this);
        this.hide = new Anchor("Hide");
        this.hide.addClickHandler(this);

        FlowPanel content = new FlowPanel();
        content.add(this.tour);
        content.add(new InlineLabel(" this pathway browser? "));
        content.add(this.hide);
        setWidget(content);
        setStyleName("app-Tour-InitialPanel");
    }

    public HandlerRegistration addTourQuestionEventHandler(TourQuestionEventHandler handler){
        return this.addHandler(handler, TourQuestionEvent.TYPE);
    }

    @Override
    public void show() {
        super.show();
        Style style = this.getElement().getStyle();
        style.setTop(6, Style.Unit.PX);
        style.setLeft(655, Style.Unit.PX);
    }

    @Override
    public void onClick(ClickEvent event) {
        event.stopPropagation();
        this.hide();
        Anchor btn = (Anchor) event.getSource();
        if(btn.equals(this.tour)){
            fireEvent(new TourQuestionEvent(TourQuestionEvent.Answer.YES));
        }else if(btn.equals(this.hide)){
            fireEvent(new TourQuestionEvent(TourQuestionEvent.Answer.NO));
        }
    }
}
