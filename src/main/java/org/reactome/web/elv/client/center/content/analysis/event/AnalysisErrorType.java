package org.reactome.web.elv.client.center.content.analysis.event;

import com.google.gwt.http.client.Response;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public enum AnalysisErrorType {

    SERVICE_UNAVAILABLE ("The analysis service is temporarily unavailable. Please wait a moment and resubmit your data"),
    PROCESSING_DATA ("The data format incorrect. Please check it and resubmit your data again"),
    RESULT_FORMAT ("Error processing the result. Please get in touch with our help desk at help@reactome.org"),
    FILE_NOT_SELECTED ("Please select a file to analyse"),
    FROM_RESPONSE ("Unknown");

    private String message;

    private AnalysisErrorType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public AnalysisErrorType setMessage(Response response){
        this.message = response.getStatusText();
        return this;
    }
}