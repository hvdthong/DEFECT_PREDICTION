import java.io.IOException;
import java.lang.reflect.Field;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.util.ExceptionUtils;

/**
 * Implementation of a simple log4j system that will either latch onto
 * an existing category, or just do a simple rolling file log.
 *
 * @author <a href="mailto:geirm@apache.org>Geir Magnusson Jr.</a>
 * @author <a href="mailto:dlr@finemaltcoding.com>Daniel L. Rall</a>
 * @author <a href="mailto:nbubna@apache.org>Nathan Bubna</a>
 * @version $Id: Log4JLogChute.java 463298 2006-10-12 16:10:32Z henning $
 * @since Velocity 1.5
 */
public class Log4JLogChute implements LogChute
{
    /**
     *
     */
    public static final String RUNTIME_LOG_LOG4J_LOGGER =
            "runtime.log.logsystem.log4j.logger";

    private RuntimeServices rsvc = null;
    private boolean hasTrace = false;
    private RollingFileAppender appender = null;

    /**
     */
    protected Logger logger = null;

    /**
     * @see org.apache.velocity.runtime.log.LogChute#init(org.apache.velocity.runtime.RuntimeServices)
     */
    public void init(RuntimeServices rs) throws Exception
    {
        rsvc = rs;

        /* first see if there is a category specified and just use that - it allows
         * the application to make us use an existing logger
         */
        String name = (String)rsvc.getProperty(RUNTIME_LOG_LOG4J_LOGGER);
        if (name != null)
        {
            logger = Logger.getLogger(name);
            log(DEBUG_ID, "Log4JLogChute using logger '" + name + '\'');
        }
        else
        {
            logger = Logger.getLogger(this.getClass().getName());

            String file = rsvc.getString(RuntimeConstants.RUNTIME_LOG);
            if (file != null && file.length() > 0)
            {
                initAppender(file);
            }
        }

        /* Ok, now let's see if this version of log4j supports the trace level. */
        try
        {
            Field traceLevel = Level.class.getField("TRACE");
            hasTrace = true;
        }
        catch (NoSuchFieldException e)
        {
            log(DEBUG_ID,
                "The version of log4j being used does not support the \"trace\" level.");
        }
    }

    private void initAppender(String file) throws Exception
    {
        try
        {
            PatternLayout layout = new PatternLayout("%d - %m%n");
            this.appender = new RollingFileAppender(layout, file, true);

            appender.setMaxBackupIndex(1);
            appender.setMaximumFileSize(100000);

            logger.setAdditivity(false);
            logger.setLevel(Level.DEBUG);
            logger.addAppender(appender);
            log(DEBUG_ID, "Log4JLogChute initialized using file '"+file+'\'');
        }
        catch (IOException ioe)
        {
            rsvc.getLog().warn("Could not create file appender '"+file+'\'', ioe);
            throw ExceptionUtils.createRuntimeException("Error configuring Log4JLogChute : ", ioe);
        }
    }

    /**
     *  logs messages
     *
     *  @param level severity level
     *  @param message complete error message
     */
    public void log(int level, String message)
    {
        switch (level)
        {
            case LogChute.WARN_ID:
                logger.warn(message);
                break;
            case LogChute.INFO_ID:
                logger.info(message);
                break;
            case LogChute.DEBUG_ID:
                logger.debug(message);
                break;
            case LogChute.TRACE_ID:
                if (hasTrace)
                {
                    logger.trace(message);
                }
                else
                {
                    logger.debug(message);
                }
                break;
            case LogChute.ERROR_ID:
                logger.error(message);
                break;
            default:
                logger.debug(message);
                break;
        }
    }

    /**
     * @see org.apache.velocity.runtime.log.LogChute#log(int, java.lang.String, java.lang.Throwable)
     */
    public void log(int level, String message, Throwable t)
    {
        switch (level)
        {
            case LogChute.WARN_ID:
                logger.warn(message, t);
                break;
            case LogChute.INFO_ID:
                logger.info(message, t);
                break;
            case LogChute.DEBUG_ID:
                logger.debug(message, t);
                break;
            case LogChute.TRACE_ID:
                if (hasTrace)
                {
                    logger.trace(message, t);
                }
                else
                {
                    logger.debug(message, t);
                }
                break;
            case LogChute.ERROR_ID:
                logger.error(message, t);
                break;
            default:
                logger.debug(message, t);
                break;
        }
    }

    /**
     * @see org.apache.velocity.runtime.log.LogChute#isLevelEnabled(int)
     */
    public boolean isLevelEnabled(int level)
    {
        switch (level)
        {
            case LogChute.DEBUG_ID:
                return logger.isDebugEnabled();
            case LogChute.INFO_ID:
                return logger.isInfoEnabled();
            case LogChute.TRACE_ID:
                if (hasTrace)
                {
                    return logger.isTraceEnabled();
                }
                else
                {
                    return logger.isDebugEnabled();
                }
            case LogChute.WARN_ID:
                return logger.isEnabledFor(Level.WARN);
            case LogChute.ERROR_ID:
                return logger.isEnabledFor(Level.ERROR);
            default:
                return true;
        }
    }

    /**
     * Also do a shutdown if the object is destroy()'d.
     * @throws Throwable
     */
    protected void finalize() throws Throwable
    {
        shutdown();
    }

    /** Close all destinations*/
    public void shutdown()
    {
        if (appender != null)
        {
            logger.removeAppender(appender);
            appender.close();
            appender = null;
        }
    }

}
