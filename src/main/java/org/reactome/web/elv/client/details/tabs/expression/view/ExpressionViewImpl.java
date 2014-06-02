package org.reactome.web.elv.client.details.tabs.expression.view;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.elv.client.common.data.model.*;
import org.reactome.web.elv.client.details.model.DetailsTabType;
import uk.ac.ebi.pwp.widgets.gxa.ui.GXAViewer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ExpressionViewImpl implements ExpressionView {
    private final DetailsTabType TYPE = DetailsTabType.EXPRESSION;
    private Presenter presenter;

    private Map<Long, GXAViewer> panelsLoaded = new HashMap<Long, GXAViewer>();
    private GXAViewer currentPanel;

    private DockLayoutPanel tab;
    private HTMLPanel title;

    public ExpressionViewImpl() {
        this.title = new HTMLPanel(this.TYPE.getTitle());
        this.tab = new DockLayoutPanel(Style.Unit.PX);
    }

    @Override
    public Widget asWidget() {
        return this.tab;
    }

    @Override
    public DetailsTabType getDetailTabType() {
        return this.TYPE;
    }

    @Override
    public HTMLPanel getTitle() {
        return this.title;
    }

    @Override
    public void setInitialState() {
        this.currentPanel = null;
        this.title.getElement().setInnerHTML(TYPE.getTitle());
        this.tab.clear();
        this.tab.add(new HTMLPanel("Expression..."));
    }


    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setDetailedView(DatabaseObject databaseObject, Long respId) {
        if(databaseObject instanceof Pathway){
            GXAViewer panel = this.panelsLoaded.get(respId);
            String reactomeID = databaseObject.getStableIdentifier().getDisplayName().split("\\.")[0];
            panel.setReactomeID(reactomeID);
        }
    }

    @Override
    public void setProteinAccessions(List<ReferenceSequence> referenceSequenceList, Long respId) {
        GXAViewer panel = this.panelsLoaded.get(respId);

        List<String> uniProtIDs = new LinkedList<String>();
        for (ReferenceSequence referenceSequence : referenceSequenceList) {
            if(referenceSequence.getIdentifier()==null) continue;
            uniProtIDs.add(referenceSequence.getIdentifier());
        }
        if(uniProtIDs.isEmpty()){
            panel.setEmpty();
        }else{
            panel.setUniProtIDs(uniProtIDs);
        }
    }

    @Override
    public void showInstanceDetails(Pathway pathway, DatabaseObject databaseObject) {
        if(!this.showInstanceDetailsIfExists(pathway, databaseObject)){
            DatabaseObject toShow = databaseObject!=null?databaseObject:pathway;
            this.currentPanel = new GXAViewer();
            this.panelsLoaded.put(toShow.getDbId(), this.currentPanel);
            if(toShow instanceof Pathway){
                this.presenter.getDetailedView(toShow, toShow.getDbId());
            }else if(toShow instanceof PhysicalEntity || toShow instanceof Event){
                this.presenter.getProteinAccessions(toShow, toShow.getDbId());
            }else {
                this.currentPanel.setEmpty();
            }
            this.showStructuresPanel(this.currentPanel);
        }
    }

    @Override
    public boolean showInstanceDetailsIfExists(Pathway pathway, DatabaseObject databaseObject) {
        DatabaseObject toShow = databaseObject!=null?databaseObject:pathway;
        if(this.panelsLoaded.containsKey(toShow.getDbId())){
            this.currentPanel = this.panelsLoaded.get(toShow.getDbId());
            this.showStructuresPanel(this.currentPanel);
            this.tab.clear();
            this.tab.add(currentPanel);
//            this.refreshTitle(this.currentPanel.getNumberOfLoadedStructures(), this.currentPanel.getNumberOfRequiredStructures());
            return true;
        }else{
            this.setInitialState();
            return false;
        }
    }

    private void showStructuresPanel(GXAViewer structuresPanel){
        this.tab.clear();
        this.tab.add(structuresPanel);
//        this.refreshTitle(structuresPanel.getNumberOfLoadedStructures(), structuresPanel.getNumberOfRequiredStructures());
    }
}
