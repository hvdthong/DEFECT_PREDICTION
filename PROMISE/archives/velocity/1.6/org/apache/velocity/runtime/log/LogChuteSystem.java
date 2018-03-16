import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.util.StringUtils;

/**
 * Wrapper to make user's custom LogSystem implementations work
 * with the new LogChute setup.
 *
 * @author <a href="mailto:nbubna@apache.org">Nathan Bubna</a>
 * @version $Id: LogChuteSystem.java 685685 2008-08-13 21:43:27Z nbubna $
 * @since 1.5
 */
public class LogChuteSystem implements LogChute
{

    private LogSystem logSystem;

    /**
     * Only classes in this package should be creating this.
     * Users should not have to mess with this class.
     * @param wrapMe
     */
    protected LogChuteSystem(LogSystem wrapMe)
    {
        this.logSystem = wrapMe;
    }

    /**
     * @see org.apache.velocity.runtime.log.LogChute#init(org.apache.velocity.runtime.RuntimeServices)
     */
    public void init(RuntimeServices rs) throws Exception
    {
        logSystem.init(rs);
    }

    /**
     * @see org.apache.velocity.runtime.log.LogChute#log(int, java.lang.String)
     */
    public void log(int level, String message)
    {
        logSystem.logVelocityMessage(level, message);
    }

    /**
     * First passes off the message at the specified level,
     * then passes off stack trace of the Throwable as a
     * 2nd message at the same level.
     * @param level
     * @param message
     * @param t
     */
    public void log(int level, String message, Throwable t)
    {
        logSystem.logVelocityMessage(level, message);
        logSystem.logVelocityMessage(level, StringUtils.stackTrace(t));
    }

    /**
     * @see org.apache.velocity.runtime.log.LogChute#isLevelEnabled(int)
     */
    public boolean isLevelEnabled(int level)
    {
        return true;
    }

}
