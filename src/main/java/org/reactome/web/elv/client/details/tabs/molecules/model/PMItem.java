package org.reactome.web.elv.client.details.tabs.molecules.model;

import com.google.gwt.dom.client.Style;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.elv.client.common.ReactomeImages;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class PMItem extends TreeItem {

    public static enum LEVEL {
        MOLECULE_TYPE,
        MOLECULE,
        REFERENCE,
        SUB_REFERENCE
    }

    private JSONArray children;

    private LEVEL level;

    public PMItem(JSONObject json, LEVEL level) {
        this.level = level;
        setChildren(json);

        setWidget(getLevelContent(level, json));
        switch (level){
            case MOLECULE_TYPE:
            case MOLECULE:
                if(this.children.size()>0) addLoadingMessage();
                break;
            default:
                if(this.children.size()>1) addLoadingMessage();
        }
    }

    private void addLoadingMessage(){
        HorizontalPanel loaderMsg = new HorizontalPanel();
        loaderMsg.add(new Image(ReactomeImages.INSTANCE.loader()));
        Label loadingLabel = new Label("Loading...");
        loadingLabel.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        loaderMsg.add(loadingLabel);
        addItem(loaderMsg); // Add a place holder so that this item can be opened.
    }

    public void expand() {
        if(children==null) return;
        removeItems();
        List<PMItem> children = getLevelChildren();
        for (PMItem pmItem : children) {
            addItem(pmItem);
        }
        this.children = null;
    }

    private List<PMItem> getLevelChildren(){
        List<PMItem> children = new ArrayList<PMItem>();
        switch (this.level){
            case MOLECULE_TYPE:
                for(int i=0; i< this.children.size(); ++i){
                    PMItem item = new PMItem(this.children.get(i).isObject(), LEVEL.MOLECULE);
                    children.add(item);
                }
                break;
            case MOLECULE:
                for(int i=0; i< this.children.size(); ++i){
                    PMItem item = new PMItem(this.children.get(i).isObject(), LEVEL.REFERENCE);
                    children.add(item);
                }
                break;
            case REFERENCE:
                for(int i=0; i< this.children.size(); ++i){
                    PMItem item = new PMItem(this.children.get(i).isObject(), LEVEL.SUB_REFERENCE);
                    children.add(item);
                }
                break;
        }
        return children;
    }

    private Widget getLevelContent(LEVEL level, JSONObject json){
        HorizontalPanel itemContent = new HorizontalPanel();
        String name = json.get("name").isString().stringValue();
        int size = this.children.size();
        Label label = null;
        HTMLPanel molecule = null;
        switch (level){
            case MOLECULE_TYPE:
                name += size>0 ? " (" + size + " molecules)" : "";
                label = new Label(name);
                break;
            case MOLECULE:
                name += size>0 ? " (" + size + " external references)" : "";
                label = new Label(name);
                break;
            case REFERENCE:
                if(size>1){
                    name += " (" + size + " external references)";
                }else{
                    name += " >> ";
                    molecule = getMolecule(json.get("list").isArray().get(0).isObject());
                }
                label = new Label(name);
                break;
            case SUB_REFERENCE:
                molecule = getMolecule(json);
                break;
        }

        if(label!=null) itemContent.add(label);
        if(molecule!=null) itemContent.add(molecule);

        return itemContent;
    }

    /**
     *
     * @param json JSON object with the molecule data
     * @return a String with the raw HTML
     */
    private HTMLPanel getMolecule(JSONObject json){
        String name = json.get("name").isString().stringValue();
        String id = json.get("id").isString().stringValue();
        String url = json.get("url").isString().stringValue();

        StringBuilder builder = new StringBuilder("&nbsp;&nbsp;<span class=\"");
        builder.append(name.replaceAll(" ",""));
        builder.append("\">");
        builder.append(name.substring(0, 1));
        builder.append("</span>");
        builder.append("&nbsp;&nbsp;[<a title=\"Go to ");
        builder.append(name);
        builder.append(": ");
        builder.append(id);
        builder.append("\" target=\"_blank\" href=\"");
        builder.append(url);
        builder.append("\">");
        builder.append(id);
        builder.append("</a>]");

        return new HTMLPanel(builder.toString());
    }

    private void setChildren(JSONObject json){
        switch (this.level){
            case MOLECULE_TYPE:
                children = json.containsKey("molecules") ? json.get("molecules").isArray() : new JSONArray();
                break;
            case MOLECULE:
                children = json.containsKey("references") ? json.get("references").isArray() : new JSONArray();
                break;
            case REFERENCE:
            case SUB_REFERENCE:
                children = json.containsKey("list") ? json.get("list").isArray() : new JSONArray();
                break;
            default:
                children = new JSONArray();
        }
    }
}