package org.reactome.web.elv.client.details.view;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.elv.client.common.widgets.glass.GlassPanel;
import org.reactome.web.elv.client.details.model.DetailsTabType;
import org.reactome.web.elv.client.details.tabs.DetailsTabView;

/**
  * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DetailsViewImpl implements DetailsView, SelectionHandler<Integer> {

	private Presenter presenter;

    private TabLayoutPanel tabPanel;

    /**
     * The glass element.
     */
    private GlassPanel glass = null;

    public DetailsViewImpl() {
        //NOTE that the initWidget method will be called when setting the presenter
        // -> THIS IS A SPECIAL CASE
    }

    private void initWidget(){
        tabPanel = new TabLayoutPanel(2.0, Unit.EM){
            @Override
            public void onResize() {
                super.onResize();
                if(glass!=null){
                    glass.onResize();
                }
            }
        };
        tabPanel.addStyleName("elv-Details-Content");
        tabPanel.setAnimationDuration(500);

        for (DetailsTabView.Presenter tab : presenter.getDetailsTabs()) {
            tabPanel.add(tab.getView().asWidget(), tab.getView().getTitle());
        }

        tabPanel.selectTab(DetailsTabType.getDefaultIndex());

        tabPanel.addSelectionHandler(this);
    }

    @Override
    public Widget asWidget() {
        return tabPanel;
    }

    /*
    * When a tab is selected, if there is an instance already selected then the
    * details related to that tab are loaded/shown
    */
    @Override
    public void onSelection(SelectionEvent<Integer> event) {
        int index = tabPanel.getSelectedIndex();
        presenter.showInstanceDetails(index);
    }

    @Override
    public void selectTab(Integer index) {
        tabPanel.selectTab(index);
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
        initWidget();
    }

    @Override
    public void tourFadeIn() {
        if(glass==null){
            glass = new GlassPanel(this.tabPanel);
        }
        RootPanel.get().add(glass);
    }

    @Override
    public void tourFadeOut() {
        if(glass!=null){
            RootPanel.get().remove(glass);
        }
    }
}
