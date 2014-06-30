package org.reactome.web.elv.client.details.tabs.analysis.view;

import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.elv.client.common.ReactomeImages;
import org.reactome.web.elv.client.common.analysis.model.AnalysisResult;
import org.reactome.web.elv.client.common.analysis.model.ResourceSummary;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.details.model.DetailsTabType;
import org.reactome.web.elv.client.details.tabs.analysis.view.widgets.found.FoundPanel;
import org.reactome.web.elv.client.details.tabs.analysis.view.widgets.notfound.NotFoundPanel;
import org.reactome.web.elv.client.details.tabs.analysis.view.widgets.results.AnalysisResultPanel;
import org.reactome.web.elv.client.details.tabs.analysis.view.widgets.results.events.PathwaySelectedEvent;
import org.reactome.web.elv.client.details.tabs.analysis.view.widgets.results.events.ResultPathwaySelectedEvent;
import org.reactome.web.elv.client.details.tabs.analysis.view.widgets.results.handlers.PathwaySelectedHandler;
import org.reactome.web.elv.client.details.tabs.analysis.view.widgets.results.handlers.ResultPathwaySelectedHandler;
import org.reactome.web.elv.client.details.tabs.analysis.view.widgets.summary.AnalysisInfoType;
import org.reactome.web.elv.client.details.tabs.analysis.view.widgets.summary.AnalysisSummaryPanel;
import org.reactome.web.elv.client.details.tabs.analysis.view.widgets.summary.events.OptionSelectedEvent;
import org.reactome.web.elv.client.details.tabs.analysis.view.widgets.summary.events.ResourceChangedEvent;
import org.reactome.web.elv.client.details.tabs.analysis.view.widgets.summary.handlers.OptionSelectedHandler;
import org.reactome.web.elv.client.details.tabs.analysis.view.widgets.summary.handlers.ResourceChangedHandler;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class AnalysisTabViewImpl implements AnalysisTabView, ResourceChangedHandler, OptionSelectedHandler, PathwaySelectedHandler, ResultPathwaySelectedHandler {
    private final DetailsTabType TYPE = DetailsTabType.ANALYSIS;
    private Presenter presenter;

    private String token;
    private List<String> resources;
    private List<String> columnNames;
    private DockLayoutPanel container;
    private HTMLPanel title;

    private AnalysisSummaryPanel summaryPanel;
    private StackLayoutPanel stackPanel;

    private AnalysisResultPanel analysisResultPanel;
    private FoundPanel pathwayPanel;
    private NotFoundPanel notFoundPanel;

    public AnalysisTabViewImpl() {
        this.container = new DockLayoutPanel(Style.Unit.EM);

        this.stackPanel = new StackLayoutPanel(Style.Unit.EM){
            /**
             * There is a problem with the layout when the data is ready
             * before the animation is finished where the table does not
             * fill the available space. This workaround fixes it
             * NOTE: Better listening to animation finished event
             */
            @Override
            public void showWidget(Widget child) {
                super.showWidget(child);
                (new Timer() {
                    @Override
                    public void run() {
                        forceLayout();
                    }
                }).schedule(getAnimationDuration() + 10);
            }
        };
        this.stackPanel.getElement().getStyle().setBackgroundColor("transparent");
        this.stackPanel.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
        this.title = new HTMLPanel(this.TYPE.getTitle());

        this.analysisResultPanel = new AnalysisResultPanel();
        this.analysisResultPanel.addResultPathwaySelectedHandler(this);
        this.analysisResultPanel.addPathwaySelectedHandler(this);
        this.analysisResultPanel.getElement().getStyle().setBackgroundColor("transparent");
        this.analysisResultPanel.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
        this.stackPanel.add(this.analysisResultPanel, "Analysis Result", 0);

        this.pathwayPanel = new FoundPanel();
        this.pathwayPanel.getElement().getStyle().setBackgroundColor("transparent");
        this.pathwayPanel.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
        this.stackPanel.add(this.pathwayPanel, "Found", 0);

        this.notFoundPanel = new NotFoundPanel();
        this.notFoundPanel.getElement().getStyle().setBackgroundColor("transparent");
        this.notFoundPanel.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
        this.stackPanel.add(this.notFoundPanel, "Not found", 0);

        this.container.add(this.stackPanel);
        setInitialState();
    }

    @Override
    public Widget asWidget() {
        return this.container;
    }

    @Override
    public DetailsTabType getDetailTabType() {
        return TYPE;
    }

    @Override
    public HTMLPanel getTitle() {
        return title;
    }

    @Override
    public void clearSelection() {
        if(analysisResultPanel!=null){
            this.analysisResultPanel.clearSelection();
        }
    }

//    @Override
    public void refreshTitle(Integer foundPathways){
        String found = NumberFormat.getDecimalFormat().format(foundPathways);
        this.title.getElement().setInnerHTML(TYPE.getTitle() + " (" + found + ")");
    }

//    @Override
    public void scrollToSelected() {
        if(this.analysisResultPanel!=null){
            this.analysisResultPanel.scrollToSelected();
        }
    }

    @Override
    public void selectPathway(Pathway pathway) {
        if(pathway!=null){
            this.analysisResultPanel.selectPathway(pathway.getDbId());
        }
    }

    @Override
    public void setInitialState() {
        this.token = null;
        this.summaryPanel = null; //TODO: Testing
        this.title.getElement().setInnerHTML(TYPE.getTitle());
        this.container.clear();

        FlowPanel fp = new FlowPanel();
        fp.add(new InlineLabel("Your analysis result will display here after you submit your data using the Analysis tool. Please open the Analysis tools by clicking the icon ("));
        fp.add(new Image(ReactomeImages.INSTANCE.analysisTool()));
        fp.add(new InlineLabel(") in the top bar above pathway browser window"));
        this.container.add(fp);
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showInstanceDetails(Pathway pathway, DatabaseObject databaseObject) {
        displayResultsIfNecessary(pathway, databaseObject);
    }

    @Override
    public boolean showInstanceDetailsIfExists(Pathway pathway, DatabaseObject databaseObject) {
        displayResultsIfNecessary(pathway, databaseObject);
        return true;
    }

    private void displayResultsIfNecessary(Pathway pathway, DatabaseObject databaseObject){
        if(this.analysisResultPanel!=null){
            Long selected = this.analysisResultPanel.getSelected();
            Long toSelect = (databaseObject!=null && databaseObject instanceof Pathway) ? databaseObject.getDbId() : pathway.getDbId();
            if(!toSelect.equals(selected)){
                this.stackPanel.showWidget(this.analysisResultPanel);
            }
        }
        this.scrollToSelected();
    }

    @Override
    public void showResult(AnalysisResult analysisResult, String resource) {
        this.refreshTitle(analysisResult.getPathwaysFound());

        if(this.summaryPanel==null || !this.summaryPanel.getToken().equals(analysisResult.getSummary().getToken())){
            this.resources = new LinkedList<String>();
            for (ResourceSummary resourceSummary : analysisResult.getResourceSummary()) {
                resources.add(resourceSummary.getResource());
            }
            this.columnNames = analysisResult.getExpression().getColumnNames();

            this.container.clear();
            this.summaryPanel = new AnalysisSummaryPanel(analysisResult);
            this.summaryPanel.addOptionSelectedHandler(this);
            this.summaryPanel.addResourceChangeHandler(this);

            this.container.addNorth(summaryPanel, 2.6);
            this.analysisResultPanel.showResult(analysisResult, resource);
            this.container.add(stackPanel);
            this.stackPanel.showWidget(this.analysisResultPanel);

            this.token = analysisResult.getSummary().getToken();
            Integer notFound = analysisResult.getIdentifiersNotFound();
            this.notFoundPanel.setAnalysisDetails(this.token, notFound);

            this.pathwayPanel.setResource(resource);
        }else{
            this.analysisResultPanel.showResult(analysisResult, resource);
        }

    }

    @Override
    public void onOptionSelected(OptionSelectedEvent event) {
        switch (event.getAnalysisInfoType()){
            case PATHWAYS_FOUND:
                this.stackPanel.showWidget(this.analysisResultPanel);
                break;
            case NOT_FOUND:
                this.stackPanel.showWidget(this.notFoundPanel);
                this.notFoundPanel.showNotFound(token, columnNames);
                break;
        }
    }

    @Override
    public void onPathwayFoundEntitiesSelected(ResultPathwaySelectedEvent event) {
        this.stackPanel.showWidget(this.pathwayPanel);
        Long pathwayId = event.getPathwaySummary().getDbId();
        this.pathwayPanel.setAnalysisDetails(this.token, pathwayId);
        this.pathwayPanel.showFound(this.resources, this.columnNames);
        this.summaryPanel.setDownAll(false);
    }

    @Override
    public void onResourceChanged(ResourceChangedEvent event) {
        this.summaryPanel.setSelected(AnalysisInfoType.PATHWAYS_FOUND);
        this.analysisResultPanel.setResource(event.getResource());
        this.pathwayPanel.setResource(event.getResource());
        presenter.onResourceSelected(event.getResource());
    }

    @Override
    public void onPathwaySelected(PathwaySelectedEvent event) {
        this.presenter.onPathwaySelected(event.getSpecies(), event.getDiagram(), event.getPathway());
    }

    @Override
    public void showWaitingMessage(){
        HorizontalPanel message = new HorizontalPanel();
        Image loader = new Image(ReactomeImages.INSTANCE.loader());
        message.add(loader);

        Label label = new Label("Loading the analysis result, please wait...");
        label.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        message.add(label);

        this.container.clear();
        this.container.add(message);
        this.title.getElement().setInnerHTML(TYPE.getTitle());
    }
}
