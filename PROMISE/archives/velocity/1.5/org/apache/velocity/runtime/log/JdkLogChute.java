import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.velocity.runtime.RuntimeServices;

/**
 * Implementation of a simple java.util.logging LogChute.
 *
 * @author <a href="mailto:nbubna@apache.org>Nathan Bubna</a>
 * @version $Id: JdkLogChute.java 463298 2006-10-12 16:10:32Z henning $
 * @since Velocity 1.5
 */
public class JdkLogChute implements LogChute
{
    /** Property key for specifying the name for the logger instance */
    public static final String RUNTIME_LOG_JDK_LOGGER =
        "runtime.log.logsystem.jdk.logger";

    /** Default name for the JDK logger instance */
    public static final String DEFAULT_LOG_NAME = "org.apache.velocity";

    /**
     *
     */
    protected Logger logger = null;

    /**
     * @see org.apache.velocity.runtime.log.LogChute#init(org.apache.velocity.runtime.RuntimeServices)
     */
    public void init(RuntimeServices rs)
    {
        String name = (String)rs.getProperty(RUNTIME_LOG_JDK_LOGGER);
        if (name == null)
        {
            name = DEFAULT_LOG_NAME;
        }
        logger = Logger.getLogger(name);
        log(LogChute.DEBUG_ID, "JdkLogChute will use logger '"+name+'\'');
    }

    /**
     * Returns the java.util.logging.Level that matches
     * to the specified LogChute level.
     * @param level
     * @return The current log level of the JDK Logger.
     */
    protected Level getJdkLevel(int level)
    {
        switch (level)
        {
            case LogChute.WARN_ID:
                return Level.WARNING;
            case LogChute.INFO_ID:
                return Level.INFO;
            case LogChute.DEBUG_ID:
                return Level.FINE;
            case LogChute.TRACE_ID:
                return Level.FINEST;
            case LogChute.ERROR_ID:
                return Level.SEVERE;
            default:
                return Level.FINER;
        }
    }

    /**
     * Logs messages
     *
     * @param level severity level
     * @param message complete error message
     */
    public void log(int level, String message)
    {
        log(level, message, null);
    }

    /**
     * Send a log message from Velocity along with an exception or error
     * @param level
     * @param message
     * @param t
     */
    public void log(int level, String message, Throwable t)
    {
        Level jdkLevel = getJdkLevel(level);
        if (t == null)
        {
            logger.log(jdkLevel, message);
        }
        else
        {
            logger.log(jdkLevel, message, t);
        }
    }

    /**
     * @see org.apache.velocity.runtime.log.LogChute#isLevelEnabled(int)
     */
    public boolean isLevelEnabled(int level)
    {
        Level jdkLevel = getJdkLevel(level);
        return logger.isLoggable(jdkLevel);
    }

}
