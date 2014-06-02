package org.reactome.web.elv.client.details.model.widgets;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Event;
import org.reactome.web.elv.client.common.data.model.LiteratureReference;
import org.reactome.web.elv.client.common.model.Ancestors;
import org.reactome.web.elv.client.details.events.DataRequiredListener;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public abstract class DetailsPanel extends Composite {
    private boolean isLoaded = false;
    protected DetailsPanel parentPanel;

    public DetailsPanel(DetailsPanel parentPanel) {
        this.parentPanel = parentPanel;
    }

    public abstract DatabaseObject getDatabaseObject();

    public void dataRequired(DatabaseObject databaseObject){
        DataRequiredListener.getDataRequiredListener().onDataRequired(this, databaseObject);
    }

    public void ancestorsRequired(Event event){
        DataRequiredListener.getDataRequiredListener().onPathRequired(this, event);
    }

    public void referencesRequired(DatabaseObject databaseObject){
        DataRequiredListener.getDataRequiredListener().onReferencesRequired(this, databaseObject);
    }

    protected Widget getErrorMessage(String msg){
        HTMLPanel message = new HTMLPanel(msg);
        message.addStyleName("elv-Details-OverviewDisclosure");
        message.addStyleName("elv-Details-OverviewDisclosure-empty");
        return message;
    }

    protected int getLevel(){
        if(parentPanel==null) return 1;
        int value = (this instanceof TransparentPanel)?0:1;
        return parentPanel.getLevel() + value;
    }

    protected int getLevel(DetailsPanel panel){
        @SuppressWarnings("NonJREEmulationClassesInClientCode")
        int aux = this.getClass().equals(panel.getClass())?1:0;
        if(parentPanel==null) return aux;
        return parentPanel.getLevel(panel) + aux;
    }

    protected DetailsPanel getParentPanel() {
        return parentPanel;
    }

    protected boolean isPanelInParentPanels(DetailsPanel panel){
        return this.equals(panel) || (getParentPanel() != null && getParentPanel().isPanelInParentPanels(panel));
    }

    protected boolean isLoaded() {
        return isLoaded;
    }

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }

    public void setReceivedData(DatabaseObject data){} //Not always will be override

    public void setReceivedAncestors(Ancestors ancestors){} //Not always will be override

    public void setReceivedReferences(List<LiteratureReference> literatureReferences) {} //Not always will be override

    @Override
    protected void initWidget(Widget widget) {
        super.initWidget(widget);

        if((this instanceof TransparentPanel)) return;

        addStyleName("elv-Details-OverviewDisclosure");
        if(getLevel()%2==0){
            addStyleName("elv-Details-OverviewDisclosure-even");
        }else{
            addStyleName("elv-Details-OverviewDisclosure-odd");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DetailsPanel that = (DetailsPanel) o;

        //noinspection RedundantIfStatement
        if (getDatabaseObject() != null ? !getDatabaseObject().equals(that.getDatabaseObject()) : that.getDatabaseObject() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getDatabaseObject() != null ? getDatabaseObject().hashCode() : 0;
    }
}