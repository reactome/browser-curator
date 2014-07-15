package org.reactome.web.elv.client.details.model.widgets;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.elv.client.common.data.model.CatalystActivity;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.PhysicalEntity;
import org.reactome.web.elv.client.common.widgets.disclosure.DisclosurePanelFactory;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class CatalystActivityPanel extends DetailsPanel implements OpenHandler<DisclosurePanel>{
    private CatalystActivity catalystActivity;
    private DisclosurePanel disclosurePanel;

    public CatalystActivityPanel(CatalystActivity catalystActivity) {
        this(null, catalystActivity);
    }

    public CatalystActivityPanel(DetailsPanel parentPanel, CatalystActivity catalystActivity) {
        super(parentPanel);
        this.catalystActivity = catalystActivity;
        initialize();
    }

    private void initialize(){
        this.disclosurePanel = DisclosurePanelFactory.getAdvancedDisclosurePanel(this.catalystActivity.getDisplayName());
        this.disclosurePanel.addOpenHandler(this);
        initWidget(this.disclosurePanel);
    }

    @Override
    public DatabaseObject getDatabaseObject() {
        return this.catalystActivity;
    }

    @Override
    public void onOpen(OpenEvent<DisclosurePanel> event) {
        if(!isLoaded())
            dataRequired(this.catalystActivity);
    }

    @Override
    public void setReceivedData(DatabaseObject data) {
        this.catalystActivity = (CatalystActivity) data;

        VerticalPanel vp = new VerticalPanel();
        vp.addStyleName("elv-Details-OverviewDisclosure-content");
        vp.setWidth("99%");

        vp.add(new Label("Physical Entity:"));
        Widget pPanel = new PhysicalEntityPanel(this.catalystActivity.getPhysicalEntity());
        pPanel.setWidth("98%");
        pPanel.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        vp.add(pPanel);

        if(!catalystActivity.getActiveUnit().isEmpty()){
            vp.add(new Label("Active Unit:"));
            for (PhysicalEntity dbObject : catalystActivity.getActiveUnit()) {
                Widget caPanel = new PhysicalEntityPanel(dbObject);
                caPanel.setWidth("98%");
                caPanel.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
                vp.add(caPanel);
            }
        }

        vp.add(new Label("Represents GO Molecular Function:"));
        Widget gPanel = new GO_MolecularFunctionPanel(this, this.catalystActivity.getActivity());
        gPanel.setWidth("98%");
        gPanel.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        vp.add(gPanel);

        this.disclosurePanel.setContent(vp);
        setLoaded(true);
    }
}
