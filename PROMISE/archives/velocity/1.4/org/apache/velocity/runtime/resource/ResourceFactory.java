import org.apache.velocity.Template;

/**
 * Class responsible for instantiating <code>Resource</code> objects,
 * given name and type.
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: ResourceFactory.java 75955 2004-03-03 23:23:08Z geirm $
 */
public class ResourceFactory
{
    public static Resource getResource(String resourceName, int resourceType)
    {
        Resource resource = null;
        
        switch (resourceType)
        {
            case ResourceManager.RESOURCE_TEMPLATE:
                resource = new Template();
                break;
            
            case ResourceManager.RESOURCE_CONTENT:
                resource = new ContentResource();
                break;
        }
    
        return resource;
    }
}
