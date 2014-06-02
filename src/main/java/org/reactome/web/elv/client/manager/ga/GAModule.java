package org.reactome.web.elv.client.manager.ga;

import org.reactome.web.elv.client.details.model.DetailsTabType;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public enum GAModule {
    GENERAL,
    TOP_BAR,
    EVENT_HIERARCHY,
    DIAGRAM,
    /** DETAILS PANEL **/
    DETAILS,
    OVERVIEW_TAB,
    MOLECULES_TAB,
    STRUCTURES_TAB,
    EXPRESSION_TAB,
    ANALYSIS_TAB,
    PROCESSES_TAB,
    DOWNLOADS_TAB;


    public static GAModule getCorrespondence(DetailsTabType detailsTabType){
        GAModule gaModule = null;
        switch (detailsTabType){
            case OVERVIEW:
                gaModule = GAModule.OVERVIEW_TAB;
                break;
            case PARTICIPATING_MOLECULES:
                gaModule = GAModule.MOLECULES_TAB;
                break;
            case STRUCTURES:
                gaModule = GAModule.STRUCTURES_TAB;
                break;
            case EXPRESSION:
                gaModule = GAModule.EXPRESSION_TAB;
                break;
            case ANALYSIS:
                gaModule = GAModule.ANALYSIS_TAB;
                break;
            case PARTICIPATING_PROCESSES:
                gaModule = GAModule.PROCESSES_TAB;
                break;
            case DOWNLOADS:
                gaModule = GAModule.DOWNLOADS_TAB;
        }
        return gaModule;
    }
}
