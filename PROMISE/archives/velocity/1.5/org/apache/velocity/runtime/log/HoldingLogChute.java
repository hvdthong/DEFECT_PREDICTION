import java.util.Vector;
import java.util.Iterator;
import org.apache.velocity.runtime.RuntimeServices;

/**
 *  Pre-init logger.  I believe that this was suggested by
 *  Carsten Ziegeler <cziegeler@sundn.de> and
 *  Jeroen C. van Gelderen.  If this isn't correct, let me
 *  know as this was a good idea...
 *
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @author <a href="mailto:nbubna@apache.org">Nathan Bubna</a>
 * @version $Id: HoldingLogChute.java 463298 2006-10-12 16:10:32Z henning $
 */
class HoldingLogChute implements LogChute
{
    private Vector pendingMessages = new Vector();

    /**
     * @see org.apache.velocity.runtime.log.LogChute#init(org.apache.velocity.runtime.RuntimeServices)
     */
    public void init(RuntimeServices rs) throws Exception
    {
    }

    /**
     * Logs messages. All we do is store them until 'later'.
     *
     * @param level severity level
     * @param message complete error message
     */
    public void log(int level, String message)
    {
        synchronized(this)
        {
            Object[] data = new Object[2];
            data[0] = new Integer(level);
            data[1] = message;
            pendingMessages.addElement(data);
        }
    }

    /**
     * Logs messages and errors. All we do is store them until 'later'.
     *
     * @param level severity level
     * @param message complete error message
     * @param t the accompanying java.lang.Throwable
     */
    public void log(int level, String message, Throwable t)
    {
        synchronized(this)
        {
            Object[] data = new Object[3];
            data[0] = new Integer(level);
            data[1] = message;
            data[2] = t;
            pendingMessages.addElement(data);
        }
    }

    /**
     * @see org.apache.velocity.runtime.log.LogChute#isLevelEnabled(int)
     */
    public boolean isLevelEnabled(int level)
    {
        return true;
    }

    /**
     * Dumps the log messages this chute is holding into a new chute
     * @param newChute
     */
    public void transferTo(LogChute newChute)
    {
        synchronized(this)
        {
            if (!pendingMessages.isEmpty())
            {
                for(Iterator i = pendingMessages.iterator(); i.hasNext();)
                {
                    Object[] data = (Object[])i.next();
                    int level = ((Integer)data[0]).intValue();
                    String message = (String)data[1];
                    if (data.length == 2)
                    {
                        newChute.log(level, message);
                    }
                    else
                    {
                        newChute.log(level, message, (Throwable)data[2]);
                    }
                }
            }
        }
    }

}
