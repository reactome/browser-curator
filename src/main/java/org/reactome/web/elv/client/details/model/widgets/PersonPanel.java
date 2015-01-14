package org.reactome.web.elv.client.details.model.widgets;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.elv.client.common.ReactomeImages;
import org.reactome.web.elv.client.common.data.model.DatabaseIdentifier;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.LiteratureReference;
import org.reactome.web.elv.client.common.data.model.Person;
import org.reactome.web.elv.client.common.widgets.disclosure.DisclosurePanelFactory;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class PersonPanel extends DetailsPanel implements OpenHandler<DisclosurePanel>, TransparentPanel {
    private Person person;
    private DisclosurePanel disclosurePanel;

    @SuppressWarnings("UnusedDeclaration")
    public PersonPanel(Person person) {
        this(null, person);
    }

    public PersonPanel(DetailsPanel parentPanel, Person person) {
        super(parentPanel);
        this.person = person;
        initialize();
    }

    private void initialize(){
        disclosurePanel = DisclosurePanelFactory.getDisclosurePanel(person.getDisplayName());
        disclosurePanel.addOpenHandler(this);
        disclosurePanel.setWidth("99%");
        initWidget(disclosurePanel);
    }

    @Override
    public void onOpen(OpenEvent<DisclosurePanel> event) {
        if(!isLoaded()){
            dataRequired(this.person);
        }
    }

    @Override
    public DatabaseObject getDatabaseObject() {
        return person;
    }

    @Override
    public void setReceivedData(DatabaseObject data) {
        this.person = (Person) data;
        referencesRequired(this.person);
    }

    @Override
    public void setReceivedReferences(List<LiteratureReference> literatureReferences) {
        setLoaded(true);

        VerticalPanel vp = new VerticalPanel();
        vp.addStyleName("elv-Details-OverviewDisclosure-content");
        vp.setWidth("100%");

        vp.add(getPositionBasedTitle());
        for (LiteratureReference literatureReference : literatureReferences) {
            PublicationPanel lrp = new PublicationPanel(this, literatureReference);
            lrp.setWidth("99%");
            if(!isPanelInParentPanels(lrp))
                vp.add(lrp);
        }

        VerticalPanel content = new VerticalPanel();
        for (DatabaseIdentifier databaseIdentifier : person.getCrossReference()) {
            content.add(getDatabaseIdentifier(databaseIdentifier));
        }
        content.add((vp.getWidgetCount()>1)? vp : getNoReferencesMessage());
        disclosurePanel.setContent(content);
    }

    private Widget getNoReferencesMessage(){
        HTMLPanel message;
        if(getLevel(this)==1)
            message = new HTMLPanel("No other references");
        else
            message = new HTMLPanel("No more references for this author in Reactome database");
        message.addStyleName("elv-Details-OverviewDisclosure");
        message.addStyleName("elv-Details-OverviewDisclosure-empty");
        return message;
    }

    private Widget getPositionBasedTitle(){
        /* TITLE SELECTOR based in the position of this Person Panel */
        String title;
        if(getParentPanel() instanceof PublicationPanel)
            title = "Other author Publication(s) in Reactome database:";
        else
        if(getLevel(this)==1)
            title = "Author Publication(s) in Reactome database:";
        else
            title = "Other author Publication(s) in Reactome database:";

        return new HTMLPanel(title);
    }

    //We do NOT use the DatabaseIdentifierPanel here because we want a different look and feel
    public Widget getDatabaseIdentifier(DatabaseIdentifier databaseIdentifier){
        String[] aux = databaseIdentifier.getDisplayName().split(":");
        HorizontalPanel hp = new HorizontalPanel();
        if(aux[0].equals("ORCID")){
            Image orcid = new Image(ReactomeImages.INSTANCE.orcid());
            orcid.getElement().getStyle().setMarginRight(5, Style.Unit.PX);
            orcid.setTitle("ORCID identifier");
            hp.add(orcid);
        }
        hp.add(new Label(aux[0] + ":"));

        Anchor a = new Anchor(aux[1], databaseIdentifier.getUrl(), "_blank");
        a.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        hp.add(a);
        hp.getElement().getStyle().setMarginBottom(5, Style.Unit.PX);
        return hp;
    }
}
