package org.reactome.web.pwp.client.details.common.widgets.panels;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.pwp.client.common.model.classes.*;
import org.reactome.web.pwp.client.common.model.handlers.DatabaseObjectLoadedHandler;
import org.reactome.web.pwp.client.details.common.widgets.disclosure.DisclosureHeader;
import org.reactome.web.pwp.client.details.common.widgets.disclosure.DisclosurePanelFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Joel Weiser (joel.weiser@oicr.on.ca)
 *         Created 8/4/2021
 */
public class MarkerReferencePanel extends DetailsPanel implements OpenHandler<DisclosurePanel> {
    private MarkerReference markerReference;
    private DisclosurePanel disclosurePanel;

    public MarkerReferencePanel(MarkerReference markerReference) {
        this(null, markerReference);
    }

    public MarkerReferencePanel(DetailsPanel parentPanel, MarkerReference markerReference) {
        super(parentPanel);
        this.markerReference = markerReference;
        this.initialize();
    }

    private void initialize(){
        createDisclosurePanel("Loading reference marker name(s)...");
        this.markerReference.load(new DatabaseObjectLoadedHandler() {
            @Override
            public void onDatabaseObjectLoaded(DatabaseObject databaseObject) {
                setDisclosurePanelHeaderText(joinMarkerNamesWithCommas() + " marker references");
                setReceivedData(databaseObject);
            }

            @Override
            public void onDatabaseObjectError(Throwable throwable) {
                setDisclosurePanelHeaderText(
                    "Marker reference " + MarkerReferencePanel.this.markerReference.getDbId() + " could not be loaded"
                );
                disclosurePanel.setContent(getErrorMessage());
            }
        });
    }

    private void createDisclosurePanel(String title) {
        this.disclosurePanel = DisclosurePanelFactory.getAdvancedDisclosurePanel(title);
        this.disclosurePanel.addOpenHandler(this);
        initWidget(this.disclosurePanel);
    }

    @Override
    public DatabaseObject getDatabaseObject() {
        return this.markerReference;
    }

    @Override
    public void onOpen(OpenEvent<DisclosurePanel> openEvent) {
        if(!isLoaded()) {
            this.markerReference.load(new DatabaseObjectLoadedHandler() {
                @Override
                public void onDatabaseObjectLoaded(DatabaseObject databaseObject) {
                    setReceivedData(databaseObject);
                }

                @Override
                public void onDatabaseObjectError(Throwable trThrowable) {
                    disclosurePanel.setContent(getErrorMessage());
                }
            });
        }
    }

    public void setReceivedData(DatabaseObject data) {
        this.markerReference = (MarkerReference) data;

        VerticalPanel vp = new VerticalPanel();
        vp.addStyleName("elv-Details-OverviewDisclosure-content");
        vp.setWidth("98%");

        vp.add(getMarkerPanel(this.markerReference.getMarkers()));
        vp.add(getLiteratureReferencePanel(this.markerReference.getLiteratureReferences()));

        this.disclosurePanel.setContent(vp);
        setLoaded(true);
    }

    private Widget getMarkerPanel(List<EntityWithAccessionedSequence> markers){
        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("100%");
        vp.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);

        String labelText = markers.size() == 1 ? "Marker:" : "Markers:";
        Label title = new Label(labelText);
        Style titleStyle = title.getElement().getStyle();
        titleStyle.setFontWeight(Style.FontWeight.BOLD);
        titleStyle.setMarginRight(5, Style.Unit.PX);
        vp.add(title);

        for (EntityWithAccessionedSequence marker : markers) {
            DetailsPanel p = new PhysicalEntityPanel(this, marker);
            p.setWidth("99%");
            p.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
            vp.add(p);
        }

        return vp;
    }

    private Widget getLiteratureReferencePanel(List<Publication> literatureReferences){
        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("100%");
        vp.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);

        String labelText = literatureReferences.size() == 1 ? "Literature Reference:" : "Literature References:";
        Label title = new Label(labelText);
        Style titleStyle = title.getElement().getStyle();
        titleStyle.setFontWeight(Style.FontWeight.BOLD);
        titleStyle.setMarginRight(5, Style.Unit.PX);
        vp.add(title);

        for (Publication literatureReference : literatureReferences) {
            DetailsPanel p = new PublicationPanel(this, literatureReference);
            p.setWidth("99%");
            p.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
            vp.add(p);
        }

        return vp;
    }

    private String joinMarkerNamesWithCommas() {
        return this.markerReference.getMarkers()
            .stream()
            .map(DatabaseObject::getDisplayName)
            .collect(Collectors.joining(","));
    }

    private void setDisclosurePanelHeaderText(String disclosurePanelHeaderText) {
        ((DisclosureHeader) this.disclosurePanel.getHeader()).setText(disclosurePanelHeaderText);
    }
}
