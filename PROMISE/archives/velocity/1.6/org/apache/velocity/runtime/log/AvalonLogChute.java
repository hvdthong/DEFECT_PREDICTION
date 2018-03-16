import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log.Hierarchy;
import org.apache.log.LogTarget;
import org.apache.log.Logger;
import org.apache.log.Priority;
import org.apache.log.output.io.FileTarget;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.RuntimeServices;

/**
 * Implementation of a Avalon logger.
 *
 * @author <a href="mailto:jon@latchkey.com">Jon S. Stevens</a>
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @author <a href="mailto:nbubna@apache.org">Nathan Bubna</a>
 * @version $Id: AvalonLogChute.java 685685 2008-08-13 21:43:27Z nbubna $
 * @since 1.5
 */
public class AvalonLogChute implements LogChute
{
    public static final String AVALON_LOGGER = "runtime.log.logsystem.avalon.logger";
 
    public static final String AVALON_LOGGER_FORMAT = "runtime.log.logsystem.avalon.format";
    
    public static final String AVALON_LOGGER_LEVEL = "runtime.log.logsystem.avalon.level";

    private Logger logger = null;
    private RuntimeServices rsvc = null;
    
    private static final Map logLevels = new HashMap();
    
    static
    {
        logLevels.put("trace", Priority.DEBUG);
        logLevels.put("debug", Priority.DEBUG);
        logLevels.put("info", Priority.INFO);
        logLevels.put("warn", Priority.WARN);
        logLevels.put("error", Priority.ERROR);
    }

    /**
     * @see org.apache.velocity.runtime.log.LogChute#init(org.apache.velocity.runtime.RuntimeServices)
     */
    public void init(RuntimeServices rs) throws Exception
    {
        this.rsvc = rs;

        String name = (String)rsvc.getProperty(AVALON_LOGGER);
        if (name != null)
        {
            this.logger = Hierarchy.getDefaultHierarchy().getLoggerFor(name);
        }
        else
        {
            logger = Hierarchy.getDefaultHierarchy().getLoggerFor(rsvc.toString());

            String file = (String)rsvc.getProperty(RuntimeConstants.RUNTIME_LOG);
            if (StringUtils.isNotEmpty(file))
            {
                initTarget(file, rsvc);
            }
        }
    }

    private void initTarget(final String file, final RuntimeServices rsvc) throws Exception
    {
        try
        {
            String format = null;
            Priority level = null;
            if (rsvc != null)
            {
                format = rsvc.getString(AVALON_LOGGER_FORMAT, "%{time} %{message}\\n%{throwable}");
                level = (Priority) logLevels.get(rsvc.getString(AVALON_LOGGER_LEVEL, "warn"));
            }

            VelocityFormatter vf = new VelocityFormatter(format);

            FileTarget target = new FileTarget(new File(file), false, vf);

            logger.setPriority(level);
            logger.setLogTargets(new LogTarget[] { target });
            log(DEBUG_ID, "AvalonLogChute initialized using file '"+file+'\'');
        }
        catch (IOException ioe)
        {
            rsvc.getLog().error("Unable to create log file for AvalonLogChute", ioe);
            throw new Exception("Error configuring AvalonLogChute : " + ioe);
        }
    }

    /**
     * @param file
     * @throws Exception
     * @deprecated This method should not be used. It is here only to provide
     *             backwards compatibility for the deprecated AvalonLogSystem
     *             class, in case anyone used it and this method directly.
     */
    public void init(String file) throws Exception
    {
        logger = Hierarchy.getDefaultHierarchy().getLoggerFor(rsvc.toString());
        initTarget(file, null);
        log(DEBUG_ID, "You shouldn't be using the init(String file) method!");
    }

    /**
     *  logs messages
     *
     *  @param level severity level
     *  @param message complete error message
     */
    public void log(int level, String message)
    {
        /*
         * based on level, call the right logger method
         * and prefix with the appropos prefix
         */
        switch (level)
        {
            case WARN_ID:
                logger.warn(WARN_PREFIX + message );
                break;
            case INFO_ID:
                logger.info(INFO_PREFIX + message);
                break;
            case DEBUG_ID:
                logger.debug(DEBUG_PREFIX + message);
                break;
            case TRACE_ID:
                logger.debug(TRACE_PREFIX + message);
                break;
            case ERROR_ID:
                logger.error(ERROR_PREFIX + message);
                break;
            default:
                logger.info(message);
                break;
        }
    }

    /**
     *  logs messages and error
     *
     *  @param level severity level
     *  @param message complete error message
     * @param t
     */
    public void log(int level, String message, Throwable t)
    {
        switch (level)
        {
            case WARN_ID:
                logger.warn(WARN_PREFIX + message, t);
                break;
            case INFO_ID:
                logger.info(INFO_PREFIX + message, t);
                break;
            case DEBUG_ID:
                logger.debug(DEBUG_PREFIX + message, t);
                break;
            case TRACE_ID:
                logger.debug(TRACE_PREFIX + message, t);
                break;
            case ERROR_ID:
                logger.error(ERROR_PREFIX + message, t);
                break;
            default:
                logger.info(message, t);
                break;
        }
    }

    /**
     * Checks to see whether the specified level is enabled.
     * @param level
     * @return True if the specified level is enabled.
     */
    public boolean isLevelEnabled(int level)
    {
        switch (level)
        {
            case TRACE_ID:
            case DEBUG_ID:
                return logger.isDebugEnabled();
            case INFO_ID:
                return logger.isInfoEnabled();
            case WARN_ID:
                return logger.isWarnEnabled();
            case ERROR_ID:
                return logger.isErrorEnabled();
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
        logger.unsetLogTargets();
    }

}
