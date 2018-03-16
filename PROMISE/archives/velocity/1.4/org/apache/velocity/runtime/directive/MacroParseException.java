import org.apache.velocity.runtime.parser.ParseException;

/**
 *  Exception to indicate problem happened while constructing #macro()
 *
 *  For internal use in parser - not to be passed to app level
 *
 * @author <a href="mailto:geirm@apache.org">Geir Magnusson Jr.</a>
 * @version $Id: MacroParseException.java 75955 2004-03-03 23:23:08Z geirm $
 */
public class MacroParseException extends ParseException
{
    public MacroParseException(String msg)
    {
        super(msg);
    }
}
