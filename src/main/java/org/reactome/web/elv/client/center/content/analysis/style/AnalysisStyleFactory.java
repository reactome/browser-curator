package org.reactome.web.elv.client.center.content.analysis.style;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public abstract class AnalysisStyleFactory {

    private static AnalysisResources RESOURCES = null;

    /**
     * A ClientBundle of resources used by this module.
     */
    public interface AnalysisResources extends ClientBundle {
        /**
         * The styles used in this widget.
         */
        @Source(AnalysisStyle.DEFAULT_CSS)
        public AnalysisStyle analysisStyle();

    }

    /**
     * Styles used by this module.
     */
    @CssResource.ImportedWithPrefix("reactome-analysisPanel")
    public interface AnalysisStyle extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String DEFAULT_CSS = "org/reactome/web/elv/client/center/content/analysis/style/AnalysisStyle.css";

        String analysisContainer();

        String analysisTitle();

        String analysisBlock();

        String analysisSubmission();

        String analysisMainSubmitter();

        String postSubmitterExamples();

        String postSubmitterClear();
    }

    public static AnalysisStyle getAnalysisStyle(){
        if(RESOURCES==null){
            RESOURCES = GWT.create(AnalysisResources.class);
            RESOURCES.analysisStyle().ensureInjected();
        }
        return RESOURCES.analysisStyle();
    }


}
