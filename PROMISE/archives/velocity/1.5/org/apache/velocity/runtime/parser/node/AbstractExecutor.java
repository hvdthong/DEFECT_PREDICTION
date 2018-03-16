import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.velocity.runtime.log.Log;

/**
 * Abstract class that is used to execute an arbitrary
 * method that is in introspected. This is the superclass
 * for the GetExecutor and PropertyExecutor.
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:geirm@apache.org">Geir Magnusson Jr.</a>
 * @version $Id: AbstractExecutor.java 463298 2006-10-12 16:10:32Z henning $
 */
public abstract class AbstractExecutor
{
    /** */
    protected Log log = null;

    /**
     * Method to be executed.
     */
    private Method method = null;

    /**
     * Execute method against context.
     * @param o
     * @return The resulting object.
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
     public abstract Object execute(Object o)
         throws IllegalAccessException, InvocationTargetException;

    /**
     * Tell whether the executor is alive by looking
     * at the value of the method.
     *
     * @return True if executor is alive.
     */
    public boolean isAlive()
    {
        return (method != null);
    }

    /**
     * @return The current method.
     */
    public Method getMethod()
    {
        return method;
    }

    /**
     * @param method
     */
    protected void setMethod(final Method method)
    {
        this.method = method;
    }
}
