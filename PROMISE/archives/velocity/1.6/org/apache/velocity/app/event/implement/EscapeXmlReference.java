import org.apache.commons.lang.StringEscapeUtils;

/**
 * Escape all XML entities.
 * @author wglass
 * @since 1.5
 */
public class EscapeXmlReference extends EscapeReference
{

    /**
     * Escape all XML entities.
     * 
     * @param text
     * @return An escaped String.
     */
    protected String escape(Object text)
    {
        return StringEscapeUtils.escapeXml(text.toString());
    }

    /**
     * @return attribute "eventhandler.escape.xml.match"
     */
    protected String getMatchAttribute()
    {
        return "eventhandler.escape.xml.match";
    }

}
