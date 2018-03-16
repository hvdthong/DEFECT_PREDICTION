import org.apache.velocity.runtime.RuntimeServices;

/**
 *  Logger used in case of failure. Does nothing.
 *
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: NullLogSystem.java 75955 2004-03-03 23:23:08Z geirm $
 */
public class NullLogSystem implements LogSystem
{
    public NullLogSystem()
    {
    }

    public void init( RuntimeServices rs )
        throws Exception
    {
    }
    
    /**
     *  logs messages to the great Garbage Collector
     *  in the sky
     *
     *  @param level severity level
     *  @param message complete error message
     */
    public void logVelocityMessage(int level, String message)
    {
    }
}
