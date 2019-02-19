package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.resources.client.ImageResource;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectFactory;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectUtils;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;
import org.reactome.web.pwp.client.common.model.images.DatabaseObjectImages;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public class CandidateSet extends EntitySet {

    private List<PhysicalEntity> hasCandidate;

    public CandidateSet() {
        super(SchemaClass.CANDIDATE_SET);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        this.hasCandidate = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "hasCandidate")) {
            this.hasCandidate.add((PhysicalEntity) DatabaseObjectFactory.create(object));
        }
    }

    public List<PhysicalEntity> getHasCandidate() {
        return hasCandidate;
    }

    @Override
    public ImageResource getImageResource() {
        return DatabaseObjectImages.INSTANCE.candidateSet();
    }
}
