import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.util.StringUtils;

/**
 * Factory to grab a template loader.
 * 
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @version $Id: ResourceLoaderFactory.java 75955 2004-03-03 23:23:08Z geirm $
 */
public class ResourceLoaderFactory
{
    /**
     * Gets the loader specified in the configuration file.
     * @return TemplateLoader
     */
    public static ResourceLoader getLoader(RuntimeServices rs, String loaderClassName)
     throws Exception
    {
        ResourceLoader loader = null;
        
        try
        {
            loader = ((ResourceLoader)Class.forName(loaderClassName)
                .newInstance());
            
            rs.info("Resource Loader Instantiated: " + 
                loader.getClass().getName());
            
            return loader;
        }
        catch( Exception e)
        {
            rs.error("Problem instantiating the template loader.\n" +
                          "Look at your properties file and make sure the\n" +
                          "name of the template loader is correct. Here is the\n" +
                          "error: " + StringUtils.stackTrace(e));
            
            throw new Exception("Problem initializing template loader: " + loaderClassName + 
            "\nError is: " + StringUtils.stackTrace(e));
        }
    }
}
