import org.apache.velocity.runtime.RuntimeServices;


/**
 * Use this interface to automatically
 * have the method setRuntimeServices called at initialization.  
 * Applies to EventHandler and Uberspect implementations.
 *
 * @author <a href="mailto:wglass@wglass@forio.com">Will Glass-Husain</a>
 * @version $Id: RuntimeServicesAware.java 685685 2008-08-13 21:43:27Z nbubna $
 * @since 1.5
 */
public interface  RuntimeServicesAware
{
    /**
     * Called automatically when event cartridge is initialized.
     * @param rs RuntimeServices object assigned during initialization
     */
    public void setRuntimeServices( RuntimeServices rs );

}
