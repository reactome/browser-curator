package org.reactome.web.elv.client.details.model.widgets;

import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.GO_BiologicalProcess;
import org.reactome.web.elv.client.common.widgets.disclosure.DisclosurePanelFactory;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class GO_BiologicalProcessPanel extends DetailsPanel implements OpenHandler<DisclosurePanel> {
    private GO_BiologicalProcess goBiologicalProcess;
    private DisclosurePanel disclosurePanel;

    public GO_BiologicalProcessPanel(GO_BiologicalProcess goBiologicalProcess) {
        this(null, goBiologicalProcess);
    }

    public GO_BiologicalProcessPanel(DetailsPanel parentPanel, GO_BiologicalProcess goBiologicalProcess) {
        super(parentPanel);
        this.goBiologicalProcess = goBiologicalProcess;
        initialize();
    }

    private void initialize(){
        disclosurePanel = DisclosurePanelFactory.getAdvancedDisclosurePanel(goBiologicalProcess.getDisplayName());
        disclosurePanel.addOpenHandler(this);
        initWidget(disclosurePanel);
    }

    @Override
    public DatabaseObject getDatabaseObject() {
        return this.goBiologicalProcess;
    }

    @Override
    public void onOpen(OpenEvent<DisclosurePanel> event) {
        if(!isLoaded())
            dataRequired(this.goBiologicalProcess);
    }

    @Override
    public void setReceivedData(DatabaseObject data) {
        this.goBiologicalProcess = (GO_BiologicalProcess) data;
        String accession = goBiologicalProcess.getAccession();

        FlexTable flexTable = new FlexTable();
        flexTable.setWidth("98%");
        flexTable.addStyleName("elv-Details-OverviewDisclosure-content");
        flexTable.getColumnFormatter().setWidth(0, "75px");

        flexTable.setWidget(0, 0, new Label("Accession"));
        Anchor link = new Anchor("GO:"+accession, "http://www.ebi.ac.uk/QuickGO/GTerm?id=GO:"+accession);
        link.setTarget("_blank");
        link.setTitle("Go to QuickGO for GO:" + accession);
        flexTable.setWidget(0, 1, link);

        flexTable.setWidget(1, 0, new Label("Definition"));
        flexTable.setWidget(1, 1, new Label(this.goBiologicalProcess.getDefinition()));

        this.disclosurePanel.setContent(flexTable);
        setLoaded(true);
    }
}
