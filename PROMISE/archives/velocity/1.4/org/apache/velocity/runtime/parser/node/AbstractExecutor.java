import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.velocity.runtime.RuntimeLogger;

/**
 * Abstract class that is used to execute an arbitrary
 * method that is in introspected. This is the superclass
 * for the GetExecutor and PropertyExecutor.
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:geirm@apache.org">Geir Magnusson Jr.</a>
 * @version $Id: AbstractExecutor.java 75955 2004-03-03 23:23:08Z geirm $
 */
public abstract class AbstractExecutor
{
    protected RuntimeLogger rlog = null;
    
    /**
     * Method to be executed.
     */
    protected Method method = null;
    
    /**
     * Execute method against context.
     */
     public abstract Object execute(Object o)
         throws IllegalAccessException, InvocationTargetException;

    /**
     * Tell whether the executor is alive by looking
     * at the value of the method.
     */
    public boolean isAlive()
    {
        return (method != null);
    }

    public Method getMethod()
    {
        return method;
    }
}
