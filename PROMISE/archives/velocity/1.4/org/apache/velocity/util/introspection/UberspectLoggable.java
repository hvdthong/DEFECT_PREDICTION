import org.apache.velocity.runtime.RuntimeLogger;

/**
 *  Marker interface to let an uberspector indicate it can and wants to
 *  log
 *
 *  Thanks to Paulo for the suggestion
 *
 * @author <a href="mailto:geirm@apache.org">Geir Magnusson Jr.</a>
 * @version $Id: UberspectLoggable.java 75955 2004-03-03 23:23:08Z geirm $
 *
 */
public interface UberspectLoggable
{
    /**
     *  Sets the logger.  This will be called before any calls to the
     *  uberspector
     */
    public void setRuntimeLogger(RuntimeLogger logger);
}
