import java.lang.reflect.InvocationTargetException;

import org.apache.velocity.runtime.log.Log;
import org.apache.velocity.util.introspection.Introspector;


/**
 * Executor that simply tries to execute a put(key, value)
 * operation. This will try to find a put(key) method
 * for any type of object, not just objects that
 * implement the Map interface as was previously
 * the case.
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:henning@apache.org">Henning P. Schmiedehausen</a>
 * @version $Id: PutExecutor.java 463298 2006-10-12 16:10:32Z henning $
 */
public class PutExecutor extends SetExecutor
{
    private final Introspector introspector;
    private final String property;

    /**
     * @param log
     * @param introspector
     * @param clazz
     * @param arg
     * @param property
     */
    public PutExecutor(final Log log, final Introspector introspector,
            final Class clazz, final Object arg, final String property)
    {
        this.log = log;
        this.introspector = introspector;
        this.property = property;

        discover(clazz, arg);
    }

    /**
     * @param clazz
     * @param arg
     */
    protected void discover(final Class clazz, final Object arg)
    {
        Object [] params;


        if (property == null)
        {
            params = new Object[] { arg };
        }
        else
        {
            params = new Object[] { property, arg };
        }

        try
        {
            setMethod(introspector.getMethod(clazz, "put", params));
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
            log.error("While looking for put('" + params[0] + "') method:", e);
        }
    }

    /**
     * @see org.apache.velocity.runtime.parser.node.SetExecutor#execute(java.lang.Object, java.lang.Object)
     */
    public Object execute(final Object o, final Object value)
        throws IllegalAccessException,  InvocationTargetException
    {
        Object [] params;

        if (isAlive())
        {
            if (property == null)
            {
                params = new Object [] { value };
            }
            else
            {
                params = new Object [] { property, value };
            }

            return getMethod().invoke(o, params);
        }

        return null;
    }
}
