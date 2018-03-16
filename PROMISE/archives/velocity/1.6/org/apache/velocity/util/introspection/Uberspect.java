import java.util.Iterator;

/**
 * 'Federated' introspection/reflection interface to allow the introspection
 *  behavior in Velocity to be customized.
 *
 * @author <a href="mailto:geirm@apache.org">Geir Magusson Jr.</a>
 * @version $Id: Uberspect.java 463298 2006-10-12 16:10:32Z henning $
 */
public interface Uberspect
{
    /**
     *  Initializer - will be called before use
     * @throws Exception
     */
    public void init() throws Exception;

    /**
     *  To support iteratives - #foreach()
     * @param obj
     * @param info
     * @return An Iterator.
     * @throws Exception
     */
    public Iterator getIterator(Object obj, Info info) throws Exception;

    /**
     *  Returns a general method, corresponding to $foo.bar( $woogie )
     * @param obj
     * @param method
     * @param args
     * @param info
     * @return A Velocity Method.
     * @throws Exception
     */
    public VelMethod getMethod(Object obj, String method, Object[] args, Info info) throws Exception;

    /**
     * Property getter - returns VelPropertyGet appropos for #set($foo = $bar.woogie)
     * @param obj
     * @param identifier
     * @param info
     * @return A Velocity Getter.
     * @throws Exception
     */
    public VelPropertyGet getPropertyGet(Object obj, String identifier, Info info) throws Exception;

    /**
     * Property setter - returns VelPropertySet appropos for #set($foo.bar = "geir")
     * @param obj
     * @param identifier
     * @param arg
     * @param info
     * @return A Velocity Setter.
     * @throws Exception
     */
    public VelPropertySet getPropertySet(Object obj, String identifier, Object arg, Info info) throws Exception;
}
