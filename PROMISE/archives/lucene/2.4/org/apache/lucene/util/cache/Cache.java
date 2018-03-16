public abstract class Cache {
  
  /**
   * Simple Cache wrapper that synchronizes all
   * calls that access the cache. 
   */
  static class SynchronizedCache extends Cache {
    Object mutex;
    Cache  cache;
    
    SynchronizedCache(Cache cache) {
      this.cache = cache;
      this.mutex = this;
    }
    
    SynchronizedCache(Cache cache, Object mutex) {
      this.cache = cache;
      this.mutex = mutex;
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
    
    Cache getSynchronizedCache() {
      return this;
    }
  }
  
  /**
   * Returns a thread-safe cache backed by the specified cache. 
   * In order to guarantee thread-safety, all access to the backed cache must
   * be accomplished through the returned cache.
   */
  public static Cache synchronizedCache(Cache cache) {
    return cache.getSynchronizedCache();
  }

  /**
   * Called by {@link #synchronizedCache(Cache)}. This method
   * returns a {@link SynchronizedCache} instance that wraps
   * this instance by default and can be overridden to return
   * e. g. subclasses of {@link SynchronizedCache} or this
   * in case this cache is already synchronized.
   */
  Cache getSynchronizedCache() {
    return new SynchronizedCache(this);
  }
  
  /**
   * Puts a (key, value)-pair into the cache. 
   */
  public abstract void put(Object key, Object value);
  
  /**
   * Returns the value for the given key. 
   */
  public abstract Object get(Object key);
  
  /**
   * Returns whether the given key is in this cache. 
   */
  public abstract boolean containsKey(Object key);
  
  /**
   * Closes the cache.
   */
  public abstract void close();
  
}
