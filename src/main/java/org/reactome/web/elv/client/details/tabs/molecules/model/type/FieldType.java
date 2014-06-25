package org.reactome.web.elv.client.details.tabs.molecules.model.type;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
public enum FieldType implements Comparable<FieldType>{
    MOLECULE_TYPE("Type"),
    NAME("Name");

    private String fieldName;

    FieldType(String fieldName){
        this.fieldName = fieldName;
    }

    public String getFieldName(){
        return this.fieldName;
    }

    public static List<String> getListFields(){
        List<String> list = new ArrayList<String>();
        for(FieldType res : FieldType.values()){
            list.add(res.getFieldName());
        }
        return list;
    }

    @SuppressWarnings("UnusedDeclaration")
    public static FieldType getQueryType(String field){
        for(FieldType res : FieldType.values()){
            if(res.getFieldName().equals(field)) return res;
        }
        return null;
    }
}
