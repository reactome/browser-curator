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
public class CandidateSet extends EntitySet {

    private List<PhysicalEntity> hasCandidate = new LinkedList<PhysicalEntity>();

    public CandidateSet(JSONObject jsonObject) {
        super(SchemaClass.CANDIDATE_SET, jsonObject);

        for (JSONObject object : FactoryUtils.getObjectList(jsonObject, "hasCandidate")) {
            this.hasCandidate.add((PhysicalEntity) ModelFactory.getDatabaseObject(object));
        }
    }

    public List<PhysicalEntity> getHasCandidate() {
        return hasCandidate;
    }
}
