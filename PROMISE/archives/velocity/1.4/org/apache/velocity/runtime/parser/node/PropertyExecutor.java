import java.lang.reflect.InvocationTargetException;

import org.apache.velocity.util.introspection.Introspector;

import org.apache.velocity.runtime.RuntimeLogger;

/**
 * Returned the value of object property when executed.
 */
public class PropertyExecutor extends AbstractExecutor
{
    protected Introspector introspector = null;

    protected String methodUsed = null;

    public PropertyExecutor(RuntimeLogger r, Introspector ispctr,
                            Class clazz, String property)
    {
        rlog = r;
        introspector = ispctr;

        discover(clazz, property);
    }

    protected void discover(Class clazz, String property)
    {
        /*
         *  this is gross and linear, but it keeps it straightforward.
         */

        try
        {
            char c;
            StringBuffer sb;

            Object[] params = {  };

            /*
             *  start with get<property>
             *  this leaves the property name 
             *  as is...
             */
            sb = new StringBuffer("get");
            sb.append(property);

            methodUsed = sb.toString();

            method = introspector.getMethod(clazz, methodUsed, params);
             
            if (method != null)
                return;
        
            /*
             *  now the convenience, flip the 1st character
             */
         
            sb = new StringBuffer("get");
            sb.append(property);

            c = sb.charAt(3);

            if (Character.isLowerCase(c))
            {
                sb.setCharAt(3, Character.toUpperCase(c));
            }
            else
            {
                sb.setCharAt(3, Character.toLowerCase(c));
            }

            methodUsed = sb.toString();
            method = introspector.getMethod(clazz, methodUsed, params);

            if (method != null)
                return; 
            
        }
        catch(Exception e)
        {
            rlog.error("PROGRAMMER ERROR : PropertyExector() : " + e );
        }
    }


    /**
     * Execute method against context.
     */
    public Object execute(Object o)
        throws IllegalAccessException,  InvocationTargetException
    {
        if (method == null)
            return null;

        return method.invoke(o, null);
    }
}


