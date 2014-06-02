package org.reactome.web.elv.client.details.tabs.overview.model.widgets;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.elv.client.common.data.model.Reaction;
import org.reactome.web.elv.client.details.tabs.overview.model.widgets.factory.TableRowFactory;
import org.reactome.web.elv.client.details.tabs.overview.model.widgets.factory.PropertyType;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ReactionTable extends ReactionLikeEventTable {
    private Reaction reaction;

    public ReactionTable(Reaction reaction) {
        super(reaction);
        this.reaction = reaction;
    }

    @Override
    protected Widget getTableRow(PropertyType propertyType) {
        String title = propertyType.getTitle();
        switch (propertyType){
            case REGULATOR:
                return TableRowFactory.getRegulationRow(title, this.reaction.getRegulation());
            case REVERSE_REACTION:
                return TableRowFactory.getEventRow(title, this.reaction.getReverseReaction());
            default:
                return super.getTableRow(propertyType);
        }
    }
}
