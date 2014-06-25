package org.reactome.web.elv.client.details.tabs.molecules.model.widget;

import com.google.gwt.user.client.ui.*;
import org.reactome.web.elv.client.common.widgets.disclosure.DisclosureImages;
import org.reactome.web.elv.client.details.model.widgets.MoleculePanel;
import org.reactome.web.elv.client.details.tabs.molecules.model.data.Molecule;
import org.reactome.web.elv.client.details.tabs.molecules.model.data.PhysicalToReferenceEntityMap;
import org.reactome.web.elv.client.details.tabs.molecules.model.data.Result;

import java.util.ArrayList;
import java.util.Set;

/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
public class MoleculesTable implements IsWidget  {
    private VerticalPanel vp;
    private Result result;

    public MoleculesTable(Result result) {
        this.result = result;
        vp = new VerticalPanel();
        vp.setWidth("99%");
        vp.add(getLoadingMessage());
    }

    public void setMoleculesData(ArrayList<Molecule> molecules){
        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("99%");
        for (Molecule molecule : molecules) {
            Set<PhysicalToReferenceEntityMap> phyEntities = getPhysicalEntities(molecule);
            Widget widget = new MoleculePanel(molecule, phyEntities);
            if(!molecule.isToHighlight()){
                widget.addStyleName("elv-Details-MoleculesRow-undoHighlight");
            }
            vp.add(widget);
        }
        this.vp.clear();
        this.vp = vp;
    }

    private Set<PhysicalToReferenceEntityMap> getPhysicalEntities(Molecule molecule) {
        return this.result.getPhyEntityToRefEntitySet().getKeys(molecule);
    }

    @Override
    public Widget asWidget() {
        return vp;
    }

    public static Widget getLoadingMessage(){
        HorizontalPanel hp = new HorizontalPanel();
        hp.add(new Image(DisclosureImages.INSTANCE.getLoadingImage()));
        hp.add(new HTMLPanel("Loading..."));
        hp.setSpacing(5);

        return hp;
    }
}
