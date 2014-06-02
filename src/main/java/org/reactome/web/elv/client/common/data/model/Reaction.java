package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.FactoryUtils;
import org.reactome.web.elv.client.common.data.factory.ModelFactory;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public class Reaction extends ReactionLikeEvent {

    private Reaction reverseReaction;
    private String totalProt;
    private String maxHomologues;
    private String inferredProt;
    private List<Regulation> regulation = new LinkedList<Regulation>();

    public Reaction(JSONObject jsonObject) {
        super(SchemaClass.REACTION, jsonObject);

        if(jsonObject.containsKey("reverseReaction")){
            this.reverseReaction = (Reaction) FactoryUtils.getDatabaseObject(jsonObject, "reverseReaction");
        }

        if(jsonObject.containsKey("totalProt")){
            this.totalProt = FactoryUtils.getStringValue(jsonObject, "totalProt");
        }

        if(jsonObject.containsKey("maxHomologues")){
            this.maxHomologues = FactoryUtils.getStringValue(jsonObject, "maxHomologues");
        }

        if(jsonObject.containsKey("inferredProt")){
            this.inferredProt = FactoryUtils.getStringValue(jsonObject, "inferredProt");
        }

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "regulation")) {
            this.regulation.add((Regulation) ModelFactory.getDatabaseObject(object));
        }
    }

    /****************************** NEXT TWO METHODS NOT AUTO-GENERATED ******************************/
    public List<PositiveRegulation> getPositiveRegulation(){
        List<PositiveRegulation> pr = new LinkedList<PositiveRegulation>();
        for (Regulation r : regulation) {
            if(r instanceof PositiveRegulation){
                pr.add((PositiveRegulation) r);
            }
        }
        return pr;
    }

    public List<NegativeRegulation> getNegativeRegulation(){
        List<NegativeRegulation> nr = new LinkedList<NegativeRegulation>();
        for (Regulation r : regulation) {
            if(r instanceof NegativeRegulation){
                nr.add((NegativeRegulation) r);
            }
        }
        return nr;
    }
    /*************************************************************************************************/

    public Reaction getReverseReaction() {
        return reverseReaction;
    }

    public String getTotalProt() {
        return totalProt;
    }

    public String getMaxHomologues() {
        return maxHomologues;
    }

    public String getInferredProt() {
        return inferredProt;
    }

    public List<Regulation> getRegulation() {
        return regulation;
    }
}
