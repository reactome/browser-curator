package org.reactome.web.elv.client.common.data.model;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public enum EventStatus {
    REGULAR(""),
    NEW("NEW"),
    UPDATED("UPDATED");

    private String status;

    private EventStatus(String status) {
        this.status = status;
    }

    public static EventStatus getEventStatus(String status){
        for (EventStatus eventStatus : EventStatus.values()) {
            if(eventStatus.status.equals(status)) return eventStatus;
        }
        return REGULAR;
    }
}