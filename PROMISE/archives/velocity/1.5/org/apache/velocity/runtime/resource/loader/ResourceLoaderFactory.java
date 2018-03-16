import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.util.ClassUtils;
import org.apache.velocity.util.StringUtils;

/**
 * Factory to grab a template loader.
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @version $Id: ResourceLoaderFactory.java 463298 2006-10-12 16:10:32Z henning $
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
            rs.getLog().error("Problem instantiating the template loader.\n" +
                          "Look at your properties file and make sure the\n" +
                          "name of the template loader is correct. Here is the\n" +
                          "error:", e);

            throw new Exception("Problem initializing template loader: " + loaderClassName +
            "\nError is: " + StringUtils.stackTrace(e));
        }
    }
}
