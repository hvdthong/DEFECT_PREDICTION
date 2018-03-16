import java.util.Vector;
import java.util.Enumeration;

import org.apache.velocity.runtime.RuntimeServices;

/**
 *  Pre-init logger.  I believe that this was suggested by
 *  Carsten Ziegeler <cziegeler@sundn.de> and 
 *  Jeroen C. van Gelderen.  If this isn't correct, let me
 *  know as this was a good idea... 
 *
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: PrimordialLogSystem.java 75955 2004-03-03 23:23:08Z geirm $
 */
public class PrimordialLogSystem implements LogSystem
{
    private Vector pendingMessages = new Vector();    
    private RuntimeServices rsvc = null;

    /**
     *  default CTOR.
     */
    public PrimordialLogSystem()
    {
    }

    public void init( RuntimeServices rs )
        throws Exception
    {
        rsvc = rs;
    }
    
    /**
     *  logs messages.  All we do is store them until
     *   'later'.
     *
     *  @param level severity level
     *  @param message complete error message
     */
    public void logVelocityMessage(int level, String message)
    {
        synchronized( this )
        {
            Object[] data = new Object[2];
            data[0] = new Integer(level);
            data[1] = message;
            pendingMessages.addElement(data);
        }
    }
    
    /**
     * dumps the log messages this logger is holding into a new logger
     */
    public void dumpLogMessages( LogSystem newLogger )
    {
        synchronized( this )
        {
            if ( !pendingMessages.isEmpty())
            {
                /*
                 *  iterate and log each individual message...
                 */
            
                for( Enumeration e = pendingMessages.elements(); e.hasMoreElements(); )
                {
                    Object[] data = (Object[]) e.nextElement();
                    newLogger.logVelocityMessage(((Integer) data[0]).intValue(), (String) data[1]);
                }
            }    
        }
    }    
}
