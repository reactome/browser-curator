package org.reactome.web.elv.client.details.tabs.molecules.model.type;

/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
public enum PropertyType {

    PROTEINS("Proteins"),
    CHEMICAL_COMPOUNDS("Chemical Compounds"),
    SEQUENCES("DNA/RNA"),
    OTHERS("Others"),
    DOWNLOAD("Download");

    private String title;

    PropertyType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public static PropertyType getPropertyType(String type){
        for (PropertyType pt : values()) {
            if(pt.title.equals(type)){
                return pt;
            }
        }
        return null;
    }

}