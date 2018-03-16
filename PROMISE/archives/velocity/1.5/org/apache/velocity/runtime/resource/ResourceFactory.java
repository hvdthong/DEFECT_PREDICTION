import org.apache.velocity.Template;

/**
 * Class responsible for instantiating <code>Resource</code> objects,
 * given name and type.
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: ResourceFactory.java 463298 2006-10-12 16:10:32Z henning $
 */
public class ResourceFactory
{
    /**
     * @param resourceName
     * @param resourceType
     * @return The resource described by name and type.
     */
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
