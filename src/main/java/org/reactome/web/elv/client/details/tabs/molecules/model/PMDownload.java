package org.reactome.web.elv.client.details.tabs.molecules.model;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class PMDownload extends DockLayoutPanel{
    public interface ViewHandler extends ClickHandler{}
    public interface DownloadHandler extends ClickHandler{}

    List<PMToggleButton> typesList = new ArrayList<PMToggleButton>();
    List<PMToggleButton> fieldsList = new ArrayList<PMToggleButton>();
    List<PMToggleButton> formatList = new ArrayList<PMToggleButton>();

    Button view = new Button("View");
    Button download = new Button("Download");

    TextArea textArea;

    private FormPanel form;
    private Hidden formParams;

    public PMDownload(JSONObject json) {
        super(Style.Unit.PX);
        init(json);
    }

    private void init(JSONObject json){
        VerticalPanel vp = new VerticalPanel();
        vp.setSpacing(4);
        vp.setWidth("465px");

        HorizontalPanel moleculeButtons = new HorizontalPanel();
        JSONArray participatingMolecules = json.get("participatingMolecules").isArray();
        for(int i=0; i<participatingMolecules.size(); ++i){
            JSONObject mType = participatingMolecules.get(i).isObject();
            String name = mType.get("name").isString().stringValue();
            int number = mType.get("molecules").isArray().size();
            PMToggleButton btn = new PMToggleButton(name, number);
            btn.setDown(true);
            moleculeButtons.add(btn);
            typesList.add(btn);
        }
        CaptionPanel decTypesPanel = new CaptionPanel("Molecules Type (select as required)");
        decTypesPanel.add(moleculeButtons);
        vp.add(decTypesPanel);

        HorizontalPanel fieldsButtons = new HorizontalPanel();
        for (String name : FieldType.getListFields()) {
            PMToggleButton btn = new PMToggleButton(name);
            btn.setDown(true);
            fieldsList.add(btn);
            fieldsButtons.add(btn);
        }
        CaptionPanel decFieldsPanel = new CaptionPanel("Fields (select as required)");
        decFieldsPanel.add(fieldsButtons);
        vp.add(decFieldsPanel);

        HorizontalPanel formatsButtons = new HorizontalPanel();
        FormatButtonClickHandler handler = new FormatButtonClickHandler(formatList, view);
        for (String format : FormatType.getListFormats()) {
            PMToggleButton btn = new PMToggleButton(format, handler);
            formatList.add(btn);
            formatsButtons.add(btn);
        }
        formatList.get(0).setValue(true);
        CaptionPanel decFormatsPanel = new CaptionPanel("Formats (select one)");
        decFormatsPanel.add(formatsButtons);
        vp.add(decFormatsPanel);

        HorizontalPanel controlButtons = new HorizontalPanel();
        controlButtons.setSpacing(5);
        controlButtons.getElement().getStyle().setFloat(Style.Float.RIGHT);
        controlButtons.add(view);
        controlButtons.add(download);
        vp.add(controlButtons);


        //***********************************************************************************//
        // form and formParams is used to synchronously call the RESTFulAPI in order to get
        // the files as a response for the download button action
        // are added only once in the panel but used every time the user perform a download
        formParams = new Hidden("params");
        form = new FormPanel();
        form.setVisible(false);
        form.setEncoding(FormPanel.ENCODING_URLENCODED);
        form.setMethod(FormPanel.METHOD_POST);
        form.add(formParams);
        vp.add(form);
        //***********************************************************************************//

        DockLayoutPanel viewerPanel = new DockLayoutPanel(Style.Unit.PX);

        HorizontalPanel viewerButtons = new HorizontalPanel();
        viewerButtons.setSpacing(5);
        viewerButtons.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        viewerButtons.getElement().getStyle().setFloat(Style.Float.RIGHT);
        viewerButtons.add(new Button("Clear", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                textArea.setValue("");
            }
        }));
        viewerButtons.add(new Button("Select all", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                textArea.selectAll();
            }
        }));
        viewerPanel.addSouth(viewerButtons, 50);

        textArea = new TextArea();
        textArea.setReadOnly(true);
        textArea.getElement().getStyle().setMarginTop(10, Style.Unit.PX);
        textArea.getElement().getStyle().setMarginRight(10, Style.Unit.PX);
        textArea.setWidth("98%");
        textArea.setHeight("99%");
        viewerPanel.add(textArea);


        addWest(new ScrollPanel(vp), 475);
        add(viewerPanel);
    }

    public void addDownloadHandler(DownloadHandler downloadHandler) {
        if(downloadHandler!=null)
            download.addClickHandler(downloadHandler);
    }

    public void addViewHandler(ViewHandler viewHandler) {
        if(viewHandler!=null)
            view.addClickHandler(viewHandler);
    }

    public String getFormat(){
        String format = "";
        for (PMToggleButton btn : formatList) {
            if(btn.isDown()) return btn.getKey();
        }
        return format;
    }

    public List<String> getSelectedFields(){
        List<String> list = new ArrayList<String>();
        for (PMToggleButton btn : fieldsList) {
            if(btn.isDown()) list.add(btn.getKey());
        }
        return list;
    }

    public List<String> getSelectedTypes(){
        List<String> list = new ArrayList<String>();
        for (PMToggleButton btn : typesList) {
            if(btn.isDown()) list.add(btn.getKey());
        }
        return list;
    }

    public void showData(String data){
        textArea.setValue(data);
    }

    public void submitForm(String url, JSONObject params){
        formParams.setValue(params.toString());
        form.setAction(url);
        form.submit();
    }


    private class FormatButtonClickHandler implements ClickHandler {
        Button view;
        List<PMToggleButton> onlyOne;

        public FormatButtonClickHandler(List<PMToggleButton> onlyOne, Button view) {
            this.onlyOne = onlyOne;
            this.view = view;
        }

        @Override
        public void onClick(ClickEvent event) {
            if(onlyOne==null || onlyOne.isEmpty()) return;

            PMToggleButton btn = (PMToggleButton) event.getSource();
            if(!btn.isDown()){
                btn.setDown(true);
                event.stopPropagation();
            }else{
                for (PMToggleButton btnAux : onlyOne) {
                    if(!btnAux.equals(btn)){
                        btnAux.setValue(false);
                    }else{
                        view.setEnabled(FormatType.getFormatType(btn.getText()).isViewable());
                    }
                }
            }
        }
    }
}
