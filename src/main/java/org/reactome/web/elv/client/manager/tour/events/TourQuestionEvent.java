package org.reactome.web.elv.client.manager.tour.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.elv.client.manager.tour.handlers.TourQuestionEventHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class TourQuestionEvent extends GwtEvent<TourQuestionEventHandler> {
    public static Type<TourQuestionEventHandler> TYPE = new GwtEvent.Type<TourQuestionEventHandler>();

    public enum Answer { YES, NO }

    private Answer answer;

    public TourQuestionEvent(Answer answer) {
        this.answer = answer;
    }

    public Answer getAnswer() {
        return answer;
    }

    @Override
    public Type<TourQuestionEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(TourQuestionEventHandler handler) {
        handler.onTourDialogOptionSelected(this);
    }
}
