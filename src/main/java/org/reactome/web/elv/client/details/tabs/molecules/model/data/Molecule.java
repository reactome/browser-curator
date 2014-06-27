package org.reactome.web.elv.client.details.tabs.molecules.model.data;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.common.data.model.ReferenceEntity;

import java.util.List;

/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */

public class Molecule extends ReferenceEntity implements Comparable<Molecule>{
    private SchemaClass schemaClass;
    private String url;
    private boolean toHighlight;
    private int occurrenceInPathway = 0;

    public Molecule(SchemaClass schemaClass, JSONObject jsonObject) {
        super(schemaClass, jsonObject);
        toHighlight = false;
        if(jsonObject.isObject().containsKey("url")){
            url = jsonObject.isObject().get("url").toString();
            url = url.substring(1, url.length()-1);
        }

        switch (schemaClass){
            case REFERENCE_GENE_PRODUCT:
            case REFERENCE_ISOFORM:
                this.schemaClass = SchemaClass.getSchemaClass(SchemaClass.ENTITY_WITH_ACCESSIONED_SEQUENCE.schemaClass);
                break;
            case REFERENCE_MOLECULE:
                this.schemaClass = SchemaClass.getSchemaClass(SchemaClass.SIMPLE_ENTITY.schemaClass);
                break;
            default:
                this.schemaClass = schemaClass;
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isToHighlight() {
        return toHighlight;
    }

    public void setToHighlight(boolean toHighlight) {
        this.toHighlight = toHighlight;
    }

    public int getOccurrenceInPathway() {
        return occurrenceInPathway;
    }

    public void setOccurrenceInPathway(int occurrence) {
        this.occurrenceInPathway = occurrence;
    }

    public SchemaClass getSchemaClass() {
        return schemaClass;
    }


    @Override
    public int compareTo(Molecule o) {
        List<String> names1 = this.getName();
        List<String> names2 = o.getName();
        int value;
        if(names1 != null && names2 != null && names1.size() > 0 && names2.size() > 0){
            value = names1.get(0).toUpperCase().compareTo(names2.get(0).toUpperCase());
        }else if(names1 != null && names1.size() > 0){
            value = names1.get(0).toUpperCase().compareTo(o.getDisplayName().toUpperCase());
        }else if(names2 != null && names2.size() > 0){
            value = this.getDisplayName().toUpperCase().compareTo(names2.get(0).toUpperCase());
        }else{
            value = this.getDisplayName().toUpperCase().compareTo(o.getDisplayName().toUpperCase());
        }
        return value;
    }

    public Molecule addData(DatabaseObject data) {
        ReferenceEntity referenceEntity = (ReferenceEntity) data;
        Molecule molecule = (Molecule) referenceEntity;
        molecule.setToHighlight(this.isToHighlight());
        molecule.setUrl(this.getUrl());
        return molecule;
    }
}
