package org.reactome.web.elv.client.details.model.widgets;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Regulation;
import org.reactome.web.elv.client.common.widgets.disclosure.DisclosurePanelFactory;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class RegulationPanel extends DetailsPanel implements OpenHandler<DisclosurePanel> {
    private Regulation regulation;
    private DisclosurePanel disclosurePanel;

    public RegulationPanel(Regulation regulation) {
        this(null, regulation);
    }

    public RegulationPanel(DetailsPanel parentPanel, Regulation regulation) {
        super(parentPanel);
        this.regulation = regulation;
        initialize();
    }

    private void initialize(){
        String displayName = this.regulation.getDisplayName();
        this.disclosurePanel = DisclosurePanelFactory.getAdvancedDisclosurePanel(displayName);
        this.disclosurePanel.addOpenHandler(this);
        initWidget(this.disclosurePanel);
    }

    @Override
    public DatabaseObject getDatabaseObject() {
        return regulation;
    }

    @Override
    public void onOpen(OpenEvent<DisclosurePanel> event) {
        if(!isLoaded())
            dataRequired(this.regulation);
    }

    @Override
    public void setReceivedData(DatabaseObject data) {
        this.regulation = (Regulation) data;

        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("98%");
        vp.addStyleName("elv-Details-OverviewDisclosure-content");

        vp.add(getNamesPanel(this.regulation.getName()));

        this.disclosurePanel.setContent(vp);
        setLoaded(true);
    }

    private Widget getNamesPanel(List<String> list){
        HorizontalPanel hp = new HorizontalPanel();
        hp.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);

        Label title = new Label("Names:");
        Style titleStyle = title.getElement().getStyle();
        titleStyle.setFontWeight(Style.FontWeight.BOLD);
        titleStyle.setMarginRight(5, Style.Unit.PX);
        hp.add(title);

        StringBuilder names = new StringBuilder();
        for (String name : list) {
            names.append(name);
            names.append(", ");
        }
        names.delete(names.length()-2, names.length()-1);
        hp.add(new Label(names.toString()));

        return hp;
    }
}
