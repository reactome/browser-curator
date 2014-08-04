package org.reactome.web.elv.client.manager.state;

import java.util.Arrays;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public enum AdvancedStateKey {
    REACT       ("REACT"),
    SPECIES     ("SPECIES" , "FOCUS_SPECIES_ID"),
    DIAGRAM     ("DIAGRAM", "FOCUS_PATHWAY_ID"),
    INSTANCE    ("ID"),
    PATH        ("PATH"),
    DETAILS_TAB ("DTAB", "DETAILS_TAB"),
    TOOL        ("TOOL"),
    ANALYSIS    ("ANALYSIS", "ANALYSIS_ID");

    List<String> keys;

    private AdvancedStateKey(String... keys) {
        this.keys = Arrays.asList(keys);
    }

    public static AdvancedStateKey getAdvancedStateKey(String key){
        for (AdvancedStateKey advancedStateKey : values()) {
            if(advancedStateKey.keys.contains(key)){
                return advancedStateKey;
            }
        }
        return null;
    }

    public String getDefaultKey() {
        return keys.get(0);
    }
}
