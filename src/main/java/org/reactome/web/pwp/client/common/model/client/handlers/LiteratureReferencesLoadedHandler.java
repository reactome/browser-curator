package org.reactome.web.pwp.client.common.model.client.handlers;

import org.reactome.web.pwp.client.common.model.classes.Person;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface LiteratureReferencesLoadedHandler {
    void onLiteratureReferencesLoaded(Person person);
    void onLiteratureReferencesError(Throwable throwable);
}
