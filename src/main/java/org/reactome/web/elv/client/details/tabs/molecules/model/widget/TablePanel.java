package org.reactome.web.elv.client.details.tabs.molecules.model.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.elv.client.common.utils.Console;
import org.reactome.web.elv.client.common.widgets.disclosure.DisclosureImages;
import org.reactome.web.elv.client.common.widgets.disclosure.DisclosurePanelFactory;
import org.reactome.web.elv.client.details.tabs.molecules.model.data.Result;
import org.reactome.web.elv.client.details.tabs.molecules.model.type.PropertyType;

/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
class TablePanel extends Composite implements OpenHandler<DisclosurePanel>, CloseHandler<DisclosurePanel>{
    private final DisclosurePanel disclosurePanel;
    private final PropertyType propertyType;
    private Result result;
    private int size;
    private MoleculesTable moleculesTable;
    private String displayText;

    public TablePanel(PropertyType category, int size, Result result) {
        this.propertyType = category;
        this.size = size;
        this.result = result;
        this.moleculesTable = null;

        int toHighlight = result.getNumHighlight(propertyType);
        if(toHighlight == this.size){
            displayText = propertyType.getTitle() + " (" + this.size + ")"; // size/size will be replaced by size only
        }else{
            displayText = propertyType.getTitle() + " (" + result.getNumHighlight(propertyType) + "/" + this.size + ")";
        }

        this.disclosurePanel = DisclosurePanelFactory.getAdvancedDisclosurePanel(displayText, null);

        //set loadingMessage
        disclosurePanel.setContent(getLoadingMessage());
        disclosurePanel.setAnimationEnabled(true);

        //set all the handlers
        this.disclosurePanel.addOpenHandler(this);
        this.disclosurePanel.addCloseHandler(this);
        this.initWidget(this.disclosurePanel);
    }

    /**
     * If disclosurePanel is opened for the first time the data is set,
     * for any following openEvent the data will be updated instead.
     * @param event OpenEvent
     */
    @Override
    public void onOpen(OpenEvent<DisclosurePanel> event) {
        disclosurePanel.setContent(getLoadingMessage());
        disclosurePanel.setAnimationEnabled(true);
        GWT.runAsync(new RunAsyncCallback() {
            public void onFailure(Throwable caught) {
                Console.warn("Ups, something went wrong in Molecules Tab. Take a look at TablePanel > onOpen.");
            }

            public void onSuccess() {
                if (moleculesTable == null) {//loading/opening the first time
                    moleculesTable = new MoleculesTable(result);
                    moleculesTable.setMoleculesData(result.getSorted(propertyType));
                } else {
                    moleculesTable.updateMoleculesData(result.getSorted(propertyType));
                }
                disclosurePanel.setContent(moleculesTable.asWidget());
            }
        });
    }

    /**
     * If disclosurePanel is closed, first all the content is cleared away,
     * otherwise the content will be displayed until the panel is closed (which is not the right behaviour)!
     * @param event CloseEvent
     */
    @Override
    public void onClose(CloseEvent<DisclosurePanel> event) {
        this.disclosurePanel.clear();
    }

    /**
     * Updating the result, the data of the disclosurePanel and the text in disclosureHeader.
     * @param size new number of molecules of one category
     * @param result updated version of result.
     */
    public void update(int size, final Result result){
        this.result = result;
        this.size = size;

        if(this.disclosurePanel.isOpen()){
            disclosurePanel.setContent(getLoadingMessage());
            disclosurePanel.setAnimationEnabled(true);
            GWT.runAsync(new RunAsyncCallback() {
                public void onFailure(Throwable caught) {
                    Console.warn("Ups, something went wrong in Molecules Tab. Take a look at TablePanel > update.");
                }

                public void onSuccess() {
                    moleculesTable.updateMoleculesData(result.getSorted(propertyType));
                    disclosurePanel.setContent(moleculesTable.asWidget());
                }
            });
        }

        int toHighlight = result.getNumHighlight(propertyType);
        if(toHighlight == this.size){
            displayText = propertyType.getTitle() + " (" + this.size + ")";
        }else{
            displayText = propertyType.getTitle() + " (" + result.getNumHighlight(propertyType) + "/" + this.size + ")";
        }
        this.disclosurePanel.getHeaderTextAccessor().setText(displayText);
    }

    /**
     * Setting the Molecules Data.
     */
//    private void setMoleculesData(){
//        switch (propertyType){
//            case CHEMICAL_COMPOUNDS:
//                MoleculesDataListener.getMoleculesDataListener().setMoleculesData(result.getSortedChemicals());
//                //moleculesTable.setMoleculesData(result.getSortedChemicals());
//                break;
//            case PROTEINS:
//                MoleculesDataListener.getMoleculesDataListener().setMoleculesData(result.getSortedProteins());
//                //moleculesTable.setMoleculesData(result.getSortedProteins());
//                break;
//            case SEQUENCES:
//                MoleculesDataListener.getMoleculesDataListener().setMoleculesData(result.getSortedSequences());
//                //moleculesTable.setMoleculesData(result.getSortedSequences());
//                break;
//            default:
//                MoleculesDataListener.getMoleculesDataListener().setMoleculesData(result.getSortedOthers());
//                //moleculesTable.setMoleculesData(result.getSortedOthers());
//                break;
//        }
//        this.disclosurePanel.setContent(moleculesTable.asWidget());
//    }
//
//    /**
//     * Updating the Molecules Data.
//     */
//    private void updateMoleculesData(){
//        switch (propertyType){
//            case CHEMICAL_COMPOUNDS:
//                moleculesTable.updateMoleculesData(result.getSortedChemicals());
//                break;
//            case PROTEINS:
//                moleculesTable.updateMoleculesData(result.getSortedProteins());
//                break;
//            case SEQUENCES:
//                moleculesTable.updateMoleculesData(result.getSortedSequences());
//                break;
//            default:
//                moleculesTable.updateMoleculesData(result.getSortedOthers());
//                break;
//        }
//        this.disclosurePanel.setContent(moleculesTable.asWidget());
//    }

    /**
     * Getting a panel with loading message and symbol.
     * @return Widget
     */
    private static Widget getLoadingMessage(){
        HorizontalPanel hp = new HorizontalPanel();
        hp.add(new Image(DisclosureImages.INSTANCE.getLoadingImage()));
        hp.add(new HTMLPanel("Loading molecules..."));
        hp.setSpacing(5);

        return hp;
    }
}
