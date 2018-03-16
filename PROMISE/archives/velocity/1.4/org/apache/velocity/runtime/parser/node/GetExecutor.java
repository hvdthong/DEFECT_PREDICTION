import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.util.introspection.Introspector;

import java.lang.reflect.InvocationTargetException;
import org.apache.velocity.exception.MethodInvocationException;

import org.apache.velocity.runtime.RuntimeLogger;


/**
 * Executor that simply tries to execute a get(key)
 * operation. This will try to find a get(key) method
 * for any type of object, not just objects that
 * implement the Map interface as was previously
 * the case.
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @version $Id: GetExecutor.java 75955 2004-03-03 23:23:08Z geirm $
 */
public class GetExecutor extends AbstractExecutor
{
    /**
     * Container to hold the 'key' part of 
     * get(key).
     */
    private Object[] args = new Object[1];
    
    /**
     * Default constructor.
     */
    public GetExecutor(RuntimeLogger r, Introspector ispect, Class c, String key)
        throws Exception
    {
        rlog = r;
        args[0] = key;
        method = ispect.getMethod(c, "get", args);
    }

    /**
     * Execute method against context.
     */
    public Object execute(Object o)
        throws IllegalAccessException, InvocationTargetException
    {
        if (method == null)
            return null;

        return method.invoke(o, args);
    }

    /**
     * Execute method against context.
     */
    public Object OLDexecute(Object o, InternalContextAdapter context)
        throws IllegalAccessException, MethodInvocationException
    {
        if (method == null)
            return null;
     
        try 
        {
            return method.invoke(o, args);  
        }
        catch(InvocationTargetException ite)
        {
            /*
             *  the method we invoked threw an exception.
             *  package and pass it up
             */

            throw  new MethodInvocationException( 
                "Invocation of method 'get(\"" + args[0] + "\")'" 
                + " in  " + o.getClass() 
                + " threw exception " 
                + ite.getTargetException().getClass(), 
                ite.getTargetException(), "get");
        }
        catch(IllegalArgumentException iae)
        {
            return null;
        }
    }
}









