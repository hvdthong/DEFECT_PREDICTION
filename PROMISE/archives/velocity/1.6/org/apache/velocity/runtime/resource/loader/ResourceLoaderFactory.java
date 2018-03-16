import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.util.ClassUtils;
import org.apache.velocity.util.StringUtils;

/**
 * Factory to grab a template loader.
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @version $Id: ResourceLoaderFactory.java 687177 2008-08-19 22:00:32Z nbubna $
 */
public class ResourceLoaderFactory
{
    /**
     * Gets the loader specified in the configuration file.
     * @param rs
     * @param loaderClassName
     * @return TemplateLoader
     * @throws Exception
     */
    public static ResourceLoader getLoader(RuntimeServices rs, String loaderClassName)
     throws Exception
    {
        ResourceLoader loader = null;

        try
        {
            loader = (ResourceLoader) ClassUtils.getNewInstance( loaderClassName );

            rs.getLog().debug("ResourceLoader instantiated: "
                              + loader.getClass().getName());

            return loader;
        }
        catch(Exception e)
        {
            String msg = "Problem instantiating the template loader: "+loaderClassName+".\n" +
                         "Look at your properties file and make sure the\n" +
                         "name of the template loader is correct.";
            rs.getLog().error(msg, e);
            throw new VelocityException(msg, e);
        }
    }
}
