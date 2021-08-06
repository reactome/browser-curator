package org.reactome.web.pwp.client.details.tabs.description.widgets.table;

import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.pwp.client.common.model.classes.*;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.PropertyType;
import org.reactome.web.pwp.client.details.tabs.description.widgets.table.factory.TableRowFactory;

import java.util.List;

/**
 * @author Joel Weiser (joel.weiser@oicr.on.ca)
 *         Created 8/4/2021
 */
public class CellTable extends PhysicalEntityTable {
    private Cell cell;

    public CellTable(Cell cell) {
        super(cell);
        this.cell = cell;
    }

    @Override
    protected Widget getTableRow(PropertyType propertyType) {
        Console.error("Hello");
        String title = propertyType.getTitle();
        switch (propertyType){
            case ORGAN:
                return TableRowFactory.getExternalOntologyRow(title, this.cell.getOrgan());
            case TISSUE:
                return TableRowFactory.getExternalOntologyRow(title, this.cell.getTissue());
            case TISSUE_LAYER:
                return TableRowFactory.getExternalOntologyRow(title, this.cell.getTissueLayer());
            case CELL_TYPE:
                return TableRowFactory.getExternalOntologyRow(title, this.cell.getCellTypes());
            case PROTEIN_MARKER:
                return TableRowFactory.getEntityWithAccessionedSequenceRow(title, this.cell.getProteinMarkers());
            case RNA_MARKER:
                return TableRowFactory.getEntityWithAccessionedSequenceRow(title, this.cell.getRnaMarkers());
            case MARKER_REFERENCE:
                return TableRowFactory.getMarkerReferenceRow(title, this.cell.getMarkerReferences());
            default:
                return super.getTableRow(propertyType);
        }
    }
}
