package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.resources.client.ImageResource;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectFactory;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectUtils;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;
import org.reactome.web.pwp.client.common.model.images.DatabaseObjectImages;

import java.util.LinkedList;
import java.util.List;

public class Cell extends PhysicalEntity {
    private Anatomy organ;
    private Anatomy tissue;
    private Anatomy tissueLayer;
    private List<CellType> cellTypes;
    private List<EntityWithAccessionedSequence> proteinMarkers;
    private List<EntityWithAccessionedSequence> rnaMarkers;
    private List<MarkerReference> markerReferences;

    public Cell() {
        super(SchemaClass.CELL);
    }

    @Override
    public void load(JSONObject jsonObject) {
        super.load(jsonObject);

        if (jsonObject.containsKey("organ")) {
            this.organ = DatabaseObjectUtils.getDatabaseObject(jsonObject, "organ");
        }

        if (jsonObject.containsKey("tissue")) {
            this.tissue = DatabaseObjectUtils.getDatabaseObject(jsonObject, "tissue");
        }

        if (jsonObject.containsKey("tissueLayer")) {
            this.tissueLayer = DatabaseObjectUtils.getDatabaseObject(jsonObject, "tissueLayer");
        }

        this.cellTypes = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "cellType")) {
            this.cellTypes.add((CellType) DatabaseObjectFactory.create(object));
        }

        this.proteinMarkers = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "proteinMarker")) {
            this.proteinMarkers.add((EntityWithAccessionedSequence) DatabaseObjectFactory.create(object));
        }

        this.rnaMarkers = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "rnaMarker")) {
            this.rnaMarkers.add((EntityWithAccessionedSequence) DatabaseObjectFactory.create(object));
        }

        this.markerReferences = new LinkedList<>();
        for (JSONObject object : DatabaseObjectUtils.getObjectList(jsonObject, "markerReference")) {
            this.markerReferences.add((MarkerReference) DatabaseObjectFactory.create(object));
        }
    }

    public Anatomy getOrgan() {
        return this.organ;
    }

    public Anatomy getTissue() {
        return this.tissue;
    }

    public Anatomy getTissueLayer() {
        return this.tissueLayer;
    }

    public List<CellType> getCellTypes() {
        return this.cellTypes;
    }

    public List<EntityWithAccessionedSequence> getProteinMarkers() {
        return this.proteinMarkers;
    }

    public List<EntityWithAccessionedSequence> getRnaMarkers() {
        return this.rnaMarkers;
    }

    public List<MarkerReference> getMarkerReferences() {
        return this.markerReferences;
    }

    @Override
    public ImageResource getImageResource() {
        return DatabaseObjectImages.INSTANCE.cell();
    }
}
