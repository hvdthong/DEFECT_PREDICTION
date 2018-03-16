import org.apache.velocity.runtime.RuntimeServices;

/**
 * Base interface that Logging systems need to implement.
 *
 * @author <a href="mailto:jon@latchkey.com">Jon S. Stevens</a>
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: LogSystem.java 75955 2004-03-03 23:23:08Z geirm $
 */
public interface LogSystem
{
    public final static boolean DEBUG_ON = true;

    /**
     * Prefix for debug messages.
     */
    public final static int DEBUG_ID = 0;

    /** 
     * Prefix for info messages.
     */
    public final static int INFO_ID = 1;
    
    /** 
     * Prefix for warning messages.
     */
    public final static int WARN_ID = 2;

    /** 
     * Prefix for error messages.
     */
    public final static int ERROR_ID = 3;

    /**
     *  init()
     */
    public void init( RuntimeServices rs ) throws Exception;

    /**
     * Send a log message from Velocity.
     */
    public void logVelocityMessage(int level, String message);
}
