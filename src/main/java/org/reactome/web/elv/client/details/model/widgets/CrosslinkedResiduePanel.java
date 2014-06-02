package org.reactome.web.elv.client.details.model.widgets;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import org.reactome.web.elv.client.common.data.model.CrosslinkedResidue;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.widgets.disclosure.DisclosurePanelFactory;


/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class CrosslinkedResiduePanel extends DetailsPanel {
    private CrosslinkedResidue crosslinkedResidue;
    private HorizontalPanel contentPanel;

    @SuppressWarnings("UnusedDeclaration")
    public CrosslinkedResiduePanel(CrosslinkedResidue crosslinkedResidue) {
        this(null, crosslinkedResidue);
    }

    public CrosslinkedResiduePanel(DetailsPanel parentPanel, CrosslinkedResidue crosslinkedResidue) {
        super(parentPanel);
        this.crosslinkedResidue = crosslinkedResidue;
        initialize();
    }

    private void initialize(){
        this.contentPanel = new HorizontalPanel();
        this.contentPanel.add(DisclosurePanelFactory.getLoadingMessage());
        this.contentPanel.setWidth("99%");
        initWidget(this.contentPanel);
        dataRequired(this.crosslinkedResidue);
    }

    @Override
    public void setReceivedData(DatabaseObject data) {
        this.crosslinkedResidue = (CrosslinkedResidue) data;

        FlexTable flexTable = new FlexTable();
        flexTable.setWidth("98%");
        flexTable.getColumnFormatter().setWidth(0, "75px");
        flexTable.setCellPadding(0);
        flexTable.setCellSpacing(0);

        flexTable.setWidget(0, 0, new Label("Second Coordinate"));
        flexTable.setWidget(0, 1, new Label(this.crosslinkedResidue.getSecondCoordinate().toString()));

        this.contentPanel.clear();
        this.contentPanel.add(flexTable);

        setLoaded(true);
    }

    @Override
    public DatabaseObject getDatabaseObject() {
        return this.crosslinkedResidue;
    }
}
