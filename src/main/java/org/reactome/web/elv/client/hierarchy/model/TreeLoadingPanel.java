package org.reactome.web.elv.client.hierarchy.model;

import com.google.gwt.dom.client.Style;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.elv.client.common.ReactomeImages;
import org.reactome.web.elv.client.common.data.model.Species;

/**
 * Shows a message depending on the action done over the hierarchy tree
 *
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class TreeLoadingPanel extends VerticalPanel {

    public TreeLoadingPanel() {
        this.showLoadingInfo(null);
    }

    public void showErrorMessage(String msg){
        this.setMessage(ReactomeImages.INSTANCE.exclamation(), msg);
    }

    public void showLoadingInfo(Species species){
        ImageResource ir = ReactomeImages.INSTANCE.loader();
        String msg;
        if(species==null){
            msg = "Waiting for the species list, please wait...";
        }else{
            msg = "Loading hierarchy for " + species.getDisplayName();
        }
        this.setMessage(ir, msg);
    }

    private void setMessage(ImageResource imageResource, String message){
        clear();

        HorizontalPanel hp = new HorizontalPanel();
        hp.setSpacing(10);
        hp.getElement().getStyle().setMarginBottom(15, Style.Unit.PX);
        hp.add(new Image(imageResource));
        hp.add(new Label(message));
        this.add(hp);
    }
}
