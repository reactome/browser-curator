package org.reactome.web.elv.client.popups;

import com.google.gwt.user.client.ui.ToggleButton;
import org.reactome.web.elv.client.common.Controller;
import org.reactome.web.elv.client.common.EventBus;
import org.reactome.web.elv.client.common.events.ELVEventType;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class PopupManager extends Controller {

    DiagramKey diagramKey;

    public PopupManager(EventBus eventBus) {
        super(eventBus);
    }

    @Override
    public void onTopBarDiagramKeyButtonToggled(ToggleButton btn) {
        if(diagramKey==null)
            diagramKey = new DiagramKey(this);

        if(btn.isDown()){
            diagramKey.show();
            diagramKeyOpened();
        }else{
            diagramKey.hide();
        }

    }

    public void closeDiagramKey(){
        diagramKey.hide();
    }

    public void diagramKeyClosed(){
        this.eventBus.fireELVEvent(ELVEventType.DIAGRAM_KEY_CLOSED, null);
    }

    public void diagramKeyOpened(){
        this.eventBus.fireELVEvent(ELVEventType.DIAGRAM_KEY_OPENED, null);
    }
}
