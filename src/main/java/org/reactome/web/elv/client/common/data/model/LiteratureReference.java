package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.FactoryUtils;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class LiteratureReference extends Publication {
    private String journal;
    private String pages;
    private Integer pubMedIdentifier;
    private Integer volume;
    private Integer year;

    public LiteratureReference(JSONObject jsonObject) {
        super(SchemaClass.LITERATURE_REFERENCE, jsonObject);

        if(jsonObject.containsKey("journal")){
            this.journal= FactoryUtils.getStringValue(jsonObject, "journal");
        }

        if(jsonObject.containsKey("pages")){
            this.pages = FactoryUtils.getStringValue(jsonObject, "pages");
        }

        if(jsonObject.containsKey("pubMedIdentifier")){
            this.pubMedIdentifier = FactoryUtils.getIntValue(jsonObject, "pubMedIdentifier");
        }

        if(jsonObject.containsKey("volume")){
            this.volume = FactoryUtils.getIntValue(jsonObject, "volume");
        }

        if(jsonObject.containsKey("year")){
            this.year = FactoryUtils.getIntValue(jsonObject, "year");
        }
    }

    public String getJournal() {
        return journal;
    }

    public String getPages() {
        return pages;
    }

    public Integer getPubMedIdentifier() {
        return pubMedIdentifier;
    }

    public Integer getVolume() {
        return volume;
    }

    public Integer getYear() {
        return year;
    }
}
