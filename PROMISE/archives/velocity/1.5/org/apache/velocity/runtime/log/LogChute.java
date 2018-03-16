import org.apache.velocity.runtime.RuntimeServices;

/**
 * Base interface that logging systems need to implement. This
 * is the blessed descendant of the old LogSystem interface.
 *
 * @author <a href="mailto:jon@latchkey.com">Jon S. Stevens</a>
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @author <a href="mailto:nbubna@apache.org">Nathan Bubna</a>
 * @version $Id: LogChute.java 494611 2007-01-09 21:57:20Z henning $
 */
public interface LogChute
{
    /** Prefix string for trace messages. */
    String TRACE_PREFIX = " [trace] ";

    /** Prefix string for debug messages. */
    String DEBUG_PREFIX = " [debug] ";

    /** Prefix string for info messages. */
    String INFO_PREFIX  = "  [info] ";

    /** Prefix string for warn messages. */
    String WARN_PREFIX  = "  [warn] ";

    /** Prefix string for error messages. */
    String ERROR_PREFIX = " [error] ";

    /** ID for trace messages. */
    int TRACE_ID = -1;

    /** ID for debug messages. */
    int DEBUG_ID = 0;

    /** ID for info messages. */
    int INFO_ID = 1;

    /** ID for warning messages. */
    int WARN_ID = 2;

    /** ID for error messages. */
    int ERROR_ID = 3;

    /**
     * Initializes this LogChute.
     * @param rs
     * @throws Exception
     */
    void init(RuntimeServices rs) throws Exception;

    /**
     * Send a log message from Velocity.
     * @param level
     * @param message
     */
    void log(int level, String message);

    /**
     * Send a log message from Velocity along with an exception or error
     * @param level
     * @param message
     * @param t
     */
    void log(int level, String message, Throwable t);

    /**
     * Tell whether or not a log level is enabled.
     * @param level
     * @return True if a level is enabled.
     */
    boolean isLevelEnabled(int level);

}
