import org.apache.velocity.runtime.RuntimeServices;


/**
 * Use this interface to automatically
 * have the method setRuntimeServices called at initialization.  
 * Applies to EventHandler and Uberspect implementations.
 *
 * @author <a href="mailto:wglass@wglass@forio.com">Will Glass-Husain</a>
 * @version $Id: RuntimeServicesAware.java 463298 2006-10-12 16:10:32Z henning $
 */
public interface  RuntimeServicesAware
{
    /**
     * Called automatically when event cartridge is initialized.
     * @param rs RuntimeServices object assigned during initialization
     */
    public void setRuntimeServices( RuntimeServices rs );

}
