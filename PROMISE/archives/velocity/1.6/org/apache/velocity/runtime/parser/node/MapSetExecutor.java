import java.util.Map;
import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.runtime.log.Log;

/**
 * SetExecutor that is smart about Maps. If it detects one, it does not
 * use Reflection but a cast to access the setter. 
 *
 * @author <a href="mailto:henning@apache.org">Henning P. Schmiedehausen</a>
 * @version $Id: MapSetExecutor.java 687177 2008-08-19 22:00:32Z nbubna $
 * @since 1.5
 */
public class MapSetExecutor
        extends SetExecutor 
{
    private final String property;

    public MapSetExecutor(final Log log, final Class clazz, final String property)
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
                        setMethod(Map.class.getMethod("put", new Class [] { Object.class, Object.class }));
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
                    String msg = "Exception while looking for put('" + property + "') method";
                    log.error(msg, e);
                    throw new VelocityException(msg, e);
                }
                break;
            }
        }
    }

    public Object execute(final Object o, final Object arg)
    {
        return ((Map) o).put(property, arg);
    } 
}
