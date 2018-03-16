import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.runtime.log.Log;
import org.apache.velocity.util.introspection.Introspector;

/**
 * Executor for looking up property names in the passed in class
 * This will try to find a set&lt;foo&gt;(key, value) method
 *
 * @author <a href="mailto:henning@apache.org">Henning P. Schmiedehausen</a>
 * @version $Id: SetPropertyExecutor.java 463298 2006-10-12 16:10:32Z henning $
 */
public class SetPropertyExecutor
        extends SetExecutor
{
    private final Introspector introspector;

    /**
     * @param log
     * @param introspector
     * @param clazz
     * @param property
     * @param arg
     */
    public SetPropertyExecutor(final Log log, final Introspector introspector,
            final Class clazz, final String property, final Object arg)
    {
        this.log = log;
        this.introspector = introspector;

        if (StringUtils.isNotEmpty(property))
        {
            discover(clazz, property, arg);
        }
    }

    /**
     * @return The current introspector.
     */
    protected Introspector getIntrospector()
    {
        return this.introspector;
    }

    /**
     * @param clazz
     * @param property
     * @param arg
     */
    protected void discover(final Class clazz, final String property, final Object arg)
    {
        Object [] params = new Object [] { arg };

        try
        {
            StringBuffer sb = new StringBuffer("set");
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
            log.error("While looking for property setter for '" + property + "':", e);
        }
    }

    /**
     * Execute method against context.
     * @param o
     * @param value
     * @return The value of the invocation.
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public Object execute(final Object o, final Object value)
        throws IllegalAccessException,  InvocationTargetException
    {
        Object [] params = new Object [] { value };
        return isAlive() ? getMethod().invoke(o, params) : null;
    }
}
