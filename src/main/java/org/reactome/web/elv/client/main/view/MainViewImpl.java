package org.reactome.web.elv.client.main.view;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.elv.client.center.view.CenterView;
import org.reactome.web.elv.client.common.utils.Console;
import org.reactome.web.elv.client.details.view.DetailsView;
import org.reactome.web.elv.client.hierarchy.view.HierarchyView;
import org.reactome.web.elv.client.manager.messages.MessageObject;
import org.reactome.web.elv.client.manager.messages.MessageType;
import org.reactome.web.elv.client.topbar.view.TopBarView;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class MainViewImpl implements IsWidget, MainView {
    private final String DETAILS_HEIGHT_COOKIE = "ELV_DHC";
    private final String HIERARCHY_WIDTH_COOKIE = "ELV_HWC";
    /**
     *  Defines the initial height value for the Details panel
     *  and will contain the details height value
     */
    private Integer DETAILS_HEIGHT = 285;

    private final Integer DETAILS_MIN_HEIGHT = 150;

     /**
     *  Defines the initial height value for the Hierarchy panel
     *  and will contain the hierarchy height value
     */
    private Integer HIERARCHY_WIDTH = 250;

    private final Integer HIERARCHY_MIN_WIDTH = 200;

	private Presenter presenter;

    private SplitLayoutPanel splitPanel;

	private HierarchyView treeView;

	private DetailsView detailsView;

    private CenterView centerView;

    private TopBarView topBarView;

    private DockLayoutPanel appPanel;

	public MainViewImpl(HierarchyView treeView, DetailsView detailsView, CenterView centerView, TopBarView topBarView) {
		this.treeView = treeView;
		this.detailsView = detailsView;
        this.centerView = centerView;
        this.topBarView = topBarView;
		initWidget();
	}

	private void initWidget() {
		splitPanel = new SplitLayoutPanel(){
            @Override
            public void forceLayout() {
                try{
                    super.forceLayout();
                }catch (AssertionError error){
                    Console.error(getClass().getName() + " -> force layout failed");
                    error.printStackTrace();
                    errorMessage("Changing the layout of the PathwayBrowser failed. ERROR: " + error.getMessage());
                }

                //Checking if the hierarchy panel has been resized
                int size = treeView.asWidget().getOffsetWidth();
                if( size != HIERARCHY_WIDTH){
                    if( size >= HIERARCHY_MIN_WIDTH )
                        HIERARCHY_WIDTH = size; //Keeping the new size
                    setCookieValue(HIERARCHY_WIDTH_COOKIE, size);
                    presenter.panelResized("hierarchy", size);
                }

                //Checking if the details panel has been resized
                size = detailsView.asWidget().getOffsetHeight();
                if( size != DETAILS_HEIGHT){
                    if(size >= DETAILS_MIN_HEIGHT)
                        DETAILS_HEIGHT = size; //Keeping the new size
                    setCookieValue(DETAILS_HEIGHT_COOKIE, size);
                    presenter.panelResized("details", size);
                }
            }
        };

		//NORTH
        //splitPanel.addNorth(, 50);

	    //EAST
        //splitPanel.addEast(new HTML("Options"), 10);

	    //WEST
        Widget tree = treeView.asWidget();
        splitPanel.addWest(tree, getCookieValue(HIERARCHY_WIDTH_COOKIE, HIERARCHY_WIDTH));
        //splitPanel.setWidgetMinSize(tree, HIERARCHY_MIN_WIDTH);

        //SOUTH
        Widget details = detailsView.asWidget();
        splitPanel.addSouth(details, getCookieValue(DETAILS_HEIGHT_COOKIE, DETAILS_HEIGHT));
        //splitPanel.setWidgetMinSize(details, DETAILS_MIN_HEIGHT);

        //CENTER
        splitPanel.add(centerView.asWidget());

        appPanel = new DockLayoutPanel(Style.Unit.PX);
        appPanel.addStyleName("app-Content");
        appPanel.addNorth(topBarView.asWidget(), 45);
        appPanel.add(splitPanel);
	}

    @Override
	public Widget asWidget() {
		//return uiBinder.createAndBindUi(this);
	    return appPanel;

	}

    @Override
    public void errorMessage(String message) {
        MessageObject msgObj = new MessageObject(message + "\n" +
                "in ", getClass(), MessageType.INTERNAL_ERROR);
        Console.error(getClass() + message);
        this.presenter.errorMsg(msgObj);
        //Window.alert(message);
    }

    private Integer getCookieValue(String cookie, Integer defaultValue){
        String value = Cookies.getCookie(cookie);
        if(value==null)
            return defaultValue;
        else
            return Integer.valueOf(value);
    }

    private void setCookieValue(String cookie, Integer value){
        Cookies.setCookie(cookie, value.toString());
    }

    @Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

    @Override
    public void toggleDetailsPanel(boolean open) {
        //If the DETAILS_HEIGHT is bigger than the minimun, we keep it, otherwise the minimum height is set
        Integer size = DETAILS_HEIGHT > DETAILS_MIN_HEIGHT ? DETAILS_HEIGHT : DETAILS_MIN_HEIGHT;
        DETAILS_HEIGHT = size;
        if(!open)
            size = 0;
        splitPanel.setWidgetSize(detailsView.asWidget(), size);
        splitPanel.forceLayout();
        setCookieValue(DETAILS_HEIGHT_COOKIE, size);
    }

    @Override
    public void toggleHierarchyPanel(boolean open) {
        //If the HIERARCHY WIDTH is bigger than the minimun, we keep it, otherwise the minimum width is set
        Integer size = HIERARCHY_WIDTH > HIERARCHY_MIN_WIDTH ? HIERARCHY_WIDTH : HIERARCHY_MIN_WIDTH;
        HIERARCHY_WIDTH = size;
        if(!open)
            size = 0;
        splitPanel.setWidgetSize(treeView.asWidget(), size);
        splitPanel.forceLayout();
        setCookieValue(HIERARCHY_WIDTH_COOKIE, size);
    }
}
