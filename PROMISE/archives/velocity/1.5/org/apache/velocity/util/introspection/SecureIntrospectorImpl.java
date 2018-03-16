import java.lang.reflect.Method;

import org.apache.velocity.runtime.log.Log;

/**
 * <p>Prevent "dangerous" classloader/reflection related calls.  Use this 
 * introspector for situations in which template writers are numerous
 * or untrusted.  Specifically, this introspector prevents creation of
 * arbitrary objects and prevents reflection on objects.
 * 
 * <p>See documentation of checkObjectExecutePermission() for 
 * more information on specific classes and methods blocked.
 * 
 * @author <a href="mailto:wglass@forio.com">Will Glass-Husain</a>
 * @version $Id: SecureIntrospectorImpl.java 509906 2007-02-21 06:11:05Z wglass $
 */
public class SecureIntrospectorImpl extends Introspector implements SecureIntrospectorControl
{
    private String[] badClasses;
    private String[] badPackages;
    
    public SecureIntrospectorImpl(String[] badClasses, String[] badPackages, Log log)
    {
        super(log);
        this.badClasses = badClasses;
        this.badPackages = badPackages;
    }
    
    /**
     * Get the Method object corresponding to the given class, name and parameters.  
     * Will check for appropriate execute permissions and return null if the method
     * is not allowed to be executed.
     * 
     * @param clazz Class on which method will be called
     * @param methodName Name of method to be called
     * @param params array of parameters to method
     * @return Method object retrieved by Introspector
     * @throws IllegalArgumentException The parameter passed in were incorrect.
     */
    public Method getMethod(Class clazz, String methodName, Object[] params) throws IllegalArgumentException
    {
        if (!checkObjectExecutePermission(clazz,methodName))
        {
            log.warn ("Cannot retrieve method " + methodName + 
                      " from object of class " + clazz.getName() +
                      " due to security restrictions.");
            return null;
            
        }
        else
        {
            return super.getMethod(clazz, methodName, params);
        }
    }
    
    /**
     * Determine which methods and classes to prevent from executing.  Always blocks
     * methods wait() and notify().  Always allows methods on Number, Boolean, and String.
     * Prohibits method calls on classes related to reflection and system operations.
     * For the complete list, see the properties <code>introspector.restrict.classes</code>
     * and <code>introspector.restrict.packages</code>.
     * 
     * @param clazz Class on which method will be called
     * @param methodName Name of method to be called
     * @see org.apache.velocity.util.introspection.SecureIntrospectorControl#checkObjectExecutePermission(java.lang.Class, java.lang.String)
     */
    public boolean checkObjectExecutePermission(Class clazz, String methodName)
    {
        
        /**
         * check for wait and notify 
         */
        if ( (methodName != null) && (methodName.equals("wait") || methodName.equals("notify")) )
        {
            return false;
        }
        
        /**
         * Always allow the most common classes - Number, Boolean and String
         */
        else if (java.lang.Number.class.isAssignableFrom(clazz))
        {
            return true;
        }
        
        else if (java.lang.Boolean.class.isAssignableFrom(clazz))
        {
            return true;
        }
        
        else if (java.lang.String.class.isAssignableFrom(clazz))
        {
            return true;
        }
        

        /**
         * Always allow Class.getName()
         */
        else if (java.lang.Class.class.isAssignableFrom(clazz) && (methodName != null) && methodName.equals("getName"))
        {
            return true;
        }
        
        /**
         * check the classname (minus any array info)
         * whether it matches disallowed classes or packages
         */ 
        String className = clazz.getName();
        if (className.startsWith("[L") && className.endsWith(";"))
        {
            className = className.substring(2,className.length() - 1);
        }
        
        String packageName;
        int dotPos = className.lastIndexOf('.');
        packageName = (dotPos == -1) ? "" : className.substring(0,dotPos);
        
        int sz = badPackages.length;
        for (int i = 0; i < sz; i++)
        {
            if (packageName.equals(badPackages[i]))
            {
                return false;
            }
        }
        
        sz = badClasses.length;
        for (int i = 0; i < sz; i++)
        {
            if (className.equals(badClasses[i]))
            {
                return false;
            }
        }
        
        return true;
    }
}
