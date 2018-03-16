public interface IntrospectorCache {

    /**
     * Clears the internal cache.
     */
    void clear();

    /**
     * Lookup a given Class object in the cache. If it does not exist, 
     * check whether this is due to a class change and purge the caches
     * eventually.
     *
     * @param c The class to look up.
     * @return A ClassMap object or null if it does not exist in the cache.
     */
    ClassMap get(Class c);

    /**
     * Creates a class map for specific class and registers it in the
     * cache.  Also adds the qualified name to the name-&gt;class map
     * for later Classloader change detection.
     *
     * @param c The class for which the class map gets generated.
     * @return A ClassMap object.
     */
    ClassMap put(Class c);

    /**
     * Register a Cache listener.
     *
     * @param listener A Cache listener object.
     */
    void addListener(IntrospectorCacheListener listener);

    /**
     * Remove a Cache listener.
     *
     * @param listener A Cache listener object.
     */
    void removeListener(IntrospectorCacheListener listener);

}
