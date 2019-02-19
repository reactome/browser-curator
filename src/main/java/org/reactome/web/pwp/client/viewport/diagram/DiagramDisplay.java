package org.reactome.web.pwp.client.viewport.diagram;

import com.google.gwt.dom.client.Style;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import org.reactome.diagram.client.PathwayDiagramPanel;
import org.reactome.diagram.event.*;
import org.reactome.diagram.expression.event.ExpressionOverlayStopEvent;
import org.reactome.diagram.expression.event.ExpressionOverlayStopEventHandler;
import org.reactome.diagram.model.GraphObject;
import org.reactome.web.pwp.client.common.AnalysisStatus;
import org.reactome.web.pwp.client.common.model.classes.DatabaseObject;
import org.reactome.web.pwp.client.common.model.classes.Pathway;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DiagramDisplay extends DockLayoutPanel implements Diagram.Display,
        SelectionEventHandler, PathwayChangeEventHandler, ParticipatingMoleculeSelectionEventHandler,
        SubpathwaySelectionEventHandler, ExpressionOverlayStopEventHandler {

    private static final String RESTFUL_WS_FOLDER = "ReactomeRESTfulAPI/RESTfulWS/";

    private PathwayDiagramPanel diagram;

    private Diagram.Presenter presenter;

    private List<Long> selectedDBIds = new LinkedList<>();
    private List<Long> objectsInDiagram = new LinkedList<>();

    public DiagramDisplay() {
        super(Style.Unit.PX);
        //noinspection GWTStyleCheck
        this.addStyleName("elv-Diagram-Container");

        initializeDiagram();
    }

    private void initializeDiagram() {
        this.diagram = new PathwayDiagramPanel();
        //noinspection GWTStyleCheck
        this.diagram.addStyleName("elv-Diagram-Holder");
        this.diagram.setRestServiceURL("/" + RESTFUL_WS_FOLDER);

        this.diagram.addSelectionEventHandler(this);
        this.diagram.addPathwayChangeEventHandler(this);
        this.diagram.addParticipatingMoleculeSelectionEventHandler(this);
        this.diagram.addSubpathwaySelectionEventHandler(this);
        this.diagram.addExpressionOverlayStopHandler(this);

        this.add(diagram);
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void loadPathway(Pathway pathway) {
        this.selectedDBIds.clear();
        this.diagram.setPathway(pathway.getDbId());
    }

    @Override
    public void flag(String flag) {
        //Nothing here
    }

    @Override
    public void highlight(DatabaseObject databaseObject) {
        //Nothing here
    }

    @Override
    public void select(DatabaseObject databaseObject) {
        if (databaseObject == null) {
            clearSelection();
        } else if (databaseObject instanceof Pathway) {
            Pathway p = (Pathway) databaseObject;
            if (!p.getHasDiagram()) {
                selectContainedEventIds(p);
            }
        } else {
            Long dbId = databaseObject.getDbId();
            if (selectionHasChanged(dbId)) {
                selectedDBIds.clear();
                selectedDBIds.add(dbId);

                //This is done because selecting an ID that is not in the diagram
                //fires a onSelectionChanged event, and clear Selection does not
                if (objectsInDiagram.contains(dbId)) {
                    this.diagram.setSelectionId(dbId);
                } else {
                    //In this case we want to clear the selection but do nothing after it :)
                    clearSelection();
                }
            }
        }
    }

    @Override
    public void setAnalysisToken(AnalysisStatus analysisStatus) {
        //There is no analysis service in Curator
    }

    @Override
    public void setPresenter(Diagram.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onExpressionOverlayStopped(ExpressionOverlayStopEvent e) {
        //There is no analysis service in Curator
    }

    @Override
    public void onPathwayChange(PathwayChangeEvent event) {
        //We keep the list of the objects ID in the diagram because is used in "setSelectionId" method
        //to differentiate when a object has to be unselected "quietly"
        objectsInDiagram.clear();
        if (this.diagram.getPathway() != null) {
            for (GraphObject graphObject : this.diagram.getPathway().getGraphObjects()) {
                objectsInDiagram.add(graphObject.getReactomeId());
            }

            Long currentPathwayId = event.getCurrentPathwayDBId();
            presenter.diagramLoaded(currentPathwayId);
        }
    }

    @Override
    public void onSubpathwaySelection(SubpathwaySelectionEvent e) {
        //ToDo: Not used => remove?
        this.presenter.databaseObjectSelected(e.getSubpathwayId());
    }

    @Override
    public void onPMSelectionChanged(ParticipatingMoleculeSelectionEvent participatingMoleculeSelectionEvent) {
        this.presenter.databaseObjectSelected(participatingMoleculeSelectionEvent.getSelectedParticipatingMoleculeId());
    }

    @Override
    public void onSelectionChanged(SelectionEvent selectionEvent) {
        List<Long> newSelection = selectionEvent.getSelectedDBIds();
        if (selectionHasChange(newSelection)) {
            selectedDBIds = newSelection;
            if (selectedDBIds.isEmpty()) {
                presenter.databaseObjectSelected(null);
            } else if (selectedDBIds.size() == 1) {
                presenter.databaseObjectSelected(selectedDBIds.get(0));
            }
        }
    }

    private boolean selectionHasChange(List<Long> selection) {
        if (selection.size() != selectedDBIds.size()) return true;
        for (Long dbId : selection) {
            if (!selectedDBIds.contains(dbId)) return true;
        }
        return false;
    }

    private boolean selectionHasChanged(Long dbId) {
        return (selectedDBIds.size() != 1) || !selectedDBIds.contains(dbId);
    }

    public void clearSelection() {
        if (!selectedDBIds.isEmpty()) {
            selectedDBIds.clear();
            this.diagram.clearSelection();
        }
    }

    private void selectContainedEventIds(final Pathway pathway) {
        String url = "/ReactomeRESTfulAPI/RESTfulWS/getContainedEventIds/" + pathway.getDbId();
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    try {
                        String[] values = response.getText().split(",");
                        List<Long> list = new LinkedList<Long>();

                        //THIS IS A HACK: Used for gk_central when there are inconsistencies
                        list.add(pathway.getDbId()); //Will be removed in view.setSelectionIds if not needed there

                        for (String value : values) {
                            list.add(Long.valueOf(value));
                        }
                        if (!list.isEmpty()) {
                            diagram.setSelectionIds(list);
                        }
                    } catch (Exception ex) {
                        //TODO
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    //TODO
                }
            });
        } catch (RequestException ex) {
            //TODO
        }
    }
}
