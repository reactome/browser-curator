package org.reactome.web.elv.client.details.tabs.analysis.view.widgets.common.cells;

import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class CustomNumberCell extends NumberCell {
    /**
     * The HTML templates used to render the cell.
     */
    interface Templates extends SafeHtmlTemplates {
        /**
         * The template for this Cell, which includes styles and a value.
         *
         * @param styles the styles to include in the style attribute of the div
         * @param value  the safe value. Since the value type is {@link SafeHtml},
         *               it will not be escaped before including it in the template.
         *               Alternatively, you could make the value type String, in which
         *               case the value would be escaped.
         * @return a {@link SafeHtml} instance
         */
        @SafeHtmlTemplates.Template("<div style=\"{0}\">{1}</div>")
        SafeHtml cell(SafeStyles styles, SafeHtml value);
    }

    /**
     * Create a singleton instance of the templates used to render the cell.
     */
    private static Templates templates = GWT.create(Templates.class);

    private CssColor numberColor;
    private NumberFormat format;

    public CustomNumberCell() {
        this(CssColor.make("black"));
    }

    public CustomNumberCell(CssColor numberColor) {
        this(numberColor, NumberFormat.getDecimalFormat());
    }

    public CustomNumberCell(NumberFormat format) {
        this(CssColor.make("black"), format);
    }


    public CustomNumberCell(CssColor numberColor, NumberFormat format) {
        super(format);
        this.format = format;
        this.numberColor = numberColor;
    }

    @Override
    public void render(Context context, Number value, SafeHtmlBuilder sb) {
            /*
            * Always do a null check on the value. Cell widgets can pass null to
            * cells if the underlying data contains a null, or if the data arrives
            * out of order.
            */
        if (value == null) {
            return;
        }

        // If the value comes from the user, we escape it to avoid XSS attacks.
        SafeHtml safeValue = SafeHtmlUtils.fromString(format.format(value));

        // Use the template to create the Cell's html.
        SafeStyles color = SafeStylesUtils.forTrustedColor(numberColor.value());

//        SafeStyles background = SafeStylesUtils.forTrustedBackgroundColor("rgba(255, 0, 0, 0.4)");
        SafeHtml rendered = templates.cell(color, safeValue);
        sb.append(rendered);
    }
}

