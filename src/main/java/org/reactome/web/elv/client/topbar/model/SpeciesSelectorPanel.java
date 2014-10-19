package org.reactome.web.elv.client.topbar.model;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import org.reactome.web.elv.client.common.data.model.Species;
import org.reactome.web.elv.client.common.utils.Console;
import org.reactome.web.elv.client.topbar.view.TopBarView.Presenter;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class SpeciesSelectorPanel extends HorizontalPanel {

    private ListBox species;

    private List<Species> speciesList;

    public SpeciesSelectorPanel(final Presenter presenter) {
        super();

        add(new Label("Pathways for:"));

        species = new ListBox(false);
        species.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        species.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                Species speciesSelected = null;

                int index = species.getSelectedIndex();
                if(index>-1){
                    String value = species.getValue(index);
                    for (Species aux : speciesList) {
                        if(aux.getDbId().toString().equals(value)){
                            speciesSelected = aux;
                        }
                    }
                }

                if(speciesSelected!=null)
                    presenter.speciesSelected(speciesSelected);
            }
        });
        add(species);
    }

    public void setSpeciesList(List<Species> speciesList) {
        this.speciesList = speciesList;
        for (Species species : speciesList) {
            this.species.addItem(species.getDisplayName(), species.getDbId().toString());
        }
    }

    public void selectSpecies(Species species) throws Exception {
        Long speciesId = species.getDbId();
        int index = -1;
        for(int i=0; i<this.speciesList.size(); i++){
            Species aux = this.speciesList.get(i);
            if(aux.getDbId().equals(speciesId)){
                index = i;
            }
        }
        if(index!=-1){
            this.species.setSelectedIndex(index);
        }else{
            Console.error(getClass() + " selectSpecies >> index == -1");
            throw new Exception(getClass() + " No species found, index for selectSpecies == -1");
        }
    }
}
