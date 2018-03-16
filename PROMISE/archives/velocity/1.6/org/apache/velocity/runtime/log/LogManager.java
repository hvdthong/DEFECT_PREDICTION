import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.util.ClassUtils;

/**
 * <p>
 * This class is responsible for instantiating the correct LogChute
 * </p>
 *
 * <p>
 * The approach is :
 * </p>
 * <ul>
 * <li>
 *      First try to see if the user is passing in a living object
 *      that is a LogChute, allowing the app to give its living
 *      custom loggers.
 *  </li>
 *  <li>
 *       Next, run through the (possible) list of classes specified
 *       specified as loggers, taking the first one that appears to
 *       work.  This is how we support finding logkit, log4j or
 *       jdk logging, whichever is in the classpath and found first,
 *       as all three are listed as defaults.
 *  </li>
 *  <li>
 *      Finally, we turn to the System.err stream and print log messages
 *      to it if nothing else works.
 *  </li>
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:jon@latchkey.com">Jon S. Stevens</a>
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @author <a href="mailto:nbubna@apache.org">Nathan Bubna</a>
 * @version $Id: LogManager.java 699307 2008-09-26 13:16:05Z cbrisson $
 */
public class LogManager
{
    private static LogChute createLogChute(RuntimeServices rsvc) throws Exception
    {
        Log log = rsvc.getLog();

        /* If a LogChute or LogSystem instance was set as a configuration
         * value, use that.  This is any class the user specifies.
         */
        Object o = rsvc.getProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM);
        if (o != null)
        {
            if (o instanceof LogChute)
            {
                try
                {
                    ((LogChute)o).init(rsvc);
                    return (LogChute)o;
                }
                catch (Exception e)
                {
                    String msg = "Could not init runtime.log.logsystem " + o;
                    log.error(msg, e);
                    throw new VelocityException(msg, e);
                }
            }
            else if (o instanceof LogSystem)
            {
                log.debug("LogSystem has been deprecated. Please use a LogChute implementation.");
                try
                {
                    LogChute chute = new LogChuteSystem((LogSystem)o);
                    chute.init(rsvc);
                    return chute;
                }
                catch (Exception e)
                {
                    String msg = "Could not init runtime.log.logsystem " + o;
                    log.error(msg, e);
                    throw new VelocityException(msg, e);
                }
            }
            else
            {
                String msg = o.getClass().getName() + " object set as runtime.log.logsystem is not a valid log implementation.";
                log.error(msg);
                throw new VelocityException(msg);
            }
        }

        /* otherwise, see if a class was specified.  You can put multiple
         * classes, and we use the first one we find.
         *
         * Note that the default value of this property contains the
         * AvalonLogChute, the Log4JLogChute, CommonsLogLogChute,
         * ServletLogChute, and the JdkLogChute for
         * convenience - so we use whichever we works first.
         */
        List classes = new ArrayList();
        Object obj = rsvc.getProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS );

        /*
         *  we might have a list, or not - so check
         */
        if ( obj instanceof List)
        {
            classes = (List) obj;
        }
        else if ( obj instanceof String)
        {
            classes.add( obj );
        }

        /*
         *  now run through the list, trying each.  It's ok to
         *  fail with a class not found, as we do this to also
         *  search out a default simple file logger
         */
        for( Iterator ii = classes.iterator(); ii.hasNext(); )
        {
            String claz = (String) ii.next();
            if (claz != null && claz.length() > 0 )
            {
                log.debug("Trying to use logger class " + claz );
                try
                {
                    o = ClassUtils.getNewInstance( claz );
                    if (o instanceof LogChute)
                    {
                        ((LogChute)o).init(rsvc);
                        log.debug("Using logger class " + claz);
                        return (LogChute)o;
                    }
                    else if (o instanceof LogSystem)
                    {
                        log.debug("LogSystem has been deprecated. Please use a LogChute implementation.");
                        LogChute chute = new LogChuteSystem((LogSystem)o);
                        chute.init(rsvc);
                        return chute;
                    }
                    else
                    {
                        String msg = "The specified logger class " + claz +
                                     " does not implement the "+LogChute.class.getName()+" interface.";
                        log.error(msg);
                        if (isProbablyProvidedLogChute(claz))
                        {
                            log.error("This appears to be a ClassLoader issue.  Check for multiple Velocity jars in your classpath.");
                        }
                        throw new VelocityException(msg);
                    }
                }
                catch(NoClassDefFoundError ncdfe)
                {
                    if (isProbablyProvidedLogChute(claz))
                    {
                        log.debug("Target log system for " + claz +
                                  " is not available (" + ncdfe.toString() +
                                  ").  Falling back to next log system...");
                    }
                    else
                    {
                        log.debug("Couldn't find class " + claz +
                                  " or necessary supporting classes in classpath.",
                                  ncdfe);
                    }
                }
                catch(Exception e)
                {
                    String msg = "Failed to initialize an instance of " + claz +
                                 " with the current runtime configuration.";
                    log.error(msg, e);
                    throw new VelocityException(msg,e);
                }
            }
        }

        /* If the above failed, that means either the user specified a
         * logging class that we can't find, there weren't the necessary
         * dependencies in the classpath for it, or there were the same
         * problems for the default loggers, log4j and Java1.4+.
         * Since we really don't know and we want to be sure the user knows
         * that something went wrong with the logging, let's fall back to the
         * surefire SystemLogChute. No panicking or failing to log!!
         */
        LogChute slc = new SystemLogChute();
        slc.init(rsvc);
        log.debug("Using SystemLogChute.");
        return slc;
    }

    /**
     * Simply tells whether the specified classname probably is provided
     * by Velocity or is implemented by someone else.  Not surefire, but
     * it'll probably always be right.  In any case, this method shouldn't
     * be relied upon for anything important.
     */
    private static boolean isProbablyProvidedLogChute(String claz)
    {
        if (claz == null)
        {
            return false;
        }
        else
        {
            return (claz.startsWith("org.apache.velocity.runtime.log")
                    && claz.endsWith("LogChute"));
        }
    }

    /**
     * Update the Log instance with the appropriate LogChute and other
     * settings determined by the RuntimeServices.
     * @param log
     * @param rsvc
     * @throws Exception
     * @since 1.5
     */
    public static void updateLog(Log log, RuntimeServices rsvc) throws Exception
    {
        LogChute newLogChute = createLogChute(rsvc);
        LogChute oldLogChute = log.getLogChute();

        log.setLogChute(newLogChute);

        if (oldLogChute instanceof HoldingLogChute)
        {
            HoldingLogChute hlc = (HoldingLogChute)oldLogChute;
            hlc.transferTo(newLogChute);
        }
    }

}

