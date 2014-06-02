package org.reactome.web.elv.client.details.tabs.analysis.view.style;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public abstract class AnalysisTabStyleFactory {

    private static AnalysisTabResources RESOURCES = null;

    /**
     * A ClientBundle of resources used by this module.
     */
    public interface AnalysisTabResources extends ClientBundle {
        /**
         * The styles used in this widget.
         */
        @Source(AnalysisTabStyle.DEFAULT_CSS)
        public AnalysisTabStyle analysisTabStyle();

    }

    /**
     * Styles used by this module.
     */
    @CssResource.ImportedWithPrefix("reactome-analysisPanel")
    public interface AnalysisTabStyle extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String DEFAULT_CSS = "org/reactome/web/elv/client/details/tabs/analysis/view/style/AnalysisTabStyle.css";

        String analysisTabSummary();

        String analysisTabSummaryInfo();

//        String analysisOptions();

        String analysisTableSelector();

        String analysisTableSelectorButton();

        String analysisRowSelector();

//        String notFoundIdentifiers();
//
//        String notFoundIdentifierSelected();
    }

    public static AnalysisTabStyle getAnalysisStyle(){
        if(RESOURCES==null){
            RESOURCES = GWT.create(AnalysisTabResources.class);
            RESOURCES.analysisTabStyle().ensureInjected();
        }
        return RESOURCES.analysisTabStyle();
    }
}
