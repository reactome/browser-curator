package org.reactome.web.elv.client.details.model.widgets;

import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.PsiMod;
import org.reactome.web.elv.client.common.widgets.disclosure.DisclosurePanelFactory;


/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class PsiModPanel extends DetailsPanel implements OpenHandler<DisclosurePanel> {
    private PsiMod psiMod;
    private DisclosurePanel disclosurePanel;

    @SuppressWarnings("UnusedDeclaration")
    public PsiModPanel(PsiMod psiMod) {
        this(null, psiMod);
    }

    public PsiModPanel(DetailsPanel parentPanel, PsiMod psiMod) {
        super(parentPanel);
        this.psiMod = psiMod;
        initialize();
    }

    private void initialize(){
        this.disclosurePanel = DisclosurePanelFactory.getAdvancedDisclosurePanel(this.psiMod.getDisplayName());
        this.disclosurePanel.addOpenHandler(this);
        initWidget(this.disclosurePanel);
    }

    @Override
    public DatabaseObject getDatabaseObject() {
        return this.psiMod;
    }

    @Override
    public void onOpen(OpenEvent<DisclosurePanel> event) {
        if(!isLoaded())
            dataRequired(this.psiMod);
    }

    @Override
    public void setReceivedData(DatabaseObject data) {
        this.psiMod = (PsiMod) data;
        StringBuilder sb = new StringBuilder();
        for (String s : this.psiMod.getName()) {
            sb.append(s);
            sb.append(" ");
        }

        FlexTable flexTable = new FlexTable();
        flexTable.addStyleName("elv-Details-OverviewDisclosure-content");
        flexTable.setWidth("98%");
        flexTable.getColumnFormatter().setWidth(0, "75px");
        flexTable.setWidget(0, 0, new Label("Name"));
        flexTable.setWidget(0, 1, new Label(sb.toString().trim()));

        flexTable.setWidget(1, 0, new Label("Definition"));
        flexTable.setWidget(1, 1, new Label(this.psiMod.getDefinition()));

        this.disclosurePanel.setContent(flexTable);

        setLoaded(true);
    }
}
