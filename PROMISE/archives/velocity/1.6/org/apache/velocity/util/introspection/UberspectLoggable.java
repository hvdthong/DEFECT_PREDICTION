import org.apache.velocity.runtime.RuntimeLogger;
import org.apache.velocity.runtime.log.Log;

/**
 *  Marker interface to let an uberspector indicate it can and wants to
 *  log
 *
 *  Thanks to Paulo for the suggestion
 *
 * @author <a href="mailto:nbubna@apache.org">Nathan Bubna</a>
 * @author <a href="mailto:geirm@apache.org">Geir Magnusson Jr.</a>
 * @version $Id: UberspectLoggable.java 463298 2006-10-12 16:10:32Z henning $
 *
 */
public interface UberspectLoggable
{

    /**
     * Sets the logger.  This will be called before any calls to the
     * uberspector
     * @param log
     */
    public void setLog(Log log);

    /**
     * @param logger
     * @deprecated Use setLog(Log log) instead.
     */
    public void setRuntimeLogger(RuntimeLogger logger);
}
