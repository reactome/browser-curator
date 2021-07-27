package org.reactome.web.pwp.client.viewport.diagram;

import org.reactome.web.pwp.client.common.AnalysisStatus;
import org.reactome.web.pwp.client.common.model.classes.CellLineagePath;
import org.reactome.web.pwp.client.common.model.classes.DatabaseObject;
import org.reactome.web.pwp.client.common.model.classes.Pathway;
import org.reactome.web.pwp.client.common.module.BrowserModule;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface Diagram {

    interface Presenter extends BrowserModule.Presenter {

        void databaseObjectSelected(Long dbId);

        void databaseObjectHovered(Long dbId);

        void diagramLoaded(Long dbId);

    }

    interface Display extends BrowserModule.Display {
        boolean isVisible();

        void loadPathway(Pathway pathway);

        void loadCellLineagePath(CellLineagePath cellLineagePath);

        void flag(String flag);

        void highlight(DatabaseObject databaseObject);

        void select(DatabaseObject databaseObject);

        void setAnalysisToken(AnalysisStatus analysisStatus);

        void setPresenter(Presenter presenter);

        void setVisible(boolean visible);
    }
}
