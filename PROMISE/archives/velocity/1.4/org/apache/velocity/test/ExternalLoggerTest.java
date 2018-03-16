import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeServices;

import org.apache.velocity.runtime.log.LogSystem;

import junit.framework.TestCase;

/**
 * Tests if we can hand Velocity an arbitrary class for logging.
 *
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: ExternalLoggerTest.java 75955 2004-03-03 23:23:08Z geirm $
 */
public class ExternalLoggerTest extends TestCase implements LogSystem
{
   
    private String logString = null;
    private VelocityEngine ve = null;

    /**
     * Default constructor.
     */
    public ExternalLoggerTest()
    {
        super("LoggerTest");

        try
        {
            /*
             *  use an alternative logger.  Set it up here and pass it in.
             */
            
            ve = new VelocityEngine();
            ve.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM, this );
            ve.init();
        }
        catch (Exception e)
        {
            System.err.println("Cannot setup LoggerTest : " + e);
            System.exit(1);
        }            
    }

    public void init( RuntimeServices rs )
    {
    }

    public static junit.framework.Test suite ()
    {
        return new ExternalLoggerTest();
    }

    /**
     * Runs the test.
     */
    public void runTest ()
    {
        /*
         *  simply log something and see if we get it.
         */

        logString = null;

        String testString = "This is a test.";

        ve.warn( testString );

        if (logString == null || !logString.equals( VelocityEngine.WARN_PREFIX +  testString ) )
        {
            fail("Didn't recieve log message.");
        }
    }

    public void logVelocityMessage(int level, String message)
    {
        String out = "";

        /*
         * Start with the appropriate prefix
         */
        switch( level ) 
        {
            case LogSystem.DEBUG_ID :
                out = VelocityEngine.DEBUG_PREFIX;
                break;
            case LogSystem.INFO_ID :
                out = VelocityEngine.INFO_PREFIX;
                break;
            case LogSystem.WARN_ID :
                out = VelocityEngine.WARN_PREFIX;
                break;
            case LogSystem.ERROR_ID : 
                out = VelocityEngine.ERROR_PREFIX;
                break;
            default :
                out = VelocityEngine.UNKNOWN_PREFIX;
                break;
        }

        logString =  out + message;
    }
}
