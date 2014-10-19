package org.reactome.web.elv.client.hierarchy.view;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.elv.client.common.analysis.model.PathwaySummary;
import org.reactome.web.elv.client.common.data.model.Event;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.common.data.model.Species;
import org.reactome.web.elv.client.common.model.Path;
import org.reactome.web.elv.client.manager.messages.MessageObject;

import java.util.List;
import java.util.Set;

public interface HierarchyView {

	public interface Presenter {
        void eventHovered(Path path, Pathway pathway, Event event);
        void eventHoveredReset();
        void eventSelected(Path path, Pathway pathway, Event event);
        void eventChildrenRequired(Path path, Event event);
        void frontPageItemsRequired(Species species);
        void getAnalysisData(Set<Long> eventIds, Set<Long> pathwaysWithReactions);
        void hierarchyReady();
        void pathwayExpanded(Pathway pathway);
        void errorMsg(MessageObject msgObj);
	}

    Widget asWidget();
    void clearAnalysisResult();
    void expandPathway(Path path, Pathway pathway) throws Exception;
    Set<Long> getContainedEventIds();
    Set<Long> getHierarchyPathwaysWithReactionsLoaded();
    void highlightPath(Path path, Pathway pathway, Event event) throws Exception;
    void highlightHitReactions(Set<Long> reactionsHit);
    void loadItemChildren(Species species, Path path, Pathway pathway, List<Event> children) throws Exception;
    void showHierarchyForSpecies(Species species);
    void showAnalysisResult(List<PathwaySummary> pathwaySummaries);
    void setInitialState();
	void setPresenter(Presenter presenter);
    void tourFadeIn();
    void tourFadeOut();
}
