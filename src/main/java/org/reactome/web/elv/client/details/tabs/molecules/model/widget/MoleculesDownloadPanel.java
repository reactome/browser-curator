package org.reactome.web.elv.client.details.tabs.molecules.model.widget;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.elv.client.details.model.widgets.TextPanel;
import org.reactome.web.elv.client.details.tabs.molecules.model.data.Molecule;
import org.reactome.web.elv.client.details.tabs.molecules.model.data.Result;
import org.reactome.web.elv.client.details.tabs.molecules.view.MoleculesView;

/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
public class MoleculesDownloadPanel extends DockLayoutPanel {
    private Result result;
    private CheckBox typeTB;
    private CheckBox nameTB;
    private CheckBox identifierTB;
    private CheckBox chemTB;
    private CheckBox protTB;
    private CheckBox sequTB;
    private CheckBox otheTB;
    //private ArrayList<ToggleButton> fieldList;
    //private ArrayList<ToggleButton> typesList;
    private TextArea textArea;

    private Button startDownloadBtn = new Button("Start Download");
    private MoleculesView.Presenter presenter;

    public MoleculesDownloadPanel(Result result, MoleculesView.Presenter presenter) {
        super(Style.Unit.PX);
        this.result = result;
        this.presenter = presenter;
        this.setWidth("99%");
        this.textArea = new TextArea();
    }

