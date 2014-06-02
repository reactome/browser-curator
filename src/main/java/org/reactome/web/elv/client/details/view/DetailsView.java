package org.reactome.web.elv.client.details.view;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.elv.client.details.tabs.DetailsTabView;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface DetailsView {

	public interface Presenter {
        public List<DetailsTabView.Presenter> getDetailsTabs();
        public void showInstanceDetails(Integer index);
	}

    Widget asWidget();
    void selectTab(Integer index);
    void setPresenter(Presenter presenter);
    void tourFadeIn();
    void tourFadeOut();
}
