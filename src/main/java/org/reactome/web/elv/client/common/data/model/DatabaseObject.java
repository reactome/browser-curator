package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.FactoryUtils;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;
import org.reactome.web.elv.client.common.utils.Console;

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

    public DatabaseObject(SchemaClass schemaClass, JSONObject jsonObject) {
        if(jsonObject.containsKey("dbId")){
            this.dbId = FactoryUtils.getLongValue(jsonObject, "dbId");
        }

        if(jsonObject.containsKey("_displayName")){
            this._displayName = FactoryUtils.getStringValue(jsonObject, "_displayName");
        }

        if(jsonObject.containsKey("displayName")){
            this.displayName = FactoryUtils.getStringValue(jsonObject, "displayName");
        }

        this.schemaClass = FactoryUtils.getSchemaClass(jsonObject);

        if(jsonObject.containsKey("stableIdentifier")){
            this.stableIdentifier = (StableIdentifier) FactoryUtils.getDatabaseObject(jsonObject, "stableIdentifier");
        }

        checkDatabaseObject(schemaClass);
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

    public String getIdentifier(){
        return String.valueOf(getDbId());
    }

    public SchemaClass getSchemaClass() {
        return schemaClass;
    }

    public StableIdentifier getStableIdentifier() {
        return stableIdentifier;
    }

    private void checkDatabaseObject(SchemaClass schemaClass){
        if(this.dbId.equals(0L) || this.displayName == null){
            String msg = "WRONG DATABASE OBJECT [" + this.toString() + "].";
            if(GWT.isScript()) Console.error(msg);
            throw new RuntimeException("WRONG DATABASE OBJECT [" + this.toString() + "].");
        }

        if(!this.schemaClass.equals(schemaClass)){
            String msg = "WRONG SCHEMA CLASS. Expecting [" + schemaClass.schemaClass + "], found [" + this.schemaClass.schemaClass + "].";
            if(GWT.isScript()) Console.error(msg);
            throw new RuntimeException(msg);
        }
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DatabaseObject that = (DatabaseObject) o;

        if (dbId != null ? !dbId.equals(that.dbId) : that.dbId != null) return false;
        //noinspection RedundantIfStatement
        if (schemaClass != that.schemaClass) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = dbId != null ? dbId.hashCode() : 0;
        result = 31 * result + (schemaClass != null ? schemaClass.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DatabaseObject{" +
                "dbId=" + dbId +
                ", displayName='" + displayName + '\'' +
                ", schemaClass=" + schemaClass.schemaClass +
                '}';
    }
}