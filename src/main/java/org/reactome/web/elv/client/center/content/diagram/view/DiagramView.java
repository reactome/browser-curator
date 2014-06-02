package org.reactome.web.elv.client.center.content.diagram.view;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Figure;
import org.reactome.web.elv.client.common.data.model.Pathway;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface DiagramView {

    public interface Presenter {
        public void entitiesSelected(List<Long> selection);
        public void entitySelected(Long dbId);
        public void figureSelected(Figure figure);
        public void figureClosed();
        public void pathwayLoaded(Long dbId);
        public void resetAnalysisId();
        public void subpathwaySelected(Long pathwayId, Long subpathwayId);
    }

    Widget asWidget();
    void clearSelection();
    void clearOverlays();
    void loadPathway(Pathway pathway);
    void setAnalysisToken(String token);
    void setAnalysisResource(String resource);
    void setInitialState();
    void setPresenter(Presenter presenter);
    void setFigures(Pathway pathway, DatabaseObject databaseObject);
    void setSelectionId(Long dbId);
    void setSelectionIds(List<Long> list);
    void showFigure(Figure figure);

}
