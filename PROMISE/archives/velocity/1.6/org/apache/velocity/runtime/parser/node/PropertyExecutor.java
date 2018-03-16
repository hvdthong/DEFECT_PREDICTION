import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.runtime.RuntimeLogger;
import org.apache.velocity.runtime.log.Log;
import org.apache.velocity.runtime.log.RuntimeLoggerLog;
import org.apache.velocity.util.introspection.Introspector;

/**
 * Returned the value of object property when executed.
 */
public class PropertyExecutor extends AbstractExecutor
{
    private final Introspector introspector;

    /**
     * @param log
     * @param introspector
     * @param clazz
     * @param property
     * @since 1.5
     */
    public PropertyExecutor(final Log log, final Introspector introspector,
            final Class clazz, final String property)
    {
        this.log = log;
        this.introspector = introspector;

        if (StringUtils.isNotEmpty(property))
        {
            discover(clazz, property);
        }
    }

    /**
     * @param r
     * @param introspector
     * @param clazz
     * @param property
     * @deprecated RuntimeLogger is deprecated. Use the other constructor.
     */
    public PropertyExecutor(final RuntimeLogger r, final Introspector introspector,
            final Class clazz, final String property)
    {
        this(new RuntimeLoggerLog(r), introspector, clazz, property);
    }

    /**
     * @return The current introspector.
     * @since 1.5
     */
    protected Introspector getIntrospector()
    {
        return this.introspector;
    }

    /**
     * @param clazz
     * @param property
     */
    protected void discover(final Class clazz, final String property)
    {
        /*
         *  this is gross and linear, but it keeps it straightforward.
         */

        try
        {
            Object [] params = {};

            StringBuffer sb = new StringBuffer("get");
            sb.append(property);

            setMethod(introspector.getMethod(clazz, sb.toString(), params));

            if (!isAlive())
            {
                /*
                 *  now the convenience, flip the 1st character
                 */

                char c = sb.charAt(3);

                if (Character.isLowerCase(c))
                {
                    sb.setCharAt(3, Character.toUpperCase(c));
                }
                else
                {
                    sb.setCharAt(3, Character.toLowerCase(c));
                }

                setMethod(introspector.getMethod(clazz, sb.toString(), params));
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
            String msg = "Exception while looking for property getter for '" + property;
            log.error(msg, e);
            throw new VelocityException(msg, e);
        }
    }

    /**
     * @see org.apache.velocity.runtime.parser.node.AbstractExecutor#execute(java.lang.Object)
     */
    public Object execute(Object o)
        throws IllegalAccessException,  InvocationTargetException
    {
        return isAlive() ? getMethod().invoke(o, ((Object []) null)) : null;
    }
}
