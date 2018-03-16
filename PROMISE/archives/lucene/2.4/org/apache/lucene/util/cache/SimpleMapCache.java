import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Simple cache implementation that uses a HashMap to store (key, value) pairs.
 * This cache is not synchronized, use {@link Cache#synchronizedCache(Cache)}
 * if needed.
 */
public class SimpleMapCache extends Cache {
  Map map;
  
  public SimpleMapCache() {
    this(new HashMap());
  }

  public SimpleMapCache(Map map) {
    this.map = map;
  }
  
  public Object get(Object key) {
    return map.get(key);
  }

  public void put(Object key, Object value) {
    map.put(key, value);
  }

  public void close() {
  }

  public boolean containsKey(Object key) {
    return map.containsKey(key);
  }
  
  /**
   * Returns a Set containing all keys in this cache.
   */
  public Set keySet() {
    return map.keySet();
  }
  
  Cache getSynchronizedCache() {
    return new SynchronizedSimpleMapCache(this);
  }
  
  private static class SynchronizedSimpleMapCache extends SimpleMapCache {
    Object mutex;
    SimpleMapCache cache;
    
    SynchronizedSimpleMapCache(SimpleMapCache cache) {
        this.cache = cache;
        this.mutex = this;
    }
    
    public void put(Object key, Object value) {
        synchronized(mutex) {cache.put(key, value);}
    }
    
    public Object get(Object key) {
        synchronized(mutex) {return cache.get(key);}
    }
    
    public boolean containsKey(Object key) {
        synchronized(mutex) {return cache.containsKey(key);}
    }
    
    public void close() {
        synchronized(mutex) {cache.close();}
    }
    
    public Set keySet() {
      synchronized(mutex) {return cache.keySet();}
    }
    
    Cache getSynchronizedCache() {
      return this;
    }
  }
}
