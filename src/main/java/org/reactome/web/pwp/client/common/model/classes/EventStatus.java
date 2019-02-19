package org.reactome.web.pwp.client.common.model.classes;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public enum EventStatus {
    REGULAR(""),
    NEW("NEW"),
    UPDATED("UPDATED");

    private String status;

    EventStatus(String status) {
        this.status = status;
    }

    public static org.reactome.web.pwp.client.common.model.classes.EventStatus getEventStatus(String status) {
        for (org.reactome.web.pwp.client.common.model.classes.EventStatus eventStatus : values()) {
            if (eventStatus.status.equals(status)) return eventStatus;
        }
        return REGULAR;
    }
}