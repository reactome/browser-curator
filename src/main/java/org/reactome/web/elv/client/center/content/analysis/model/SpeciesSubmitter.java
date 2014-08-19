package org.reactome.web.elv.client.center.content.analysis.model;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.elv.client.center.content.analysis.event.AnalysisCompletedEvent;
import org.reactome.web.elv.client.center.content.analysis.event.AnalysisErrorEvent;
import org.reactome.web.elv.client.center.content.analysis.event.AnalysisErrorType;
import org.reactome.web.elv.client.center.content.analysis.handler.AnalysisCompletedEventHandler;
import org.reactome.web.elv.client.center.content.analysis.handler.AnalysisErrorEventHandler;
import org.reactome.web.elv.client.center.content.analysis.resources.AnalysisExamples;
import org.reactome.web.elv.client.center.content.analysis.style.AnalysisStyleFactory;
import org.reactome.web.elv.client.common.ReactomeImages;
import org.reactome.web.elv.client.common.analysis.factory.AnalysisModelException;
import org.reactome.web.elv.client.common.analysis.factory.AnalysisModelFactory;
import org.reactome.web.elv.client.common.analysis.helper.AnalysisHelper;
import org.reactome.web.elv.client.common.analysis.model.AnalysisResult;
import org.reactome.web.elv.client.common.data.model.Species;
import org.reactome.web.elv.client.common.widgets.DialogBoxFactory;
import org.reactome.web.elv.client.manager.state.AdvancedState;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class SpeciesSubmitter extends FlowPanel implements ClickHandler {

    private Image loading;
    private ListBox species;

    public SpeciesSubmitter() {
        //noinspection GWTStyleCheck
        setStyleName("clearfix");
        addStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisBlock());

        SimplePanel title = new SimplePanel();
        title.add(new InlineLabel("Species Comparison"));
        title.addStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisTitle());
        add(title);

        SimplePanel explanation = new SimplePanel();
        explanation.getElement().setInnerHTML(AnalysisExamples.EXAMPLES.speciesComparisonInfo().getText());
        add(explanation);

        FlowPanel submissionPanel = new FlowPanel();
        submissionPanel.addStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisSubmission());
        submissionPanel.addStyleName(AnalysisStyleFactory.getAnalysisStyle().analysisMainSubmitter());
        submissionPanel.add(new Label("Compare Homo sapiens with "));
        submissionPanel.add(new Button("Compare", this));
        this.loading = new Image(ReactomeImages.INSTANCE.loader());
        this.loading.setVisible(false);
        submissionPanel.add(this.loading);
        this.species = new ListBox(false);
        submissionPanel.add(this.species);
        add(submissionPanel);
    }

    public HandlerRegistration addAnalysisCompletedEventHandler(AnalysisCompletedEventHandler handler){
        return this.addHandler(handler, AnalysisCompletedEvent.TYPE);
    }

    public HandlerRegistration addAnalysisErrorEventHandler(AnalysisErrorEventHandler handler){
        return this.addHandler(handler, AnalysisErrorEvent.TYPE);
    }

    @Override
    public void onClick(ClickEvent event) {
        Long dbId = Long.valueOf(species.getValue(species.getSelectedIndex()));
        if(dbId==-1) {
            //ToDo: Check for new Error Handling
            DialogBoxFactory.alert("Species comparison", "Please select a species to compare with");
            return;
        }

        this.loading.setVisible(true);
        String url = AnalysisHelper.URL_PREFIX + "/species/homoSapiens/" + dbId + "?page=1";
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    if(!response.getStatusText().equals("OK")){
                        fireEvent(new AnalysisErrorEvent(AnalysisErrorType.FROM_RESPONSE.setMessage(response)));
                    }else{
                        try {
                            AnalysisResult result = AnalysisModelFactory.getModelObject(AnalysisResult.class, response.getText());
                            fireEvent(new AnalysisCompletedEvent(result));
                        } catch (AnalysisModelException e) {
                            fireEvent(new AnalysisErrorEvent(AnalysisErrorType.RESULT_FORMAT));
                            //ToDo: Look into new Error Handling
                        }
                    }
                    loading.setVisible(false);
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    loading.setVisible(false);
                    fireEvent(new AnalysisErrorEvent(AnalysisErrorType.SERVICE_UNAVAILABLE));
                    //ToDo: Look into new Error Handling
                }
            });
        }catch (RequestException ex) {
            loading.setVisible(false);
            fireEvent(new AnalysisErrorEvent(AnalysisErrorType.SERVICE_UNAVAILABLE));
            //ToDo: Look into new Error Handling
        }
    }

    public void setSpeciesList(List<Species> speciesList) {
//        this.speciesList = speciesList;
        this.species.addItem("Select a species...", "-1");
        for (Species species : speciesList) {
            if(!species.getDbId().equals(AdvancedState.DEFAULT_SPECIES_ID)){
                this.species.addItem(species.getDisplayName(), species.getDbId().toString());
            }
        }
    }
}
