package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectUtils;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("unused")
public class LiteratureReference extends Publication {
    private String journal;
    private String pages;
    private Integer pubMedIdentifier;
    private Integer volume;
    private Integer year;

    public LiteratureReference() {
        super(SchemaClass.LITERATURE_REFERENCE);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        if (jsonObject.containsKey("journal")) {
            this.journal = DatabaseObjectUtils.getStringValue(jsonObject, "journal");
        }

        if (jsonObject.containsKey("pages")) {
            this.pages = DatabaseObjectUtils.getStringValue(jsonObject, "pages");
        }

        if (jsonObject.containsKey("pubMedIdentifier")) {
            this.pubMedIdentifier = DatabaseObjectUtils.getIntValue(jsonObject, "pubMedIdentifier");
        }

        if (jsonObject.containsKey("volume")) {
            this.volume = DatabaseObjectUtils.getIntValue(jsonObject, "volume");
        }

        if (jsonObject.containsKey("year")) {
            this.year = DatabaseObjectUtils.getIntValue(jsonObject, "year");
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
