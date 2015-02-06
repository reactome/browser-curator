package org.reactome.web.elv.client.center.content.diagram.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import org.reactome.diagram.client.PathwayDiagramPanel;
import org.reactome.diagram.event.*;
import org.reactome.diagram.expression.event.ExpressionOverlayStopEvent;
import org.reactome.diagram.expression.event.ExpressionOverlayStopEventHandler;
import org.reactome.diagram.model.GraphObject;
import org.reactome.web.elv.client.center.content.diagram.model.DiagramContainer;
import org.reactome.web.elv.client.center.content.diagram.model.FigureContainer;
import org.reactome.web.elv.client.common.analysis.helper.AnalysisHelper;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Figure;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.common.widgets.button.DiagramButton;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DiagramViewImpl implements DiagramView, SelectionEventHandler, PathwayChangeEventHandler, ZoomedOutEventHandler,
        ParticipatingMoleculeSelectionEventHandler , SubpathwaySelectionEventHandler, ExpressionOverlayStopEventHandler {
    private static final String RESTFUL_WS_FOLDER = "ReactomeRESTfulAPI/RESTfulWS/";

    private TabLayoutPanel container;
    private PathwayDiagramPanel diagram;
    private DiagramContainer diagramContainer;
    private FigureContainer figureContainer;

    private String token;

    private Presenter presenter;

    private List<Long> selectedDBIds;
    private List<Long> objectsInDiagram;

    public DiagramViewImpl() {
        this.container = new TabLayoutPanel(0, Style.Unit.PX){
            @Override
            public void setVisible(boolean visible) {
                super.setVisible(visible);
                super.onResize();
            }
        };
        this.container.setAnimationDuration(500);
        this.container.addStyleName("elv-Diagram-Container");

        initializeDiagram();
        initializeFigurePanel();

        selectedDBIds = new LinkedList<Long>();
        objectsInDiagram = new LinkedList<Long>();
    }

    private void initializeDiagram(){
        this.diagram = new PathwayDiagramPanel();
        this.diagram.addStyleName("elv-Diagram-Holder");
        String url;

        if(!GWT.isScript())
            url = GWT.getHostPageBaseURL() + RESTFUL_WS_FOLDER;
        else
            url = "/" + RESTFUL_WS_FOLDER;

        this.diagram.setRestServiceURL(url);

        this.diagram.addSelectionEventHandler(this);
        this.diagram.addPathwayChangeEventHandler(this);
        this.diagram.addParticipatingMoleculeSelectionEventHandler(this);
        this.diagram.addSubpathwaySelectionEventHandler(this);
        this.diagram.addExpressionOverlayStopHandler(this);
        this.diagram.addZoomedOutEventHandler(this);

        this.diagramContainer = new DiagramContainer(this.diagram);
        this.diagramContainer.setFigureButtonsClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                DiagramButton button = (DiagramButton) event.getSource();
                if(button.getFigure()!=null) {
                    presenter.figureSelected(button.getFigure());
                }else{
                    presenter.showFireworks(button.getPathway().getDbId());
                }
            }
        });
        this.container.add(diagramContainer, "Diagram");
    }

    private void initializeFigurePanel(){
        this.figureContainer = new FigureContainer();
        this.figureContainer.setGoToDiagramClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                presenter.figureClosed();
                container.selectTab(0);
            }
        });
        this.container.add(this.figureContainer, "Figure");
    }

    @Override
    public Widget asWidget() {
        return this.container;
    }

    @Override
    public void clearSelection() {
        if(!selectedDBIds.isEmpty()){
            selectedDBIds.clear();
            this.diagram.clearSelection();
        }
    }

    @Override
    public void clearOverlays() {
        this.token = null;
        if(this.diagram!=null){
            this.diagram.clearOverlays();
        }
    }

    @Override
    public void loadPathway(Pathway pathway) {
        this.selectedDBIds.clear();
        this.container.selectTab(0);
        this.diagram.setPathway(pathway.getDbId());
    }

    @Override
    public void setAnalysisToken(final String token) {
        this.token = token;
        AnalysisHelper.chooseResource(token,new AnalysisHelper.ResourceChosenHandler() {
            @Override
            public void onResourceChosen(String resource) {
                diagram.showAnalysisData(token, resource);
            }
        });
    }

    @Override
    public void setAnalysisResource(String resource) {
        if(this.token!=null){
            this.diagram.showAnalysisData(this.token, resource);
        }
    }

    @Override
    public void onExpressionOverlayStopped(ExpressionOverlayStopEvent e) {
        this.token = null;
        presenter.resetAnalysisId();
    }

    @Override
    public void onPathwayChange(PathwayChangeEvent event) {
        //We keep the list of the objects ID in the diagram because is used in "setSelectionId" method
        //to differentiate when a object has to be unselected "quietly"
        objectsInDiagram.clear();
        if(this.diagram.getPathway()!=null){
            for (GraphObject graphObject : this.diagram.getPathway().getGraphObjects()) {
                objectsInDiagram.add(graphObject.getReactomeId());
            }

            Long currentPathwayId = event.getCurrentPathwayDBId();
            presenter.pathwayLoaded(currentPathwayId);
        }
    }

    @Override
    public void onSubpathwaySelection(SubpathwaySelectionEvent e) {
        //ToDo: Not used => remove?
        this.presenter.subpathwaySelected(e.getDiagramPathwayId(), e.getSubpathwayId());
    }

    @Override
    public void onPMSelectionChanged(ParticipatingMoleculeSelectionEvent participatingMoleculeSelectionEvent) {
        this.presenter.entitySelected(participatingMoleculeSelectionEvent.getSelectedParticipatingMoleculeId());
    }

    @Override
    public void onSelectionChanged(SelectionEvent selectionEvent) {
        List<Long> newSelection = selectionEvent.getSelectedDBIds();
        if(selectionHasChange(newSelection)){
            selectedDBIds = newSelection;
            if(selectedDBIds.size()==1){
                presenter.entitySelected(selectedDBIds.get(0));
            }else{
                presenter.entitiesSelected(selectedDBIds);
            }
        }
    }

    /**
     * Fired when the user zooms the diagram out enough to switch back to Fireworks
     */
    @Override
    public void onZoomedOut(ZoomedOutEvent zoomedOutEvent) {
        this.presenter.showFireworks(zoomedOutEvent.getPathwayId());
    }

    private boolean selectionHasChange(List<Long> selection){
        if(selection.size()!=selectedDBIds.size()) return true;
        for (Long dbId : selection) {
            if(!selectedDBIds.contains(dbId)) return true;
        }
        return false;
    }

    private boolean selectionHasChanged(Long dbId){
        return (selectedDBIds.size() != 1) || !selectedDBIds.contains(dbId);
    }

    @Override
    public void setInitialState() {
        this.selectedDBIds.clear();
        this.diagram.removeAll();
        this.container.selectTab(0);
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setFigures(Pathway pathway, DatabaseObject databaseObject) {
        this.diagramContainer.setSelectedObjects(pathway, databaseObject);
    }

    @Override
    public void setSelectionId(Long dbId) {
        if(selectionHasChanged(dbId)){
            selectedDBIds.clear();
            selectedDBIds.add(dbId);

            //This is done because selecting an ID that is not in the diagram
            //fires a onSelectionChanged event, and clear Selection does not
            if(objectsInDiagram.contains(dbId)){
                this.diagram.setSelectionId(dbId);
            }else{
                //In this case we want to clear the selection but do nothing after it :)
                clearSelection();
            }
        }
    }

    @Override
    public void setSelectionIds(List<Long> list){
        //This fixes a bug that mostly happens in gk_central when a pathway is supposed
        //to have diagram but it does NOT. Here we check if the loaded pathway, the first
        //parent containing diagram (since that is a condition somewhere else) contains
        //the "contained events" of the one that is now selected (with hasDiagram == false)
        List<Long> aux = new LinkedList<Long>();
        for (Long dbId : list) {
            if(objectsInDiagram.contains(dbId)){
                //We only keep in aux those that are contained, so we ensure we select
                //only those in the current loaded diagram. Now, the hack is been done
                //by adding the id of the diagram without diagram (supposed to have one)
                //in the presenter, so here all the other contained events will be
                //removed and the pathway without diagram will be the one selected :S
                aux.add(dbId);
            }
        }
        if(!aux.isEmpty()){
            if(selectionHasChange(aux)){
                selectedDBIds = aux;
                this.diagram.setSelectionIds(aux);
            }
        }else{
            //In this case we want to clear the selection but do nothing after it :)
            clearSelection();
        }
    }

    @Override
    public void showFigure(Figure figure) {
        this.figureContainer.setFigure(figure);
        this.container.selectTab(1);
    }
}