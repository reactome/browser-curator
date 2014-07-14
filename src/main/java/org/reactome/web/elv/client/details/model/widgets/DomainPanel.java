package org.reactome.web.elv.client.details.model.widgets;

import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Domain;
import org.reactome.web.elv.client.common.widgets.disclosure.DisclosurePanelFactory;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DomainPanel extends DetailsPanel implements OpenHandler<DisclosurePanel> {
    private DisclosurePanel disclosurePanel;
    private Domain domain;

    public DomainPanel(DetailsPanel parentPanel) {
        super(parentPanel);
    }

    public DomainPanel(DetailsPanel parentPanel, Domain domain) {
        super(parentPanel);
        this.domain = domain;
        initialize();
    }

    private void initialize(){
        String displayName = domain.getDisplayName();
        disclosurePanel = DisclosurePanelFactory.getDisclosurePanel(displayName);
        disclosurePanel.addOpenHandler(this);
        initWidget(disclosurePanel);
    }


    @Override
    public DatabaseObject getDatabaseObject() {
        return this.domain;
    }

    @Override
    public void onOpen(OpenEvent<DisclosurePanel> event) {
        if(!isLoaded())
            dataRequired(this.domain);
    }

    @Override
    public void setReceivedData(DatabaseObject data) {
        this.domain = (Domain) data;
        //TODO:...
    }
}
