import org.apache.velocity.runtime.RuntimeServices;

/**
 *  Logger used in case of failure. Does nothing.
 *
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @author <a href="mailto:nbubna@optonline.net">Nathan Bubna.</a>
 * @version $Id: NullLogChute.java 463298 2006-10-12 16:10:32Z henning $
 */
public class NullLogChute implements LogChute
{

    /**
     * @see org.apache.velocity.runtime.log.LogChute#init(org.apache.velocity.runtime.RuntimeServices)
     */
    public void init(RuntimeServices rs) throws Exception
    {
    }

    /**
     * logs messages to the great Garbage Collector in the sky
     *
     *  @param level severity level
     *  @param message complete error message
     */
    public void log(int level, String message)
    {
    }

    /**
     * logs messages and their accompanying Throwables
     * to the great Garbage Collector in the sky
     *
     * @param level severity level
     * @param message complete error message
     * @param t the java.lang.Throwable
     */
    public void log(int level, String message, Throwable t)
    {
    }

    /**
     * @see org.apache.velocity.runtime.log.LogChute#isLevelEnabled(int)
     */
    public boolean isLevelEnabled(int level)
    {
        return false;
    }

}
