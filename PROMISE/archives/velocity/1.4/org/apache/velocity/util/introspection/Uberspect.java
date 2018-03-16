import org.apache.velocity.runtime.RuntimeLogger;

import java.util.Iterator;
import java.lang.reflect.Method;

/**
 * 'Federated' introspection/reflection interface to allow the introspection
 *  behavior in Velocity to be customized.
 *
 * @author <a href="mailto:geirm@apache.org">Geir Magusson Jr.</a>
 * @version $Id: Uberspect.java 75955 2004-03-03 23:23:08Z geirm $
 */
public interface Uberspect
{
    /**
     *  Initializer - will be called before use
     */
    public void init() throws Exception;

    /**
     *  To support iteratives - #foreach()
     */
    public Iterator getIterator(Object obj, Info info) throws Exception;

    /**
     *  Returns a general method, corresponding to $foo.bar( $woogie )
     */
    public VelMethod getMethod(Object obj, String method, Object[] args, Info info) throws Exception;

    /**
     * Property getter - returns VelPropertyGet appropos for #set($foo = $bar.woogie)
     */
    public VelPropertyGet getPropertyGet(Object obj, String identifier, Info info) throws Exception;

    /**
     * Property setter - returns VelPropertySet appropos for #set($foo.bar = "geir")
     */
    public VelPropertySet getPropertySet(Object obj, String identifier, Object arg, Info info) throws Exception;
}
