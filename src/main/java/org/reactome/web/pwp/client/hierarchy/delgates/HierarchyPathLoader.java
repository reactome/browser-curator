package org.reactome.web.pwp.client.hierarchy.delgates;

import org.reactome.web.pwp.client.common.model.classes.CellLineagePath;
import org.reactome.web.pwp.client.common.model.classes.DatabaseObject;
import org.reactome.web.pwp.client.common.model.classes.Event;
import org.reactome.web.pwp.client.common.model.classes.Pathway;
import org.reactome.web.pwp.client.common.model.client.RESTFulClient;
import org.reactome.web.pwp.client.common.model.client.handlers.AncestorsCreatedHandler;
import org.reactome.web.pwp.client.common.model.util.Ancestors;
import org.reactome.web.pwp.client.common.model.util.Path;
import org.reactome.web.pwp.client.common.utils.Console;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class HierarchyPathLoader {

    public interface HierarchyPathLoaderHandler {
        void expandPathway(Path path, Pathway pathway);

        void expandCellLineagePath(Path path, CellLineagePath cellLineagePath);

        void onPathLoaded(Path path);
    }

    private Path pathToExpand;
    private int openingPath;

    private HierarchyPathLoaderHandler handler;

    public HierarchyPathLoader(HierarchyPathLoaderHandler handler) {
        this.handler = handler;
    }

    public void loadHierarchyEvent(final Path path, final Event event) {
        if (event == null){
            Console.error("Event cannot be null here", this);
        }else {
            RESTFulClient.getAncestors(event, new AncestorsCreatedHandler() {
                @Override
                public void onAncestorsLoaded(Ancestors ancestors) {
                    setAncestorsListToExpand(ancestors, path, event);
                }

                @Override
                public void onAncestorsError(Throwable exception) {
                    Console.error(exception.getMessage(), HierarchyPathLoader.this);
                }
            });
        }
    }

    public void expandPath() {
        Path path;
        if (this.pathToExpand != null) {
            path = this.pathToExpand.getSubPath(this.openingPath);
            if (this.openingPath < this.pathToExpand.size() - 1) {
                DatabaseObject next = this.pathToExpand.get(this.openingPath);
                this.openingPath += 1;
                if (next instanceof Pathway) {
                    this.handler.expandPathway(path, (Pathway) next);
                    return;
                } else if (next instanceof CellLineagePath) {
                    this.handler.expandCellLineagePath(path, (CellLineagePath) next);
                }
            }
        }else{
            path = null;
        }
        this.handler.onPathLoaded(path);
    }

    private void setAncestorsListToExpand(Ancestors ancestors, Path path, Event event) {
        this.pathToExpand = null;

        List<Path> candidatePaths;
        if (path==null || path.isEmpty()) {
            candidatePaths = ancestors.getPathsContaining(event);
        } else {
            candidatePaths = ancestors.getPathsContaining(path.asList());
        }

        if (!candidatePaths.isEmpty()) {
            this.pathToExpand = candidatePaths.get(0);
        } else {
            this.pathToExpand = new Path();
        }

        if (this.pathToExpand != null && !this.pathToExpand.isEmpty()) {
            this.openingPath = 0;
            this.expandPath();
        } else {
            this.handler.onPathLoaded(path);
        }
    }
}
