package org.reactome.web.elv.client.center.model;

import org.reactome.web.elv.client.common.LocationHelper;

import java.util.LinkedList;
import java.util.List;

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
        for (CenterToolType centerToolType : values(true)) {
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
        for (CenterToolType centerToolType : values()) {  //This is not location dependent!
            if(centerToolType.equals(type))
                return index;
            index++;
        }
        return CenterToolType.getDefaultIndex();
    }

    public String getTitle() {
        return title;
    }

    public static List<CenterToolType> values(boolean location){
        List<CenterToolType> rtn = new LinkedList<CenterToolType>();
        for (CenterToolType centerToolType : values()) {
            if(centerToolType.equals(ANALYSIS) && location){
                if(LocationHelper.isAnalysisAvailable()) {
                    rtn.add(centerToolType);
                }
            }else{
                rtn.add(centerToolType);
            }
        }
        return rtn;
    }
}
