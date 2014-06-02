package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public class GenomeEncodedEntity extends PhysicalEntity {
//    private Taxon species;

    public GenomeEncodedEntity(JSONObject jsonObject) {
        this(SchemaClass.GENOME_ENCODED_ENTITY, jsonObject);
    }

    public GenomeEncodedEntity(SchemaClass schemaClass, JSONObject jsonObject) {
        super(schemaClass, jsonObject);

    }

//    public Taxon getSpecies() {
//        return species;
//    }
//
//    public void setSpecies(Taxon species) {
//        this.species = species;
//    }
}
