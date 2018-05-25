import org.apache.velocity.runtime.RuntimeLogger;
import org.apache.velocity.runtime.log.Log;
import org.apache.velocity.runtime.log.RuntimeLoggerLog;
import org.apache.velocity.util.introspection.Introspector;

/**
 *  Handles discovery and valuation of a
 *  boolean object property, of the
 *  form public boolean is<property> when executed.
 *
 *  We do this separately as to preserve the current
 *  quasi-broken semantics of get<as is property>
 *  get< flip 1st char> get("property") and now followed
 *  by is<Property>
 *
 *  @author <a href="geirm@apache.org">Geir Magnusson Jr.</a>
 *  @version $Id: BooleanPropertyExecutor.java 463298 2006-10-12 16:10:32Z henning $
 */
public class BooleanPropertyExecutor extends PropertyExecutor
{
    /**
     * @param log
     * @param introspector
     * @param clazz
     * @param property
     */
    public BooleanPropertyExecutor(final Log log, final Introspector introspector,
            final Class clazz, final String property)
    {
        super(log, introspector, clazz, property);
    }

    /**
     * @param rlog
     * @param introspector
     * @param clazz
     * @param property
     * @deprecated RuntimeLogger is deprecated. Use the other constructor.
     */
    public BooleanPropertyExecutor(final RuntimeLogger rlog, final Introspector introspector,
            final Class clazz, final String property)
    {
        super(new RuntimeLoggerLog(rlog), introspector, clazz, property);
    }

    protected void discover(final Class clazz, final String property)
    {
        try
        {
            Object [] params = {};

            StringBuffer sb = new StringBuffer("is");
            sb.append(property);

            setMethod(getIntrospector().getMethod(clazz, sb.toString(), params));

            if (!isAlive())
            {
                /*
                 *  now the convenience, flip the 1st character
                 */

                char c = sb.charAt(2);

                if (Character.isLowerCase(c))
                {
                    sb.setCharAt(2, Character.toUpperCase(c));
                }
                else
                {
                    sb.setCharAt(2, Character.toLowerCase(c));
                }

                setMethod(getIntrospector().getMethod(clazz, sb.toString(), params));
            }

            if (isAlive())
            {
                if (getMethod().getReturnType() != Boolean.TYPE)
                {
                }
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
            log.error("While looking for boolean property getter for '" + property + "':", e);
        }
    }
}