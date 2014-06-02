package org.reactome.web.elv.client.details.model.widgets;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.InstanceEdit;
import org.reactome.web.elv.client.common.data.model.Person;
import org.reactome.web.elv.client.common.widgets.disclosure.DisclosurePanelFactory;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class InstanceEditPanel extends DetailsPanel implements OpenHandler<DisclosurePanel> {
    private InstanceEdit instanceEdit;
    private DisclosurePanel disclosurePanel;

    public InstanceEditPanel(InstanceEdit instanceEdit) {
        this(null, instanceEdit);
    }

    public InstanceEditPanel(DetailsPanel parentPanel, InstanceEdit instanceEdit) {
        super(parentPanel);
        this.instanceEdit = instanceEdit;
        initialize();
    }

    private void initialize(){
        disclosurePanel = DisclosurePanelFactory.getAdvancedDisclosurePanel(instanceEdit.getDisplayName());
        disclosurePanel.addOpenHandler(this);
        initWidget(disclosurePanel);
    }

    @Override
    public void onOpen(OpenEvent<DisclosurePanel> event) {
        if(!isLoaded())
            dataRequired(this.instanceEdit);
    }

    @Override
    public DatabaseObject getDatabaseObject() {
        return instanceEdit;
    }

    @Override
    public void setReceivedData(DatabaseObject data) {
        setLoaded(true);
        InstanceEdit instanceEdit = (InstanceEdit) data;
        VerticalPanel vp = new VerticalPanel();
        vp.addStyleName("elv-Details-OverviewDisclosure-content");
        vp.setWidth("100%");
        vp.getElement().getStyle().setPaddingRight(5, Style.Unit.PX);
        for (Person person : instanceEdit.getAuthor()) {
            vp.add(new PersonPanel(this, person));
        }
        disclosurePanel.setContent(vp);
    }

}
