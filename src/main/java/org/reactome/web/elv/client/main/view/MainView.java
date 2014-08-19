package org.reactome.web.elv.client.main.view;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.elv.client.manager.messages.MessageObject;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface MainView {
	
	public interface Presenter {
		public void go(final HasWidgets container);
        public void panelResized(String panel, Integer size);
        public void errorMsg(MessageObject msgObj);
	}

    Widget asWidget();
    void errorMessage(String message);
    void setPresenter(Presenter presenter);
    void toggleDetailsPanel(boolean open);
    void toggleHierarchyPanel(boolean open);
}