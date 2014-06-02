package org.reactome.web.elv.client.details.tabs.molecules.view;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.model.DatabaseObject;
import org.reactome.web.elv.client.details.tabs.DetailsTabView;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface MoleculesView extends DetailsTabView<MoleculesView.Presenter>{

    public interface Presenter extends DetailsTabView.Presenter {
        void downloadFormatedData(List<String> types, List<String> fields, String format);
        void getParticipatingMolecules();
        void getFormatedData(List<String> types, List<String> fields, String format);

    }

    void setParticipatingMolecules(DatabaseObject databaseObject, JSONObject json);
    void showExportResults(String results);
    void submitForm(String url, JSONObject params);
}
