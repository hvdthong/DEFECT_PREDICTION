import java.util.Hashtable;
import java.util.Map;
import java.util.Iterator;
import org.apache.velocity.runtime.RuntimeServices;

/**
 * Default implementation of the resource cache for the default
 * ResourceManager.
 *
 * @author <a href="mailto:geirm@apache.org">Geir Magnusson Jr.</a>
 * @author <a href="mailto:dlr@finemaltcoding.com">Daniel Rall</a>
 * @version $Id: ResourceCacheImpl.java 75955 2004-03-03 23:23:08Z geirm $
 */
public class ResourceCacheImpl implements ResourceCache
{
    /**
     * Cache storage, assumed to be thread-safe.
     */
    protected Map cache = new Hashtable();

    /**
     * Runtime services, generally initialized by the
     * <code>initialize()</code> method.
     */
    protected RuntimeServices rsvc = null;
    
    public void initialize( RuntimeServices rs )
    {
        rsvc = rs;
        
        rsvc.info("ResourceCache : initialized. (" + this.getClass() + ")");
    }
    
    public Resource get( Object key )
    {
        return (Resource) cache.get( key );
    }
    
    public Resource put( Object key, Resource value )
    {
        return (Resource) cache.put( key, value );
    }
    
    public Resource remove( Object key )
    {
        return (Resource) cache.remove( key );
    }
    
    public Iterator enumerateKeys()
    {
        return cache.keySet().iterator();
    }
}
