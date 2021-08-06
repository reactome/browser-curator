package org.reactome.web.pwp.client.details.common.widgets.panels;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.pwp.client.common.model.classes.*;

import java.util.List;

/**
 * @author Joel Weiser (joel.weiser@oicr.on.ca)
 *         Created 8/5/2021
 */
public class CellPanel extends DetailsPanel implements TransparentPanel {
    private Cell cell;

    public CellPanel(Cell cell) {
        this(null, cell);
    }

    public CellPanel(DetailsPanel parentPanel, Cell cell) {
        super(parentPanel);
        this.cell = cell;
        setLoaded(true);
        initialize();
    }

    private void initialize() {
        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("100%");
        vp.addStyleName("elv-Details-OverviewDisclosure-content");

        if (this.cell.getOrgan() != null) {
            final String anatomyPanelLabel = "Organ:";
            vp.add(getAnatomyPanel(anatomyPanelLabel, this.cell.getOrgan()));
        }

        if (this.cell.getTissue() != null) {
            final String anatomyPanelLabel = "Tissue:";
            vp.add(getAnatomyPanel(anatomyPanelLabel, this.cell.getTissue()));
        }

        if (this.cell.getTissueLayer() != null) {
            final String anatomyPanelLabel = "Tissue layer:";
            vp.add(getAnatomyPanel(anatomyPanelLabel, this.cell.getTissueLayer()));
        }

        if (this.cell.getCellTypes() != null && !this.cell.getCellTypes().isEmpty()) {
            vp.add(getCellTypePanel(this.cell.getCellTypes()));
        }

        if (this.cell.getProteinMarkers() != null && !this.cell.getProteinMarkers().isEmpty()) {
            final String markerType = "Protein";
            vp.add(getMarkersPanel(markerType, this.cell.getProteinMarkers()));
        }

        if (this.cell.getRnaMarkers() != null && !this.cell.getRnaMarkers().isEmpty()) {
            final String markerType = "RNA";
            vp.add(getMarkersPanel(markerType, this.cell.getRnaMarkers()));
        }

        if (this.cell.getMarkerReferences() != null && !this.cell.getMarkerReferences().isEmpty()) {
            vp.add(getMarkerReferencesPanel(this.cell.getMarkerReferences()));
        }

        initWidget(vp);
    }

    @Override
    public DatabaseObject getDatabaseObject() {
        return this.cell;
    }

    private Widget getAnatomyPanel(String label, Anatomy anatomy){
        VerticalPanel vp = createStyledVerticalPanel(label);
        //We associate our parentPanel as the parent of the next panel because this panel is a kind of auxiliary panel
        Widget pPanel = new ExternalOntologyPanel(this, anatomy);
        pPanel.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        vp.add(pPanel);

        return vp;
    }

    private Widget getCellTypePanel(List<CellType> cellTypes) {
        VerticalPanel vp = createStyledVerticalPanel("Cell types:");
        for (CellType cellType : cellTypes) {
            Widget mPanel = new ExternalOntologyPanel(this, cellType);
            mPanel.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
            vp.add(mPanel);
        }

        return vp;
    }

    private Widget getMarkersPanel(String markerType, List<EntityWithAccessionedSequence> markers){
        VerticalPanel vp = createStyledVerticalPanel(markerType + " markers:");
        for (EntityWithAccessionedSequence marker : markers) {
            Widget mPanel = new PhysicalEntityPanel(this, marker);
            mPanel.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
            vp.add(mPanel);
        }

        return vp;
    }

    private Widget getMarkerReferencesPanel(List<MarkerReference> markerReferences){
        VerticalPanel vp = createStyledVerticalPanel("Marker references:");
        for (MarkerReference markerReference : markerReferences) {
            Widget mPanel = new MarkerReferencePanel(this, markerReference);
            mPanel.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
            vp.add(mPanel);
        }

        return vp;
    }

    private VerticalPanel createStyledVerticalPanel(String panelLabelName) {
        VerticalPanel vp = new VerticalPanel();
        vp.addStyleName("elv-Details-OverviewDisclosure-content");
        vp.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);
        vp.setWidth("99%");
        vp.add(new Label(panelLabelName));

        return vp;
    }
}
