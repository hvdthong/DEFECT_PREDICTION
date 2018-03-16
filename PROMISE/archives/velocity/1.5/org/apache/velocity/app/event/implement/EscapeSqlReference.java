import org.apache.commons.lang.StringEscapeUtils;

/**
 * Escapes the characters in a String to be suitable to pass to an SQL query.
 * @author wglass
 */
public class EscapeSqlReference extends EscapeReference
{

    /**
     * Escapes the characters in a String to be suitable to pass to an SQL query.
     * 
     * @param text
     * @return An escaped string.
     */
    protected String escape(Object text)
    {
        return StringEscapeUtils.escapeSql(text.toString());
    }

    /**
     * @return attribute "eventhandler.escape.sql.match"
     */
    protected String getMatchAttribute()
    {
        return "eventhandler.escape.sql.match";
    }

}
