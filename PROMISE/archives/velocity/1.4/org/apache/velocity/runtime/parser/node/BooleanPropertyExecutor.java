import org.apache.velocity.util.introspection.Introspector;

import org.apache.velocity.runtime.RuntimeLogger;

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
 *  @version $Id: BooleanPropertyExecutor.java 75955 2004-03-03 23:23:08Z geirm $
 */
public class BooleanPropertyExecutor extends PropertyExecutor
{
    public BooleanPropertyExecutor(RuntimeLogger rlog, Introspector is, Class clazz, String property)
    {
        super(rlog, is, clazz, property);
    }

    protected void discover(Class clazz, String property)
    {
        try
        {
            char c;
            StringBuffer sb;

            Object[] params = {  };

            /*
             *  now look for a boolean isFoo
             */

            sb = new StringBuffer("is");
            sb.append(property);

            c = sb.charAt(2);

            if (Character.isLowerCase(c))
            {
                sb.setCharAt(2, Character.toUpperCase(c));
            }

            methodUsed = sb.toString();
            method = introspector.getMethod(clazz, methodUsed, params);

            if (method != null)
            {
                /*
                 *  now, this has to return a boolean
                 */

                if (method.getReturnType() == Boolean.TYPE)
                    return;

                method = null;
            }
        }
        catch(Exception e)
        {
            rlog.error("PROGRAMMER ERROR : BooleanPropertyExector() : " + e);
        }
    }
}