    public void initialise(final Result result){
        //fieldList = new ArrayList<ToggleButton>();
        //typesList = new ArrayList<ToggleButton>();
        this.clear(); //if not cleared then updated panels are added under old ones

        TextPanel information = new TextPanel(" Here you can download the information the Molecules Tab provides.\n" +
                "The format will be TSV which can easily be handled with most word processors.");
        information.setStyleName("elv-InformationPanel-Download");
        this.addNorth(information, 25);

        //Creating ToggleButton for each possible category of molecules.
        VerticalPanel requiredType = new VerticalPanel();
        requiredType.add(new TextPanel("Please select the type of Molecules you are interested in:"));

        chemTB = new CheckBox("Chemical Compounds");
        chemTB.setTitle("Show or hide chemicals");
        protTB = new CheckBox("Proteins");
        protTB.setTitle("Show or hide proteins");
        sequTB = new CheckBox("Sequences");
        sequTB.setTitle("Show or hide sequences");
        otheTB = new CheckBox("Others");
        otheTB.setTitle("Show or hide others");

        //Allow immediate changes to Preview by adding ClickHandler to every ToggleButton.
        ClickHandler updateText = new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                textArea.setText(resultToText());
                textArea.setStyleName("elv-PreviewPanel-Download");
            }
        };

        //Checking for each category of molecules if they are present before adding Handler.
        if(result.getChemicals().size() > 0){
            chemTB.setValue(true);
            requiredType.add(chemTB);
            chemTB.addClickHandler(updateText);
            //this.typesList.add(chemTB);
        }

        if(result.getProteins().size() > 0){
            protTB.setValue(true);
            requiredType.add(protTB);
            protTB.addClickHandler(updateText);
            //this.typesList.add(protTB);
        }

        if(result.getSequences().size() > 0){
            sequTB.setValue(true);
            requiredType.add(sequTB);
            sequTB.addClickHandler(updateText);
            //this.typesList.add(sequTB);
        }

        if(result.getOthers().size() > 0){
            otheTB.setValue(true);
            requiredType.add(otheTB);
            otheTB.addClickHandler(updateText);
            //this.typesList.add(otheTB);
        }

        requiredType.setStyleName("elv-SelectionPanels-Download");


        //Creating ToggleButton for each available attribute of molecules and adding Handler.
        VerticalPanel requiredFields = new VerticalPanel();
        requiredFields.add(new TextPanel("Please select the fields you are interested in:"));
        typeTB = new CheckBox("Type");
        typeTB.setTitle("Show or hide type column");
        typeTB.setValue(true);
        typeTB.addClickHandler(updateText);
        requiredFields.add(typeTB);

        identifierTB = new CheckBox("Identifier");
        identifierTB.setTitle("Show or hide identifier column");
        identifierTB.setValue(true);
        identifierTB.addClickHandler(updateText);
        requiredFields.add(identifierTB);

        nameTB = new CheckBox("Name");
        nameTB.setTitle("Show or hide name column");
        nameTB.setValue(true);
        nameTB.addClickHandler(updateText);
        requiredFields.add(nameTB);

        //this.fieldList.add(typeTB);
        //this.fieldList.add(nameTB);

        requiredFields.setStyleName("elv-SelectionPanels-Download");

        //Creating button for download.
        //Browsers that fully support Blob/Download:
        //Chrome, Chrome for Android, Firefox 20+, IE 10+, Opera 15+, Safari 6.1+
        VerticalPanel buttonField = new VerticalPanel();
        buttonField.add(startDownloadBtn);
        startDownloadBtn.getElement().getStyle().setFloat(Style.Float.RIGHT);
        buttonField.setStyleName("elv-ButtonPanel-Download");
        startDownloadBtn.setTitle("Depending on your browser you can either download your file by clicking on this button" +
                                  " or your will be redirected to a new tab in your browser where you can right click and" +
                                  " save the data.");
        startDownloadBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if((chemTB.getValue() || protTB.getValue() || sequTB.getValue() || otheTB.getValue())
                        && (typeTB.getValue() || nameTB.getValue() || identifierTB.getValue())){
                    alertDownload(textArea.getText());
                }else{
                    Window.alert("You are trying to download an empty file.\n" +
                            "Please select at least one type of molecules AND one field for the download.");
                }
                presenter.moleculeDownloadStarted();
            }
        });

        //Bringing together the two panels.
        FlowPanel controlArea = new FlowPanel();
        controlArea.insert(requiredType.asWidget(), 0);
        controlArea.insert(requiredFields.asWidget(), 1);
        ScrollPanel scrollPanel = new ScrollPanel(controlArea);

        this.addWest(scrollPanel, 200);
        this.addSouth(buttonField, 35);

        //Preview
        this.textArea = new TextArea();
        this.textArea.setVisible(true);
        this.textArea.setTitle("Preview of what your download file will look like.");
        this.textArea.setText(resultToText());
        this.add(textArea);
        this.textArea.setStyleName("elv-PreviewPanel-Download");

        this.addStyleName("elv-Details-OverviewPanel");
    }

    public static native void alertDownload(String text) /*-{
        $wnd.saveAs(
            new Blob(
                [text]
                , {type: "text/plain;charset=utf-8;"}
            )
            , "participatingMolecules.tsv"
        );
    }-*/;

    /**
     * Converting ResultObject into text for preview according to buttons pressed.
     * @return String for preview
     */
    private String resultToText() {
        String resultString = "";

        //Adding column names:
        if(typeTB != null && typeTB.getValue()){
            resultString += "MoleculeType\t";
        }

        if(nameTB != null && identifierTB.getValue()){
            resultString += "Identifier\t";
        }

        if(nameTB != null && nameTB.getValue()){
            resultString += "MoleculeName\t";
        }

        //Adding line break in case there are column names
        if(resultString.length() > 0){
            resultString += "\n";
        }

        if(chemTB != null && this.chemTB.getValue()){
            for(Molecule m : result.getChemicals()){
                if(m.isToHighlight()){
                    if(this.typeTB.getValue()){
                        resultString += "Chemical\t";
                    }

                    if(this.identifierTB.getValue()){
                        resultString += m.getIdentifier() + "\t";
                    }

                    if(this.nameTB.getValue()){
                        resultString += m.getDisplayName() + "\t";
                    }

                    resultString += "\n";
                }
            }
        }

        if(protTB != null && this.protTB.getValue()){
            for(Molecule m : result.getProteins()){
                if(m.isToHighlight()){
                    if(this.typeTB.getValue()){
                        resultString += "Protein\t";
                    }

                    if(this.identifierTB.getValue()){
                        resultString += m.getIdentifier() + "\t";
                    }

                    if(this.nameTB.getValue()){
                        resultString += m.getDisplayName() + "\t";
                    }

                    resultString += "\n";
                }
            }
        }

        if(sequTB != null && this.sequTB.getValue()){
            for(Molecule m : result.getSequences()){
                if(m.isToHighlight()){
                    if(this.typeTB.getValue()){
                        resultString += "Sequence\t";
                    }

                    if(this.identifierTB.getValue()){
                        resultString += m.getIdentifier() + "\t";
                    }

                    if(this.nameTB.getValue()){
                        resultString += m.getDisplayName() + "\t";
                    }

                    resultString += "\n";
                }
            }
        }

        if(otheTB != null && this.otheTB.getValue()){
            for(Molecule m : result.getOthers()){
                if(m.isToHighlight()){
                    if(this.typeTB.getValue()){
                        resultString += "Other\t";
                    }

                    if(this.identifierTB.getValue()){
                        resultString += m.getIdentifier() + "\t";
                    }

                    if(this.nameTB.getValue()){
                        resultString += m.getDisplayName() + "\t";
                    }

                    resultString += "\n";
                }
            }
        }

        return resultString;
    }

    /**
     * Update internal result and preview text.
     * @param result New result data
     */
    public void update(Result result){
        this.result = result;
        textArea.setText(resultToText());
        textArea.setStyleName("elv-PreviewPanel-Download");
    }

//
//    public ArrayList<String> getSelectedTypes() {
//        ArrayList<String> types = new ArrayList<String>();
//        for(ToggleButton tb : typesList){
//            if(tb.getValue()){
//                types.add(tb.getText());
//            }
//        }
//        return types;
//    }
//
//    public ArrayList<String> getSelectedFields() {
//        ArrayList<String> fields = new ArrayList<String>();
//        for(ToggleButton tb : fieldList){
//            if(tb.getValue()){
//                fields.add(tb.getText());
//            }
//        }
//        return fields;
//    }
}

