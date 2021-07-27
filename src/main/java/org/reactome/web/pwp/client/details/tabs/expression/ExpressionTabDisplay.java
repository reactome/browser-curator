package org.reactome.web.pwp.client.details.tabs.expression;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.pwp.client.common.CommonImages;
import org.reactome.web.pwp.client.common.model.classes.DatabaseObject;
import org.reactome.web.pwp.client.common.model.classes.Event;
import org.reactome.web.pwp.client.common.model.classes.Pathway;
import org.reactome.web.pwp.client.common.model.classes.ReferenceSequence;
import org.reactome.web.pwp.client.common.model.handlers.DatabaseObjectLoadedHandler;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.details.tabs.DetailsTabTitle;
import org.reactome.web.pwp.client.details.tabs.DetailsTabType;
import uk.ac.ebi.pwp.widgets.gxa.ui.GXAViewer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ExpressionTabDisplay extends ResizeComposite implements ExpressionTab.Display {

    private ExpressionTab.Presenter presenter;

    private DockLayoutPanel container;
    private DetailsTabTitle title;
    private Map<DatabaseObject, GXAViewer> cache = new HashMap<>();

    public ExpressionTabDisplay() {
        this.title = this.getDetailTabType().getTitle();
        this.container = new DockLayoutPanel(Style.Unit.EM);
        initWidget(this.container);
        setInitialState();
    }

    public DetailsTabType getDetailTabType() {
        return DetailsTabType.EXPRESSION;
    }

    @Override
    public Widget getTitleContainer() {
        return this.title;
    }

    @Override
    public void setInitialState() {
        this.container.clear();
        this.container.add(getDetailTabType().getInitialStatePanel());
    }

    @Override
    public void setPresenter(ExpressionTab.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showDetails(DatabaseObject databaseObject) {
        GXAViewer gxaViewer = new GXAViewer();
        cache.put(databaseObject, gxaViewer);
        String reactomeID = databaseObject.getStableIdentifier().getDisplayName().split("\\.")[0];
        gxaViewer.setReactomeID(reactomeID);

        this.container.clear();
        this.container.add(gxaViewer);
    }

    @Override
    public void showProteins(DatabaseObject databaseObject) {
        if (this.cache.containsKey(databaseObject)) {
            this.container.clear();
            this.container.add(this.cache.get(databaseObject));
        }else {
            this.showLoadingMessage();
            presenter.getReferenceSequences(databaseObject);
        }
    }

    @Override
    public void showReferenceSequences(DatabaseObject databaseObject, List<ReferenceSequence> referenceSequenceList) {
        GXAViewer gxaViewer = new GXAViewer();
        this.cache.put(databaseObject, gxaViewer);
        List<String> uniProtIDs = new LinkedList<>();
        for (ReferenceSequence referenceSequence : referenceSequenceList) {
            if (referenceSequence.getIdentifier() == null) continue;
            uniProtIDs.add(referenceSequence.getIdentifier());
        }
        if (uniProtIDs.isEmpty()) {
            gxaViewer.setEmpty();
        } else {
            gxaViewer.setUniProtIDs(uniProtIDs);
        }

        this.container.clear();
        this.container.add(gxaViewer);
    }

    @Override
    public void showEventWithDiagram(final Event eventWithDiagram) {
        if (this.cache.containsKey(eventWithDiagram)) {
            this.container.clear();
            this.container.add(this.cache.get(eventWithDiagram));
        }else {
            eventWithDiagram.load(new DatabaseObjectLoadedHandler() {
                @Override
                public void onDatabaseObjectLoaded(DatabaseObject databaseObject) {
                    showDetails(databaseObject);
                }

                @Override
                public void onDatabaseObjectError(Throwable trThrowable) {
                    Console.error(eventWithDiagram.getDisplayName() + " details could not be retrieved from the server.", ExpressionTabDisplay.this);
                }
            });
        }
    }

    @Override
    public void showLoadingMessage() {
        HorizontalPanel message = new HorizontalPanel();
        Image loader = new Image(CommonImages.INSTANCE.loader());
        message.add(loader);

        Label label = new Label("Loading the data required to show the gene expression data. Please wait...");
        label.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        message.add(label);

        this.container.clear();
        this.container.add(message);
    }

    @Override
    public void showErrorMessage(String message) {
        HorizontalPanel panel = new HorizontalPanel();
        Image loader = new Image(CommonImages.INSTANCE.exclamation());
        panel.add(loader);

        Label label = new Label(message);
        label.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        panel.add(label);

        this.container.clear();
        this.container.add(panel);
    }
}
