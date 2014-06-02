package org.reactome.web.elv.client.details.model;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public enum DetailsTabType  {

    /* NOTE: If a new tab is added, make sure GAModule correspondence method is updated */
    OVERVIEW("OT", "Overview"),
    PARTICIPATING_MOLECULES("MT", "Molecules"),
    STRUCTURES("ST", "Structures"),
    EXPRESSION("EX", "Expression"),
    ANALYSIS("AN", "Analysis"),
    PARTICIPATING_PROCESSES("PT", "Processes"),
    DOWNLOADS("DT", "Downloads");

    private String code;
    private String title;

    DetailsTabType(String code, String title){
        this.code = code;
        this.title = title;
    }

    public static DetailsTabType getByCode(String code){
        for (DetailsTabType tabType : values()) {
            if(tabType.getCode().equals(code))
                return tabType;
        }
        return OVERVIEW;
    }

    public String getCode() {
        return code;
    }

    public static DetailsTabType getDefault(){
        return OVERVIEW;
    }

    public static int getDefaultIndex(){
        return getIndex(getDefault());
    }

    public static int getIndex(DetailsTabType type){
        int index = 0;
        for (DetailsTabType tabType : values()) {
            if(tabType.equals(type))
                return index;
            index++;
        }
        return DetailsTabType.getDefaultIndex();
    }

    public String getTitle() {
        return title;
    }
}