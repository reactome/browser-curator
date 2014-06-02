package org.reactome.web.elv.client.center.model;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public enum CenterToolType {

    DIAGRAM("DM", "Diagram"),
    ANALYSIS("AT", "Analysis Tool");

    private String code;
    private String title;

    private CenterToolType(String code, String title) {
        this.code = code;
        this.title = title;
    }

    public static CenterToolType getByCode(String code){
        for (CenterToolType centerToolType : values()) {
            if(centerToolType.getCode().equals(code)){
                return centerToolType;
            }
        }
        return getDefault();
    }

    public String getCode() {
        return code;
    }

    public static CenterToolType getDefault(){
        return DIAGRAM;
    }

    public static int getDefaultIndex(){
        return getIndex(getDefault());
    }

    public static int getIndex(CenterToolType type){
        int index = 0;
        for (CenterToolType centerToolType : values()) {
            if(centerToolType.equals(type))
                return index;
            index++;
        }
        return CenterToolType.getDefaultIndex();
    }

    public String getTitle() {
        return title;
    }
}
