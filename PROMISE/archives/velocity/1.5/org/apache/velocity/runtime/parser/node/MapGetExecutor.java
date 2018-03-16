import java.util.Map;

import org.apache.velocity.runtime.log.Log;

/**
 * GetExecutor that is smart about Maps. If it detects one, it does not
 * use Reflection but a cast to access the getter. 
 *
 * @author <a href="mailto:henning@apache.org">Henning P. Schmiedehausen</a>
 * @version $Id: MapGetExecutor.java 463298 2006-10-12 16:10:32Z henning $
 */
public class MapGetExecutor
        extends AbstractExecutor 
{
    private final String property;

    public MapGetExecutor(final Log log, final Class clazz, final String property)
    {
        this.log = log;
        this.property = property;
        discover(clazz);
    }

    protected void discover (final Class clazz)
    {
        Class [] interfaces = clazz.getInterfaces();
        for (int i = 0 ; i < interfaces.length; i++)
        {
            if (interfaces[i].equals(Map.class))
            {
                try
                {
                    if (property != null)
                    {
                        setMethod(Map.class.getMethod("get", new Class [] { Object.class }));
                    }
                }
                /**
                 * pass through application level runtime exceptions
                 */
                catch( RuntimeException e )
                {
                    throw e;
                }
                catch(Exception e)
                {
                    log.error("While looking for get('" + property + "') method:", e);
                }
                break;
            }
        }
    }

    public Object execute(final Object o)
    {
        return ((Map) o).get(property);
    } 
}
