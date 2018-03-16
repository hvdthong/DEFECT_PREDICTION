import org.apache.velocity.app.event.EventCartridge;

/**
 *  Interface for event support.  Note that this is a public internal
 *  interface, as it is something that will be accessed from outside
 *  of the .context package.
 *
 *  @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 *  @version $Id: InternalEventContext.java 463298 2006-10-12 16:10:32Z henning $
 */
public interface InternalEventContext
{
    /**
     * @param ec
     * @return The old EventCartridge.
     */
    public EventCartridge attachEventCartridge( EventCartridge ec);

    /**
     * @return The current EventCartridge.
     */
    public EventCartridge getEventCartridge();
}
