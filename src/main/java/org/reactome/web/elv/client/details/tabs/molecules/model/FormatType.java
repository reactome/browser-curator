package org.reactome.web.elv.client.details.tabs.molecules.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public enum FormatType implements Comparable<FormatType>{

    CSV("CSV", "csv", true),
    TSV("TSV", "tsv", true),
    XML("XML", "xml", true),
    EXCEL("Excel", "xls", false);

    private String name;
    private String extension;
    private boolean isViewable;

    FormatType(String name, String extension, boolean isViewable){
        this.name = name;
        this.extension = extension;
        this.isViewable = isViewable;
    }

    @SuppressWarnings("UnusedDeclaration")
    public String getExtension(){
        return this.extension;
    }

    public String getFormatName(){
        return this.name;
    }

    public static FormatType getFormatType(String format){
        for(FormatType res : FormatType.values()){
            if(res.getFormatName().equals(format)) return res;
        }
        return null;
    }

    public static List<String> getListFormats(){
        List<String> list = new ArrayList<String>();
        for(FormatType res : FormatType.values()){
            list.add(res.getFormatName());
        }
        return list;
    }

    public boolean isViewable(){
        return this.isViewable;
    }
}