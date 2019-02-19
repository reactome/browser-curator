package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectUtils;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public class GO_MolecularFunction extends DatabaseObject {
    private String accession;
    private String definition;
    private ReferenceDatabase referenceDatabase;
    private org.reactome.web.pwp.client.common.model.classes.GO_MolecularFunction componentOf;
    private List<String> ecNumber;
    private List<String> name;
    private org.reactome.web.pwp.client.common.model.classes.GO_MolecularFunction negativelyRegulate;
    private org.reactome.web.pwp.client.common.model.classes.GO_MolecularFunction positivelyRegulate;
    private org.reactome.web.pwp.client.common.model.classes.GO_MolecularFunction regulate;


    public GO_MolecularFunction() {
        super(SchemaClass.GO_BIOLOGICAL_FUNCTION);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        if (jsonObject.containsKey("accession")) {
            this.accession = DatabaseObjectUtils.getStringValue(jsonObject, "accession");
        }

        if (jsonObject.containsKey("definition")) {
            this.definition = DatabaseObjectUtils.getStringValue(jsonObject, "definition");
        }

        if (jsonObject.containsKey("referenceDatabase")) {
            this.referenceDatabase = DatabaseObjectUtils.getDatabaseObject(jsonObject, "referenceDatabase");
        }

        if (jsonObject.containsKey("componentOf")) {
            this.componentOf = DatabaseObjectUtils.getDatabaseObject(jsonObject, "componentOf");
        }

        this.ecNumber = new LinkedList<>();
        for (String ecNumber : DatabaseObjectUtils.getStringList(jsonObject, "ecNumber")) {
            this.ecNumber.add(ecNumber);
        }

        this.name = new LinkedList<>();
        for (String name : DatabaseObjectUtils.getStringList(jsonObject, "name")) {
            this.name.add(name);
        }

        if (jsonObject.containsKey("negativelyRegulate")) {
            this.negativelyRegulate = DatabaseObjectUtils.getDatabaseObject(jsonObject, "negativelyRegulate");
        }

        if (jsonObject.containsKey("positivelyRegulate")) {
            this.positivelyRegulate = DatabaseObjectUtils.getDatabaseObject(jsonObject, "positivelyRegulate");
        }

        if (jsonObject.containsKey("regulate")) {
            this.regulate = DatabaseObjectUtils.getDatabaseObject(jsonObject, "regulate");
        }
    }

    public String getAccession() {
        return accession;
    }

    public String getDefinition() {
        return definition;
    }

    public ReferenceDatabase getReferenceDatabase() {
        return referenceDatabase;
    }

    public org.reactome.web.pwp.client.common.model.classes.GO_MolecularFunction getComponentOf() {
        return componentOf;
    }

    public List<String> getEcNumber() {
        return ecNumber;
    }

    public List<String> getName() {
        return name;
    }

    public org.reactome.web.pwp.client.common.model.classes.GO_MolecularFunction getNegativelyRegulate() {
        return negativelyRegulate;
    }

    public org.reactome.web.pwp.client.common.model.classes.GO_MolecularFunction getPositivelyRegulate() {
        return positivelyRegulate;
    }

    public org.reactome.web.pwp.client.common.model.classes.GO_MolecularFunction getRegulate() {
        return regulate;
    }
}
