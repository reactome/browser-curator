package org.reactome.web.elv.client.details.tabs.processes.model.widgets.factory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.*;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public abstract class TableRowFactory {

    private static final boolean EMPTY_ROWS_AS_NA = !GWT.isScript();

    public static Widget getProcessesRow(String title, Widget... panelList){
        Widget cont;
        if(panelList==null || panelList.length==0){
            if(EMPTY_ROWS_AS_NA){
                cont = new HTMLPanel("N/A");
            }else{
                return null; //by returning null the ProcessesTable class will understand that nothing needs to be added
            }
        }else{
            cont = new VerticalPanel();
            for (Widget processesPanel : panelList) {
                ((VerticalPanel) cont).add(processesPanel);
            }
        }
        cont.setWidth("100%");

        FlexTable flexTable = new FlexTable();
        flexTable.setWidth("100%");
        flexTable.addStyleName("elv-Details-OverviewRow");
        flexTable.getColumnFormatter().setWidth(0, "200px");
//        flexTable.getFlexCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);

        HTMLPanel prop = new HTMLPanel(title);
//        prop.getElement().getStyle().setMarginTop(20, Style.Unit.PX);
        prop.addStyleName("elv-Details-OverviewProperty");

        flexTable.setWidget(0, 0, prop);
        flexTable.setWidget(0, 1, cont);

        return flexTable;
    }
}
