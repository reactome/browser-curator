package org.reactome.web.pwp.client.common.model.classes;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.resources.client.ImageResource;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectFactory;
import org.reactome.web.pwp.client.common.model.factory.DatabaseObjectUtils;
import org.reactome.web.pwp.client.common.model.factory.SchemaClass;
import org.reactome.web.pwp.client.common.model.handlers.DatabaseObjectLoadedHandler;
import org.reactome.web.pwp.client.common.model.images.DatabaseObjectImages;

/**
 * DatabaseObject contains the minimum fields used to define an instance in the REACTOME RESTFul service
 *
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class DatabaseObject {
    private Long dbId;
    private String _displayName;
    private String displayName;
    private SchemaClass schemaClass;
    private StableIdentifier stableIdentifier;

    private boolean isLoaded = false;

    public DatabaseObject(SchemaClass schemaClass) {
        this.schemaClass = schemaClass;
    }

    @SuppressWarnings("unchecked")
    public <T extends org.reactome.web.pwp.client.common.model.classes.DatabaseObject> T cast() {
        return (T) this;
    }

    public void load(final DatabaseObjectLoadedHandler handler) {
        if(!this.isLoaded) {
            this.isLoaded = true;
            DatabaseObjectFactory.load(this, handler);
        }else{
            handler.onDatabaseObjectLoaded(this);
        }
    }

    public void load(JSONObject jsonObject) {
        if (jsonObject.containsKey("dbId")) {
            this.dbId = DatabaseObjectUtils.getLongValue(jsonObject, "dbId");
        }

        if (jsonObject.containsKey("_displayName")) {
            this._displayName = DatabaseObjectUtils.getStringValue(jsonObject, "_displayName");
        }

        if (jsonObject.containsKey("displayName")) {
            this.displayName = DatabaseObjectUtils.getStringValue(jsonObject, "displayName");
        }

        if (jsonObject.containsKey("stableIdentifier")) {
            this.stableIdentifier = DatabaseObjectUtils.getDatabaseObject(jsonObject, "stableIdentifier");
        }

//        this.isLoaded =  jsonObject.keySet().size()>3;

        checkDatabaseObject(DatabaseObjectUtils.getSchemaClass(jsonObject));
    }

    public Long getDbId() {
        return dbId;
    }

    public String get_displayName() {
        return _displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIdentifier() {
        if(stableIdentifier!=null){
            return stableIdentifier.getDisplayName().split("\\.")[0];
        }
        return "" + getDbId();
    }

    public SchemaClass getSchemaClass() {
        return schemaClass;
    }

    public StableIdentifier getStableIdentifier() {
        return stableIdentifier;
    }

    private void checkDatabaseObject(SchemaClass schemaClass) {
        if (this.dbId.equals(0L) || this.displayName == null) {
            String msg = "WRONG DATABASE OBJECT [" + this.toString() + "].";
            throw new RuntimeException("WRONG DATABASE OBJECT [" + this.toString() + "].");
        }

        if (!this.schemaClass.equals(schemaClass)) {
            String msg = "WRONG SCHEMA CLASS. Expecting [" + schemaClass.schemaClass + "], found [" + this.schemaClass.schemaClass + "].";
            throw new RuntimeException(msg);
        }
    }

    //Override this method for those objects with associated icon
    public ImageResource getImageResource(){
        return DatabaseObjectImages.INSTANCE.exclamation();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        org.reactome.web.pwp.client.common.model.classes.DatabaseObject that = (org.reactome.web.pwp.client.common.model.classes.DatabaseObject) o;

        return !(dbId != null ? !dbId.equals(that.dbId) : that.dbId != null);

    }

    @Override
    public int hashCode() {
        return dbId != null ? dbId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return schemaClass.schemaClass + "{" +
                (stableIdentifier != null ? "st_id=" + stableIdentifier.getIdentifier() : "dbId=" + dbId) +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}