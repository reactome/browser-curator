package org.reactome.web.elv.client.manager.state;

import com.google.gwt.user.client.Window;
import org.reactome.web.elv.client.common.Controller;
import org.reactome.web.elv.client.common.EventBus;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.Event;
import org.reactome.web.elv.client.common.data.model.Pathway;
import org.reactome.web.elv.client.common.data.model.Species;
import org.reactome.web.elv.client.details.model.DetailsTabType;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class TitleManager extends Controller {
    private String initialTitle = "";
    private Species species;
    private Pathway pathway;
    private DatabaseObject databaseObject;
    private DetailsTabType detailsTabType;

    public TitleManager(EventBus eventBus) {
        super(eventBus);
        initialize();
    }

    private void initialize(){
        this.species = null;
        this.pathway = null;
        this.databaseObject = null;
        this.detailsTabType = DetailsTabType.getDefault();
    }

    @Override
    public void onELVReady() {
        this.initialTitle = Window.getTitle();
    }

    @Override
    public void onDetailsPanelTabChanged(DetailsTabType tabType) {
        this.detailsTabType = tabType;
        this.updateName();
    }

    @Override
    public void onStateManagerDetailsTabSelected(DetailsTabType tab) {
        this.detailsTabType = tab;
        this.updateName();
    }

    @Override
    public void onStateManagerSpeciesSelected(Species species) {
        this.species = species;
        this.updateName();
    }

    @Override
    public void onStateManagerInstancesInitialStateReached() {
        initialize();
        Window.setTitle(this.initialTitle);
    }

    @Override
    public void onStateManagerDatabaseObjectsSelected(List<Event> path, Pathway pathway, DatabaseObject databaseObject) {
        this.pathway = pathway;
        if(pathway.equals(databaseObject)){
            this.databaseObject = null;
        }else{
            this.databaseObject = databaseObject;
        }
        this.updateName();
    }

    private void updateName(){
        StringBuilder sb = new StringBuilder("PB | ");
        if(this.pathway==null && this.databaseObject==null){
            return;
        }
        if(databaseObject==null) {
            sb.append(this.pathway.getDisplayName());
        }else{
            sb.append(this.databaseObject.getDisplayName());
        }
        if(this.species!=null && !this.species.getDbId().equals(AdvancedState.DEFAULT_SPECIES_ID)){
            sb.append(" [");
            sb.append(species.getDisplayName());
            sb.append("]");
        }
        if(!this.detailsTabType.equals(DetailsTabType.getDefault())){
            sb.append(" - ");
            sb.append(this.detailsTabType.getTitle());
        }
        Window.setTitle(sb.toString());
    }
}