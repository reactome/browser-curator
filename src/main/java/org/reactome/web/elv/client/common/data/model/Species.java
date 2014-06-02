package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.FactoryUtils;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 *
 */
@SuppressWarnings("UnusedDeclaration")
public class Species extends Taxon {

    private List<Figure> figure = new LinkedList<Figure>();
    /*private int speciesRank;
    private int species;
    private String speciesClass;*/

    public Species(JSONObject jsonObject) {
        super(SchemaClass.SPECIES, jsonObject);

        for (JSONObject value : FactoryUtils.getObjectList(jsonObject, "figure")) {
            this.figure.add(new Figure(value));
        }
        /*this.speciesRank = getIntValue(jsonObject, "speciesRank");
        this.species = getIntValue(jsonObject, "species");
        this.speciesClass = getStringValue(jsonObject, "speciesClass");*/
    }

    public List<Figure> getFigure() {
        return figure;
    }

    /*
    public int getSpeciesRank() {
        return speciesRank;
    }

    public int getSpecies() {
        return species;
    }

    public String getSpeciesClass() {
        return speciesClass;
    }*/
}
