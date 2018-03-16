import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Factory class for creating Maps.
 * 
 * The main purpose of this class is to take advantage of Java 5
 * Concurrent classes if they are available. We use reflection to instantiate
 * java.util.concurrent classes to avoid compile time dependency on Java 5.
 * 
 * for more info on this class.
 * @author <a href="mailto:wyla@sci.fi">Jarkko Viinamaki</a>
 * @since 1.6
 */
public class MapFactory
{
    private static Constructor concurrentHashMapConstructor;
    static
    {
        try
        {
            concurrentHashMapConstructor =
                Class.forName("java.util.concurrent.ConcurrentHashMap")
                     .getConstructor(new Class[] { int.class, float.class, int.class } );
        }
        catch (Exception ex)
        {
        }
    }
    
    /**
     * Creates a new instance of a class that implements Map interface.
     * 
     * Note that there is a small performance penalty because concurrent
     * maps are created using reflection.
     * 
     * @param size initial size of the map
     * @param loadFactor smaller value = better performance, 
     *          larger value = better memory utilization
     * @param concurrencyLevel estimated number of writer Threads. 
     *          If this is smaller than 1, HashMap is always returned which is not 
     *          threadsafe.
     * @param allowNullKeys if true, the returned Map instance supports null keys         
     *          
     * @return one of ConcurrentHashMap, HashMap, Hashtable
     */
    public static Map create(int size, float loadFactor,
                             int concurrencyLevel, boolean allowNullKeys)
    {
        Map map = null;
        if (concurrencyLevel <= 1)
        {
            map = new HashMap(size, loadFactor);
        }
        else
        {
            if (concurrentHashMapConstructor != null)
            {
                try
                {
                    map = (Map)concurrentHashMapConstructor.newInstance(
                        new Object[] { new Integer(size), new Float(loadFactor), new Integer(concurrencyLevel) });
                }
                catch (Exception ex)
                {
                    throw new RuntimeException("this should not happen", ex);
                }
            }
            else
            {
                /*
                 * Hashtable should be faster than
                 * Collections.synchronizedMap(new HashMap());
                 * so favor it if there is no need for null key support
                 */
                if (allowNullKeys)
                {
                    map = Collections.synchronizedMap(new HashMap(size, loadFactor));
                }
                else
                {
                    map = new Hashtable(size, loadFactor);
                }
            }
        }
        return map;
    }
}
