package org.reactome.web.elv.client.manager.tour;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ToggleButton;
import org.reactome.web.elv.client.common.Controller;
import org.reactome.web.elv.client.common.EventBus;
import org.reactome.web.elv.client.common.ReactomeImages;
import org.reactome.web.elv.client.common.data.model.*;
import org.reactome.web.elv.client.common.events.ELVEventType;
import org.reactome.web.elv.client.common.model.Pair;
import org.reactome.web.elv.client.common.model.Path;
import org.reactome.web.elv.client.common.utils.Console;
import org.reactome.web.elv.client.common.widgets.disclosure.DisclosureImages;
import org.reactome.web.elv.client.details.model.DetailsTabType;
import org.reactome.web.elv.client.manager.state.AdvancedState;
import org.reactome.web.elv.client.manager.tour.dialogs.TopPopupPanel;
import org.reactome.web.elv.client.manager.tour.dialogs.TourInfoPanel;
import org.reactome.web.elv.client.manager.tour.dialogs.TourInitialQuestion;
import org.reactome.web.elv.client.manager.tour.events.TourCancelledEvent;
import org.reactome.web.elv.client.manager.tour.events.TourQuestionEvent;
import org.reactome.web.elv.client.manager.tour.events.TourStepFinishedEvent;
import org.reactome.web.elv.client.manager.tour.handlers.TourCancelledEventHandler;
import org.reactome.web.elv.client.manager.tour.handlers.TourQuestionEventHandler;
import org.reactome.web.elv.client.manager.tour.handlers.TourStepFinishedEventHandler;

