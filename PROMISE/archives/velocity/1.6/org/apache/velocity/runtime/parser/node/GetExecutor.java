import java.lang.reflect.InvocationTargetException;
import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.runtime.RuntimeLogger;
import org.apache.velocity.runtime.log.Log;
import org.apache.velocity.runtime.log.RuntimeLoggerLog;
import org.apache.velocity.util.introspection.Introspector;


/**
 * Executor that simply tries to execute a get(key)
 * operation. This will try to find a get(key) method
 * for any type of object, not just objects that
 * implement the Map interface as was previously
 * the case.
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @version $Id: GetExecutor.java 687177 2008-08-19 22:00:32Z nbubna $
 */
public class GetExecutor extends AbstractExecutor
{
    private final Introspector introspector;

    private Object [] params = {};

    /**
     * @param log
     * @param introspector
     * @param clazz
     * @param property
     * @since 1.5
     */
    public GetExecutor(final Log log, final Introspector introspector,
            final Class clazz, final String property)
    {
        this.log = log;
        this.introspector = introspector;


        if (property != null)
        {
            this.params = new Object[] { property };
        }
        discover(clazz);
    }

    /**
     * @param rlog
     * @param introspector
     * @param clazz
     * @param property
     * @deprecated RuntimeLogger is deprecated. Use the other constructor.
     */
    public GetExecutor(final RuntimeLogger rlog, final Introspector introspector,
            final Class clazz, final String property)
    {
        this(new RuntimeLoggerLog(rlog), introspector, clazz, property);
    }

    /**
     * @since 1.5
     */
    protected void discover(final Class clazz)
    {
        try
        {
            setMethod(introspector.getMethod(clazz, "get", params));
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
            String msg = "Exception while looking for get('" + params[0] + "') method";
            log.error(msg, e);
            throw new VelocityException(msg, e);
        }
    }

    /**
     * @see org.apache.velocity.runtime.parser.node.AbstractExecutor#execute(java.lang.Object)
     */
    public Object execute(final Object o)
        throws IllegalAccessException,  InvocationTargetException
    {
        return isAlive() ? getMethod().invoke(o, params) : null;
    }
}
