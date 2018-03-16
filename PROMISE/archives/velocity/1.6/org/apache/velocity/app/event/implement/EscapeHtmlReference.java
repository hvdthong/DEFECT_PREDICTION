import org.apache.commons.lang.StringEscapeUtils;

/**
 * Escape all HTML entities.
 * @author wglass
 * @since 1.5
 */
public class EscapeHtmlReference extends EscapeReference
{

    /**
     * Escape all HTML entities.
     * 
     * @param text
     * @return An escaped String.
     */
    protected String escape(Object text)
    {
        return StringEscapeUtils.escapeHtml(text.toString());
    }

    /**
     * @return attribute "eventhandler.escape.html.match"
     */
    protected String getMatchAttribute()
    {
        return "eventhandler.escape.html.match";
    }

}
