import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;
import java.util.Iterator;

import org.apache.commons.collections.map.LRUMap;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.RuntimeServices;

/**
 * Default implementation of the resource cache for the default
 * ResourceManager.  The cache uses a <i>least recently used</i> (LRU)
 * algorithm, with a maximum size specified via the
 * <code>resource.manager.cache.size</code> property (idenfied by the
 * {@link
 * org.apache.velocity.runtime.RuntimeConstants#RESOURCE_MANAGER_DEFAULTCACHE_SIZE}
 * constant).  This property get be set to <code>0</code> or less for
 * a greedy, unbounded cache (the behavior from pre-v1.5).
 *
 * @author <a href="mailto:geirm@apache.org">Geir Magnusson Jr.</a>
 * @author <a href="mailto:dlr@finemaltcoding.com">Daniel Rall</a>
 * @version $Id: ResourceCacheImpl.java 463298 2006-10-12 16:10:32Z henning $
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

    /**
     * @see org.apache.velocity.runtime.resource.ResourceCache#initialize(org.apache.velocity.runtime.RuntimeServices)
     */
    public void initialize( RuntimeServices rs )
    {
        rsvc = rs;

        int maxSize =
            rsvc.getInt(RuntimeConstants.RESOURCE_MANAGER_DEFAULTCACHE_SIZE, 89);
        if (maxSize > 0)
        {
            Map lruCache = Collections.synchronizedMap(new LRUMap(maxSize));
            lruCache.putAll(cache);
            cache = lruCache;
        }
        rsvc.getLog().debug("ResourceCache: initialized ("+this.getClass()+')');
    }

    /**
     * @see org.apache.velocity.runtime.resource.ResourceCache#get(java.lang.Object)
     */
    public Resource get( Object key )
    {
        return (Resource) cache.get( key );
    }

    /**
     * @see org.apache.velocity.runtime.resource.ResourceCache#put(java.lang.Object, org.apache.velocity.runtime.resource.Resource)
     */
    public Resource put( Object key, Resource value )
    {
        return (Resource) cache.put( key, value );
    }

    /**
     * @see org.apache.velocity.runtime.resource.ResourceCache#remove(java.lang.Object)
     */
    public Resource remove( Object key )
    {
        return (Resource) cache.remove( key );
    }

    /**
     * @see org.apache.velocity.runtime.resource.ResourceCache#enumerateKeys()
     */
    public Iterator enumerateKeys()
    {
        return cache.keySet().iterator();
    }
}
