package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.FactoryUtils;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

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
    private GO_MolecularFunction componentOf;
    private List<String> ecNumber = new LinkedList<String>();
    private List<String> name = new LinkedList<String>();
    private GO_MolecularFunction negativelyRegulate;
    private GO_MolecularFunction positivelyRegulate;
    private GO_MolecularFunction regulate;


    public GO_MolecularFunction(JSONObject jsonObject) {
        super(SchemaClass.GO_BIOLOGICAL_FUNCTION, jsonObject);

        if(jsonObject.containsKey("accession")){
            this.accession = FactoryUtils.getStringValue(jsonObject, "accession");
        }

        if(jsonObject.containsKey("definition")){
            this.definition = FactoryUtils.getStringValue(jsonObject, "definition");
        }

        if(jsonObject.containsKey("referenceDatabase")){
            this.referenceDatabase= (ReferenceDatabase) FactoryUtils.getDatabaseObject(jsonObject, "referenceDatabase");
        }

        if(jsonObject.containsKey("componentOf")){
            this.componentOf = (GO_MolecularFunction) FactoryUtils.getDatabaseObject(jsonObject, "componentOf");
        }

        for (String ecNumber: FactoryUtils.getStringList(jsonObject, "ecNumber")) {
            this.ecNumber.add(ecNumber);
        }

        for (String name : FactoryUtils.getStringList(jsonObject, "name")) {
            this.name.add(name);
        }

        if(jsonObject.containsKey("negativelyRegulate")){
            this.negativelyRegulate = (GO_MolecularFunction) FactoryUtils.getDatabaseObject(jsonObject, "negativelyRegulate");
        }

        if(jsonObject.containsKey("positivelyRegulate")){
            this.positivelyRegulate = (GO_MolecularFunction) FactoryUtils.getDatabaseObject(jsonObject, "positivelyRegulate");
        }

        if(jsonObject.containsKey("regulate")){
            this.regulate = (GO_MolecularFunction) FactoryUtils.getDatabaseObject(jsonObject, "regulate");
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

    public GO_MolecularFunction getComponentOf() {
        return componentOf;
    }

    public List<String> getEcNumber() {
        return ecNumber;
    }

    public List<String> getName() {
        return name;
    }

    public GO_MolecularFunction getNegativelyRegulate() {
        return negativelyRegulate;
    }

    public GO_MolecularFunction getPositivelyRegulate() {
        return positivelyRegulate;
    }

    public GO_MolecularFunction getRegulate() {
        return regulate;
    }
}
