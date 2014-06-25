package org.reactome.web.elv.client.details.model.widgets;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;
import org.reactome.web.elv.client.common.data.model.DatabaseIdentifier;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.widgets.disclosure.DisclosurePanelFactory;
import org.reactome.web.elv.client.details.events.MoleculeSelectedListener;
import org.reactome.web.elv.client.details.tabs.molecules.model.data.Molecule;
import org.reactome.web.elv.client.details.tabs.molecules.model.data.PhysicalToReferenceEntityMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
public class MoleculePanel extends DetailsPanel implements OpenHandler<DisclosurePanel>, ClickHandler {
    //private Molecule referenceEntity;
    private Molecule molecule;
    private DisclosurePanel disclosurePanel;
    private String title;
    Set<PhysicalToReferenceEntityMap> physicalEntities;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MoleculePanel(Molecule molecule, Set<PhysicalToReferenceEntityMap> physicalEntities) {
        this(null, molecule, physicalEntities);
    }

    public MoleculePanel(DetailsPanel parentPanel, Molecule molecule, Set<PhysicalToReferenceEntityMap> physicalEntities) {
        super(parentPanel);
        this.molecule = molecule;
        this.physicalEntities = physicalEntities;
        initialize();
    }

    private void initialize(){
        FlowPanel overview = new FlowPanel();
        overview.setTitle("Show further information about external references");
        this.setTitle(molecule.getDbId().toString());

        //Name of reference.
        List<String> refNames = this.molecule.getName();
        String refName;
        if(refNames == null || refNames.size() == 0){
            refName = this.molecule.getDisplayName();
        }else{
            refName = refNames.get(0);
        }
        refName = refName + " (" + molecule.getOccurrenceInPathway() + "x)";
        TextPanel name = new TextPanel(refName);
        name.setTitle("refDbID, Name, Occurrences in Pathway");

        //Link to external reference database.
        //String referenceDB = this.referenceEntity.getReferenceDatabase().getDisplayName();
        String identifier  = this.molecule.getIdentifier();
        //String string = this.referenceEntity.getReferenceDatabase().getUrl(); => NULL
        Anchor ref = new Anchor(identifier, this.molecule.getUrl(), "_blank");
        ref.setTitle("Link to main external reference DB");
        ref.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                Window.open(molecule.getUrl(), "_blank", "");
            }
        });

        overview.insert(ref, 0); //First adding ref because length of name differs enormously!!!
        ref.asWidget().getElement().getStyle().setFloat(Style.Float.LEFT);
        ref.asWidget().getElement().getStyle().setColor("blue");
        ref.addStyleName("elv-Details-Reference-MoleculesRow");
        overview.insert(name, 1);
        //name.asWidget().getElement().getStyle().setFloat(Style.Float.LEFT);
        name.addStyleName("elv-Details-Name-MoleculesRow");

        overview.setHeight("99%");

        this.disclosurePanel = DisclosurePanelFactory.getAdvancedDisclosurePanel(overview, this);
        this.disclosurePanel.addOpenHandler(this);
        initWidget(this.disclosurePanel);
    }

    @Override
    public DatabaseObject getDatabaseObject() {
        return this.molecule;
    }

    @Override
    public void onOpen(OpenEvent<DisclosurePanel> event) {
        if(!isLoaded()){
            moleculeDataRequired(this.molecule);
        }
    }

    @Override
    public void setReceivedMoleculeData(Molecule data){
        this.molecule = this.molecule.addData(data);

        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("98%");
        vp.addStyleName("elv-Details-OverviewDisclosure-content");

        //Building up the tree for External cross-references.
        if(!this.molecule.getCrossReference().isEmpty()){
            Tree referencesTree = new Tree();
            TreeItem references = new TreeItem(SafeHtmlUtils.fromString("External cross-references"));
            for (DatabaseIdentifier databaseIdentifier : this.molecule.getCrossReference()) {
                DatabaseIdentifierPanel dbIdPanel = new DatabaseIdentifierPanel(databaseIdentifier);
                TreeItem reference = dbIdPanel.asTreeItem();
                references.addItem(reference);
                reference.setState(true, false);
            }
            references.setState(true);
            referencesTree.clear();
            referencesTree.addItem(references);
            vp.add(referencesTree);
        }

        if(vp.getWidgetCount()==0){
            vp.add(getErrorMessage("There is not more info available"));
        }

        this.disclosurePanel.setContent(vp);

        setLoaded(true);
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        List<PhysicalToReferenceEntityMap> select = new ArrayList<PhysicalToReferenceEntityMap>();
        List<PhysicalToReferenceEntityMap> highlight = new ArrayList<PhysicalToReferenceEntityMap>();
        for(PhysicalToReferenceEntityMap phyEntity : physicalEntities){
            //DatabaseObject databaseObj = phyEntity;

            if(phyEntity.getSchemaClass() != SchemaClass.COMPLEX && phyEntity.getSchemaClass() != SchemaClass.CANDIDATE_SET
                    && phyEntity.getSchemaClass() != SchemaClass.DEFINED_SET && phyEntity.getSchemaClass() != SchemaClass.ENTITY_SET
                    && phyEntity.getSchemaClass() != SchemaClass.OPEN_SET){

                    select.add(0, phyEntity);

            }else{
                highlight.add(phyEntity);
            }

        }

        select.addAll(highlight);
        clickEvent.stopPropagation();
        MoleculeSelectedListener.getMoleculeSelectedListener().moleculeSelected(select);
    }
}
