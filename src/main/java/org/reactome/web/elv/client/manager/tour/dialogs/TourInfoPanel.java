package org.reactome.web.elv.client.manager.tour.dialogs;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class TourInfoPanel extends TopPopupPanel {
    private VerticalPanel content;

    public TourInfoPanel() {
        super(false);
        this.content = new VerticalPanel();
        this.content.setWidth("100%");
        this.content.setSpacing(5);

        this.setAnimationEnabled(true);
        this.setWidget(content);
    }

    public void addContent(Widget content){
        this.content.add(content);
    }

    private Button getExitButton(){
        Button exit = new Button("Close");
        exit.sinkEvents(Event.ONCLICK);
        exit.setStyleName("app-Tour-Close-Button");
        exit.addHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                event.stopPropagation();
                hide();
            }
        }, ClickEvent.getType());
        return exit;
    }

    @Override
    public void show() {
        this.content.add(getExitButton());
        super.show();
    }
}
