package org.reactome.web.elv.client.main.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.ToggleButton;
import org.reactome.web.elv.client.common.Controller;
import org.reactome.web.elv.client.common.EventBus;
import org.reactome.web.elv.client.common.events.ELVEventType;
import org.reactome.web.elv.client.main.view.MainView;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class MainPresenter extends Controller implements MainView.Presenter {

	private MainView view;

	public MainPresenter(EventBus eventBus, MainView view){
        super(eventBus);
		this.view = view;
		this.view.setPresenter(this);
	}

	public void go(HasWidgets container) {
        container.add(view.asWidget());
	}

//    @Override
//    public void onStateManagerDatabaseObjectsSelected(Pathway pathway, DatabaseObject event) {
//        this.view.forceDiagramLayout();
//    }


    @Override
    public void onStateManagerError(String message) {
        this.view.errorMessage(message);
    }

    @Override
    public void onTopBarDetailsButtonToggled(ToggleButton btn) {
        super.onTopBarDetailsButtonToggled(btn);
        this.view.toggleDetailsPanel(btn.getValue());
    }

    @Override
    public void onTopBarHierarchyButtonToggled(ToggleButton btn) {
        super.onTopBarHierarchyButtonToggled(btn);
        this.view.toggleHierarchyPanel(btn.getValue());
    }

    @Override
    public void panelResized(String panel, Integer size) {
        ELVEventType type;
        if(panel.toLowerCase().equals("hierarchy")){
            type = ELVEventType.HIERARCHY_PANEL_RESIZED;
        }else{
            type = ELVEventType.DETAILS_PANEL_RESIZED;
        }
        this.eventBus.fireELVEvent(type, size);
    }
}
