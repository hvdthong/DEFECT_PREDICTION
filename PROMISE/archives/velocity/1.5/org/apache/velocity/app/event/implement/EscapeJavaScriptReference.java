import org.apache.commons.lang.StringEscapeUtils;

/**
 * Escapes the characters in a String to be suitable for use in JavaScript.
 * @author wglass
 */
public class EscapeJavaScriptReference extends EscapeReference
{

    /**
     * Escapes the characters in a String to be suitable for use in JavaScript.
     * 
     * @param text
     * @return An escaped String.
     */
    protected String escape(Object text)
    {
        return StringEscapeUtils.escapeJavaScript(text.toString());
    }

    /**
     * @return attribute "eventhandler.escape.javascript.match"
     */
    protected String getMatchAttribute()
    {
        return "eventhandler.escape.javascript.match";
    }

}
