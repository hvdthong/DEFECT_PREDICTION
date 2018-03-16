import org.apache.velocity.runtime.RuntimeServices;

/**
 * Old base interface that old logging systems needed to implement.
 *
 * @author <a href="mailto:jon@latchkey.com">Jon S. Stevens</a>
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @deprecated Use LogChute instead!
 * @version $Id: LogSystem.java 463298 2006-10-12 16:10:32Z henning $
 */
public interface LogSystem
{
    /**
     * @deprecated This is unused and meaningless
     */
    public final static boolean DEBUG_ON = true;

    /**
     * ID for debug messages.
     */
    public final static int DEBUG_ID = 0;

    /**
     * ID for info messages.
     */
    public final static int INFO_ID = 1;

    /**
     * ID for warning messages.
     */
    public final static int WARN_ID = 2;

    /**
     * ID for error messages.
     */
    public final static int ERROR_ID = 3;

    /**
     * Initializes this LogSystem.
     * @param rs
     * @throws Exception
     */
    public void init( RuntimeServices rs ) throws Exception;

    /**
     * @param level
     * @param message
     * @deprecated Use log(level, message).
     */
    public void logVelocityMessage(int level, String message);
}
