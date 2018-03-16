import java.util.Date;
import org.apache.log.format.PatternFormatter;

/**
 *
 */
public class VelocityFormatter extends PatternFormatter
{
    /**
     * @param format
     */
    public VelocityFormatter( String format )
    {
	super( format );
    }

    /**
     * Utility method to format time.
     *
     * @param time the time
     * @param format ancilliary format parameter - allowed to be null
     * @return the formatted string
     */
    protected String getTime( final long time, final String format )
    {
        return new Date().toString();
    }
}
