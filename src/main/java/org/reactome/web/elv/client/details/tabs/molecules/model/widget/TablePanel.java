package org.reactome.web.elv.client.details.tabs.molecules.model.widget;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import org.reactome.web.elv.client.common.widgets.disclosure.DisclosurePanelFactory;
import org.reactome.web.elv.client.details.model.widgets.TextPanel;
import org.reactome.web.elv.client.details.tabs.molecules.model.data.Result;
import org.reactome.web.elv.client.details.tabs.molecules.model.type.PropertyType;

/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
public class TablePanel extends Composite implements OpenHandler<DisclosurePanel> {
    private DisclosurePanel disclosurePanel;
    FlowPanel overview;
    TextPanel namePanel;

    private PropertyType propertyType;
    private Result result;
    private int size;
    private MoleculesTable moleculesTable;

    public TablePanel(PropertyType category, int size, Result result) {
        this.propertyType = category;
        this.size = size;
        this.result = result;

        overview = new FlowPanel();
        namePanel = new TextPanel(propertyType.getTitle() + " (" + result.numHighlight(propertyType) + "/" + this.size + ")");
        namePanel.setTitle("Show list of participating " + propertyType.getTitle());

        overview.insert(namePanel, 0);
        namePanel.asWidget().getElement().getStyle().setFloat(Style.Float.LEFT);
        namePanel.asWidget().getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        namePanel.asWidget().getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        overview.setHeight("99%");

//        Getting the quantity and total number of molecules in result
//        int quantity = size;
//        QuantityBar quantityBar = new QuantityBar(quantity, result.getNumberOfMolecules());
//
//        overview.insert(quantityBar, 1);
//        quantityBar.addStyleName("quantity-colourBarFrame");

        this.disclosurePanel = DisclosurePanelFactory.getAdvancedDisclosurePanel(overview, null);

        this.disclosurePanel.addOpenHandler(this);
        this.initWidget(this.disclosurePanel);
    }

    @Override
    public void onOpen(OpenEvent<DisclosurePanel> event) {
        moleculesTable = new MoleculesTable(result);
        switch (propertyType){
            case CHEMICAL_COMPOUNDS:
                moleculesTable.setMoleculesData(result.getSortedChemicals());
                break;
            case PROTEINS:
                moleculesTable.setMoleculesData(result.getSortedProteins());
                break;
            case SEQUENCES:
                moleculesTable.setMoleculesData(result.getSortedSequences());
                break;
            default:
                moleculesTable.setMoleculesData(result.getSortedOthers());
                break;
        }
        this.disclosurePanel.setContent(moleculesTable.asWidget());
    }

    public void update(int size, Result result){
        this.result = result;
        this.size = size;
        //this.disclosurePanel.setHeader(overview.asWidget());

        overview.clear();
        namePanel = new TextPanel(propertyType.getTitle() + " (" + result.numHighlight(propertyType) + "/" + this.size + ")");
        namePanel.setTitle("Show list of participating " + propertyType.getTitle());
        overview.insert(namePanel, 0);
        namePanel.asWidget().getElement().getStyle().setFloat(Style.Float.LEFT);
        namePanel.asWidget().getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        namePanel.asWidget().getElement().getStyle().setMarginLeft(5, Style.Unit.PX);

        if(this.disclosurePanel.isOpen()){
            switch (propertyType){
                case CHEMICAL_COMPOUNDS:
                    moleculesTable.setMoleculesData(result.getSortedChemicals());
                    break;
                case PROTEINS:
                    moleculesTable.setMoleculesData(result.getSortedProteins());
                    break;
                case SEQUENCES:
                    moleculesTable.setMoleculesData(result.getSortedSequences());
                    break;
                default:
                    moleculesTable.setMoleculesData(result.getSortedOthers());
                    break;
            }

            this.disclosurePanel.setContent(moleculesTable.asWidget());
        }
    }
}