/**
 * This manager shows a message on the top of the Pathway Browser asking the user about a tour through the browser
 * features.
 *
 * This manager fires different events to notify other modules about the tour stage and step and also sniffs the bus
 * in order to keep track of the user actions to ensure the proposed tasks have been done :)
 *
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class TourManager extends Controller implements TourQuestionEventHandler, TourStepFinishedEventHandler,
        TourCancelledEventHandler {
    public static final String TOUR_TIMES = "ELV_TOUR_TIMES";
//    public static final int TOUR_MAX_TIMES = 2;

    final private TopPopupPanel message = new TopPopupPanel();
    final private TourState state = new TourState(TourStage.INITIAL_STAGE, 0);

    private boolean showTour = true;

    //Different panels size changes to ensure the user is moving them
    private int treeResizedCount = 0;
    private int detailsResizedCount = 0;

    public TourManager(EventBus eventBus) {
        super(eventBus);
        //The messages have a "next" link equivalent to finishing the current step
        this.message.addTourStepFinishedEventHandler(this);
        this.message.addTourCancelledEventHandler(this);

//        this.showTour = getTourHiddenTimes()<TOUR_MAX_TIMES;
    }

    private void tourStateChanged(TourStage stage, int step){
        this.state.setStage(stage); this.state.setStep(step);
        if(stage.equals(TourStage.INITIAL_STAGE)){
            eventBus.fireELVEvent(ELVEventType.TOUR_MANAGER_TOUR_STARTED);
            moveForward();
        }else if(stage.equals(TourStage.CANCELED)){
            eventBus.fireELVEvent(ELVEventType.TOUR_MANAGER_TOUR_CANCELLED);
            tourCancelled();
        }else if(stage.equals(TourStage.HIDDEN)){
            tourHidden();
        }else{
            Pair<TourStage,Integer> pair = new Pair<TourStage,Integer>(stage, step);
            eventBus.fireELVEvent(ELVEventType.TOUR_MANAGER_TOUR_PROGRESS, pair);
        }
    }

    @Override
    public void onStateManagerTargetReached() {
        if(!this.showTour) return;
        this.showTour = false;

        TourInitialQuestion dialog = new TourInitialQuestion();
        dialog.setAnimationEnabled(true);
        dialog.addTourQuestionEventHandler(this);
        dialog.show();
    }

    @Override
    public void onTourDialogOptionSelected(TourQuestionEvent event) {
        switch (event.getAnswer()){
            case YES:
                tourStateChanged(TourStage.INITIAL_STAGE, 0);
                break;
            case NO:
                tourStateChanged(TourStage.HIDDEN, 0);
                break;
        }
    }

    @Override
    public void onTourStepFinished(TourStepFinishedEvent event) {
        this.moveForward();
    }

    @Override
    public void onTourCancelled(TourCancelledEvent event) {
        tourStateChanged(TourStage.CANCELED, -1);
    }

    /**
     * This method is called every time that a step is finished, either the user does what is ask to do
     * or clicks on the "next" link of the shown message.
     * The idea is to keep the logic of the TOUR in this method, so we can easily change the steps just
     * redefining them in here.
     */
    private void moveForward(){
        int step = this.state.getStep();
        switch (this.state.getStage()){
            case INITIAL_STAGE:
                tourToShowModules(0);                           break;
            case SHOW_MODULES:
                switch (step){
                    case 4: tourToPanelsResizing();             break;
                    default: tourToShowModules(step+1);         break;
                }                                               break;
            case TEST_RESIZE:
                tourToTopBar();                                 break;
            case TEST_TOP_BAR:
                switch (step){
                    case 0: tourToTopBarStep1();                break;
                    case 1: tourToTopBarStep2();                break;
                    case 2: tourToTopBarStep3();                break;
                    case 3: tourToTopBarStep4();                break;
                    case 4: tourToEventsHierarchy(true);        break;
                }                                               break;
            case TEST_HIERARCHY:
                switch (step){
                    case 0: tourToEventsHierarchyStep1(true);   break;
                    case 1: tourToDiagramViewer();              break;
                }                                               break;
            case TEST_DIAGRAM:
                switch (step){
                    case 0: tourToDiagramViewerStep1();         break;
                    case 1: tourToDiagramViewerStep2();         break;
                    case 2: tourToDiagramViewerStep3();         break;
                    case 3: tourToDetailsPanel();               break;
                }                                               break;
            case TEST_DETAILS:
                switch (step){
                    case 0: tourToDetailsPanelStep1();          break;
                    case 1: tourToDetailsPanelStep2();          break;
                    case 2: tourToDetailsPanelStep3();          break;
                    case 3: tourToDetailsPanelStep4();          break;
                    case 4: tourToDetailsPanelStep5();          break;
                    case 5: tourToDetailsPanelStep6();          break;
                    case 6: tourToDetailsPanelStep7();          break;
                    case 7: tourToDetailsPanelStep8();          break;
                    case 8: tourToTestHistory();                break;
                }                                               break;
            case TEST_HISTORY:
                switch (step){
                    case 0: tourFinished();                     break;
                }                                               break;
            default:
                System.err.println("THIS SHOULD NOT BE REACHED");
        }
    }

    /******************* SHOW THE DIFFERENT MODULES [BEGIN] ***********************/
    private void tourToShowModules(int step){
        tourStateChanged(TourStage.SHOW_MODULES, step);
        String txt;
        switch (this.state.getStep()){
            case 0:
                txt = "Welcome to the tour\nNote: You can leave this tour at any time by clicking on the 'Exit Tour' button. " +
                        "You can also move this panel by dragging it (clicking on the title)\n\n" +
                        "This pathway browser has four sections...\nClick on Next to continue";
                message.setContent(txt);
                return; //DO NOT USE BREAK HERE
            case 1:
                txt = "FEATURE BAR: You can switch between the different species, toggle some sections of the pathway browser and view the diagram key";break;
            case 2:
                txt = "EVENT HIERARCHY: You can browse Reactome events (pathways and reactions) and select the one you would like to view";break;
            case 3:
                txt = "DIAGRAM PANEL: You will view the selected pathway diagram (and its illustrations when available)";break;
            case 4:
                txt = "DETAILS PANEL: You can view details of Reactome's and other resources' content about the selected instance";break;
            default:
                txt = "";
        }
        message.setContent(txt + "\nClick on Next to continue");
    }
    /******************* SHOW THE DIFFERENT MODULES [END] *************************/

    /******************* RESIZING THE PANELS [BEGIN] ***********************/
    private void tourToPanelsResizing(){
        tourStateChanged(TourStage.TEST_RESIZE, 0);
        message.setContent("Let's explore the pathway browser!\n" +
                "Move the highlighted bars to change the panel sizes to continue or click on Next");
        message.show();
    }

    @Override
    public void onHierarchyPanelResized(Integer size) {
        if(!this.state.isStage(TourStage.TEST_RESIZE)) return;
        treeResizedCount++;
        if(treeResizedCount >10 && detailsResizedCount>10){
            this.moveForward();
        }
    }

    @Override
    public void onDetailPanelResized(Integer size) {
        if(!this.state.isStage(TourStage.TEST_RESIZE)) return;
        detailsResizedCount++;
        if(treeResizedCount >10 && detailsResizedCount>10){
            this.moveForward();
        }
    }
    /******************* RESIZING THE PANELS [END] *************************/


    /******************* PLAYING WITH THE TOP-BAR [BEGIN] ******************/
    private void tourToTopBar(){
        tourStateChanged(TourStage.TEST_TOP_BAR, 0);
        message.setContent("Click the Reactome logo (top left corner) to return to the homepage\n" +
                "Click on Next to continue");
    }

    public void tourToTopBarStep1(){
        tourStateChanged(TourStage.TEST_TOP_BAR, 1);
        message.setContent("Select a species other than 'Homo sapiens' in the species selector", false);
    }

    @Override
    public void onTopBarSpeciesSelected(Species species) {
        if(this.state.isStage(TourStage.TEST_TOP_BAR) && this.state.getStep()==1){
            if(species.getDbId().equals(AdvancedState.DEFAULT_SPECIES_ID)){
                this.moveForward();
            }else{
                //TODO: It would be nice to add a link to the explanation of the events orthology prediction
                message.setContent("You are now viewing the event hierarchy for " + species.getDisplayName() + "!\n" +
                        "Select the species 'Homo sapiens' in the species selector to continue", false);
                message.show();
            }
        }
    }

    public void tourToTopBarStep2(){
        tourStateChanged(TourStage.TEST_TOP_BAR, 2);
        message.setContent("Click on the layout selector buttons ( __image__ __image__ __image__ ) to see how the " +
                "layout changes or click on Next to continue",
                ReactomeImages.INSTANCE.hierarchy(), ReactomeImages.INSTANCE.details(), ReactomeImages.INSTANCE.diagram());
    }

    @Override
    public void onTopBarDetailsButtonToggled(ToggleButton btn) {
        if(this.state.isStage(TourStage.TEST_TOP_BAR) && this.state.getStep()==2){
            this.moveForward();
        }
    }

    public void tourToTopBarStep3(){
        tourStateChanged(TourStage.TEST_TOP_BAR, 3);
        message.setContent("Click on the diagram key icon ( __image__ ) to open the analysis tool data submission interface ",
                true, ReactomeImages.INSTANCE.analysisTool());
    }

    public void tourToTopBarStep4(){
        tourStateChanged(TourStage.TEST_TOP_BAR, 4);
        message.setContent("Click on the diagram key icon ( __image__ ) to see a detailed diagram " +
                "description. Close it to continue", false, ReactomeImages.INSTANCE.downArrow());
    }

    @Override
    public void onTopBarDiagramKeyButtonToggled(ToggleButton btn) {
        if(this.state.isStage(TourStage.TEST_TOP_BAR) && this.state.getStep()==4){
            if(!btn.getValue()){
                this.moveForward();
            }
        }
    }
    /******************* PLAYING WITH THE TOP-BAR [END] ********************/

    /************** PLAYING WITH THE EVENTS HIERARCHY [BEGIN] **************/
    private void tourToEventsHierarchy(boolean first){
        tourStateChanged(TourStage.TEST_HIERARCHY, 0);
        String msg = "Select a Pathway ( __image__ ) in the Event Hierarchy to continue\n " +
                "The diagram will be loaded and the details will be shown in the details panel";
        if(!first){
            msg = "You have not clicked on a Pathway!\n" + msg;
        }
        message.setContent(msg, false, ReactomeImages.INSTANCE.pathway());
    }

    @Override
    public void onHierarchyEventSelected(Path path, Pathway pathway, Event event) {
        if(this.state.isStage(TourStage.TEST_HIERARCHY)){
            if(this.state.getStep()==0){
                if(event instanceof Pathway){
                    this.moveForward();
                }else{
                    tourToEventsHierarchy(false);
                }
            }else if(this.state.getStep()==1){
                if(event instanceof  ReactionLikeEvent){
                    this.moveForward();
                }else{
                    tourToEventsHierarchyStep1(false);
                }
            }
        }
    }

    private void tourToEventsHierarchyStep1(boolean first){
        tourStateChanged(TourStage.TEST_HIERARCHY, 1);
        String msg;
        if(first){
            msg = "Now expand the Pathways until you find reactions and select one of them to continue";
        }else{
            msg = "You have not clicked on a reaction!\nExpand the Pathways until you find reactions and select one of them to continue";
        }
        message.setContent(msg, false);
    }
    /************** PLAYING WITH THE EVENTS HIERARCHY [END] ****************/

    /************** PLAYING WITH THE DIAGRAM VIEWER [BEGIN] ****************/
    private void tourToDiagramViewer(){
        tourStateChanged(TourStage.TEST_DIAGRAM, 0);
        message.setContent("Now the diagram!\n" +
                "Select an input or output of the selected reaction (or another reaction) or click on Next to continue");
    }

    private void tourToDiagramViewerStep1(){
        tourStateChanged(TourStage.TEST_DIAGRAM, 1);
        message.setContent("Please select a reaction or click on Next to continue");
    }

    @Override
    public void onDiagramEntitySelected(DatabaseObject databaseObject) {
        if(this.state.isStage(TourStage.TEST_DIAGRAM)){
            switch (this.state.getStep()){
                case 0:
                    if(databaseObject instanceof PhysicalEntity){
                        moveForward();
                    }else{
                        message.setContent("That is not an input or output ;)\n" +
                                "Select an input or output of the selected reaction (or other reaction) or click on Next to continue\n" +
                                "HINT: Input and output objects have names displayed");
                    }
                    break;
                case  1:
                    if(databaseObject instanceof ReactionLikeEvent){
                        moveForward();
                    }else{
                        message.setContent("That is not a reaction ;)\nSelect a reaction or click on Next to continue\n" +
                                "HINT: Reactions are represented by a symbolic node which is connected to inputs " +
                                "and outputs by lines");
                    }
                    break;
            }
        }
    }

    private void tourToDiagramViewerStep2(){
        tourStateChanged(TourStage.TEST_DIAGRAM, 2);
        message.setContent("Now use the plus and minus buttons on the top left corner of the diagram panel to zoom " +
                "in and out, and the arrows to move the diagram\n" +
                "NOTE: You can also zoom with the mouse scroll wheel and navigate the diagram by dragging it with the mouse\n" +
                "Please click on Next to continue");
    }

    private void tourToDiagramViewerStep3(){
        tourStateChanged(TourStage.TEST_DIAGRAM, 3);
        message.setContent("Please click on the settings icon on the rop right corner of the diagram panel to see " +
                "a list of options\n" +
                "NOTE: You can also right click on an empty region of the diagram to see other options\n" +
                "Please click on Next to continue");
    }
    /************** PLAYING WITH THE DIAGRAM VIEWER [END] ******************/

    /************** PLAYING WITH THE DETAILS PANEL [BEGIN] *****************/
    private void tourToDetailsPanel(){
        tourStateChanged(TourStage.TEST_DETAILS, 0);
        StringBuilder sb = new StringBuilder();
        for (DetailsTabType detailsTabType : DetailsTabType.values()) {
            sb.append(detailsTabType.getTitle());
            sb.append(", ");
        }
        sb.delete(sb.length()-2, sb.length());
        message.setContent("...and finally the details panel\n" +
                "It contains " + DetailsTabType.values().length +"  different tabs: " + sb.toString() + "\n" +
                "Please click on Next to continue");
    }

    private void tourToDetailsPanelStep1(){
        tourStateChanged(TourStage.TEST_DETAILS, 1);
        message.setContent("The overview tab displays information from the Reactome database\n" +
                "Initially, only the main properties are shown. Clicking on the __image__ icon (right side of some " +
                "of the panels inside) will expand it, retrieving and displaying more information\n" +
                "HINT: Clicking on the panel name has the same effect\n" +
                "Please click on Next to continue",
                DisclosureImages.INSTANCE.getExpandImage());
    }

    private void tourToDetailsPanelStep2(){
        tourStateChanged(TourStage.TEST_DETAILS, 2);
        message.setContent("The summary panels can also contain an icon on the left hand side\n" +
                " __image__ \n" + //The spaces before and after __image__ are necessary for the parser
                "Clicking on this icon selects the named entity refocusing the whole browser to display " +
                "information about it\nPlease select the Molecules tab or click on Next to continue",
                ReactomeImages.INSTANCE.summaryPanelExample());
    }

    private void tourToDetailsPanelStep3(){
        tourStateChanged(TourStage.TEST_DETAILS, 3);
        message.setContent("The current tab shows the Participating Molecules\n" +
                "It shows the participants of the selected instance\n" +
                "You can either browse or download the data\n" +
                "HINT: Use the plus symbols to expand the content\n" +
                "Please select the Structures tab or click on Next to continue");
    }

    private void tourToDetailsPanelStep4(){
        tourStateChanged(TourStage.TEST_DETAILS, 4);
        message.setContent("The current tab displays the Structural information depending on the type of the selected" +
                "instance and availability in the queried resource\n" +
                ">> Rhea reaction chemistry for reactions\n" +
                ">> PDB structures for entities\n" +
                ">> ChEBI structures for small molecules\n" +
                "Please select the Expression tab or click on Next to continue");
    }

    private void tourToDetailsPanelStep5(){
        tourStateChanged(TourStage.TEST_DETAILS, 5);
        message.setContent("The current tab displays Expression data from the Gene Expression Atlas\n" +
                "Gene expression information is shown for different body parts corresponding to the genes associated " +
                "with the selected instance\n" +
                "Please select the Analysis tab or click on Next to continue");
    }

    private void tourToDetailsPanelStep6(){
        tourStateChanged(TourStage.TEST_DETAILS, 6);
        message.setContent("The current tab displays the result of the analysis tool\n" +
                "It is shown in a table and is interactive with the rest of the pathway browser\n" +
                "Please open the data submission tool clicking the icon ( __image__ ) placed on the top bar of the pathway browser " +
                "Please select the Processes tab or click on Next to continue", ReactomeImages.INSTANCE.analysisTool());
    }

    private void tourToDetailsPanelStep7(){
        tourStateChanged(TourStage.TEST_DETAILS, 7);
        message.setContent("The current tab shows the Processes information\n" +
                "If an event is clicked, pathways that include this event will be listed\n" +
                "If a molecule is clicked, events and objects that include the molecule will be displayed\n" +
                "In addition, if the molecule exists in other modified forms, these will be listed\n" +
                "HINT: This tab contains panels which can be expanded and the associated objects selected " +
                "in the same way as those in the overview tab\n" +
                "Please select the Downloads tab or Click on Next to continue");
    }

    private void tourToDetailsPanelStep8(){
        tourStateChanged(TourStage.TEST_DETAILS, 8);
        message.setContent("The current tab shows the Download options\n" +
                "This tab allows you to download the selected pathway in several formats ranging from " +
                "xml-type formats to simple Word and PDF documents\n" +
                "Please click on Next to continue");
    }

    @Override
    public void onDetailsPanelTabChanged(DetailsTabType tabType) {
        if(this.state.isStage(TourStage.TEST_DETAILS)){
            switch (this.state.getStep()){
                case 2:
                    if(tabType.equals(DetailsTabType.PARTICIPATING_MOLECULES)) moveForward();
                    else message.setContent("Please select " + DetailsTabType.PARTICIPATING_MOLECULES.getTitle() + " tab or click on Next to continue");
                    break;
                case 3:
                    if(tabType.equals(DetailsTabType.STRUCTURES)) moveForward();
                    else message.setContent("Please select " + DetailsTabType.STRUCTURES.getTitle() + " tab or click on Next to continue");
                    break;
                case 4:
                    if(tabType.equals(DetailsTabType.EXPRESSION)) moveForward();
                    else message.setContent("Please select " + DetailsTabType.EXPRESSION.getTitle() + " tab or click on Next to continue");
                    break;
                case 5:
                    if(tabType.equals(DetailsTabType.ANALYSIS)) moveForward();
                    else message.setContent("Please select " + DetailsTabType.ANALYSIS.getTitle() + " tab or click on Next to continue");
                    break;
                case 6:
                    if(tabType.equals(DetailsTabType.PARTICIPATING_PROCESSES)) moveForward();
                    else message.setContent("Please select " + DetailsTabType.PARTICIPATING_PROCESSES.getTitle() + " tab or click on Next to continue");
                    break;
                case 7:
                    if(tabType.equals(DetailsTabType.DOWNLOADS)) moveForward();
                    else message.setContent("Please select " + DetailsTabType.DOWNLOADS.getTitle() + " tab or click on Next to continue");
                    break;
            }
        }
    }
    /************** PLAYING WITH THE DETAILS PANEL [END] *****************+*/

    /************** PLAYING WITH THE BROWSER HISTORY BUTTONS [BEGIN] *****************+*/
    private void tourToTestHistory(){
        tourStateChanged(TourStage.TEST_HISTORY, 0);
        message.setContent("ONE MORE THING!\n" +
                "You can use the browser back and forward buttons to navigate through your recently visited instances\n" +
                "You can also bookmark your current view and/or send the link to your colleagues :)\n" +
                "Click on Next to continue", false, true);
    }
    /************** PLAYING WITH THE BROWSER HISTORY BUTTONS [END] *******************+*/

    private void tourHidden(){
        Cookies.setCookie(TOUR_TIMES, "" + (getTourHiddenTimes()+1));
        Console.info("TOUR HIDDEN...");
    }

    private void tourCancelled(){
        showFinalMessage();
    }

    private void tourFinished(){
        eventBus.fireELVEvent(ELVEventType.TOUR_MANAGER_TOUR_FINISHED);
        showFinalMessage();
    }

    private void showFinalMessage(){
        message.hide();
        TourInfoPanel info = new TourInfoPanel();
        info.addContent(new InlineLabel("We hope the tour was helpful"));
        info.addContent(new InlineLabel("If you would like to learn more, please refer to the "));
        info.addContent(new Anchor("Reactome User Guide", "http://www.reactome.org/userguide/Usersguide.html", "_blank"));
        info.addContent(new InlineLabel("Enjoy Browsing the REACTOME Pathways!"));
        info.show();
    }

    private Integer getTourHiddenTimes(){
        String tourHidden = Cookies.getCookie(TOUR_TIMES);
        return tourHidden==null?0:Integer.valueOf(tourHidden);
    }
}