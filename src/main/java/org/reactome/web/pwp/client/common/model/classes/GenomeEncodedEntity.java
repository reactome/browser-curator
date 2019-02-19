package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.resources.client.ImageResource;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;
import org.reactome.web.pwp.client.common.model.images.DatabaseObjectImages;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public class GenomeEncodedEntity extends PhysicalEntity {
//    private Taxon species;

    public GenomeEncodedEntity() {
        this(SchemaClass.GENOME_ENCODED_ENTITY);
    }

    public GenomeEncodedEntity(SchemaClass schemaClass) {
        super(schemaClass);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);
    }

//    public Taxon getSpecies() {
//        return species;
//    }
//
//    public void setSpecies(Taxon species) {
//        this.species = species;
//    }

    @Override
    public ImageResource getImageResource() {
        return DatabaseObjectImages.INSTANCE.genomeEncodeEntity();
    }
}
