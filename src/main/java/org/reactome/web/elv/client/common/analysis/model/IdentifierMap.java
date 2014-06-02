package org.reactome.web.elv.client.common.analysis.model;

import java.util.Set;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface IdentifierMap {

    String getResource();

    Set<String> getIds();
}
