package org.reactome.web.elv.client.details.tabs.molecules.model;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.*;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class MoleculesPanel extends DockLayoutPanel implements ValueChangeHandler {

    private int moleculesNumber;

    ToggleButton btnList;

    ToggleButton btnDownload;
    private PMDownload download;

    StackLayoutPanel centerContainer;

    @SuppressWarnings("unchecked")
    public MoleculesPanel(JSONObject json) {
        super(Style.Unit.PX);

        JSONValue aux = json.get("moleculesNumber");
        moleculesNumber = aux.isNumber()!=null ? (int) aux.isNumber().doubleValue() : Integer.valueOf(aux.isString().stringValue());

        btnList = new ToggleButton("List");
        btnList.addValueChangeHandler(this);
        btnList.setDown(true);
        btnList.setEnabled(false);
        btnList.setWidth("150px");

        btnDownload = new ToggleButton(("Download"));
        btnDownload.addValueChangeHandler(this);
        btnDownload.setWidth("150px");

        HorizontalPanel panel = new HorizontalPanel();
        panel.setSpacing(15);
        panel.add(btnList);
        panel.add(btnDownload);
        addNorth(panel, 50);

        PMTree tree = new PMTree(json);
        download = new PMDownload(json);

        centerContainer = new StackLayoutPanel(Style.Unit.EM);
        centerContainer.add(new ScrollPanel(tree), "List", 0);
        centerContainer.add(download, "Download", 0);
        add(centerContainer);
    }

    public PMDownload getDownload() {
        return download;
    }

    public int getMoleculesNumber() {
        return moleculesNumber;
    }

    @Override
    public void onValueChange(ValueChangeEvent valueChangeEvent) {
        ToggleButton btn = (ToggleButton) valueChangeEvent.getSource();
        boolean value = btn.getValue();
        btnList.setEnabled(true); btnDownload.setEnabled(true);
        if(btn.equals(this.btnList)){
            btnList.setEnabled(false);
            btnDownload.setValue(!value, false);
            if(value) showList();
            else showDownload();
        }else if(btn.equals(this.btnDownload)){
            btnDownload.setEnabled(false);
            btnList.setValue(!value, false);
            if(value) showDownload();
            else showList();
        }
    }

    private void showDownload(){
        centerContainer.showWidget(1);
    }

    private void showList(){
        centerContainer.showWidget(0);
    }
}
