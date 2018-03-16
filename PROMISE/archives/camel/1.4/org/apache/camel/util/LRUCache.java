package org.apache.camel.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A Least Recently Used Cache
 *
 * @version $Revision: 655284 $
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private static final long serialVersionUID = -342098639681884413L;
    private int maxCacheSize = 10000;

    public LRUCache(int maximumCacheSize) {
        this(maximumCacheSize, maximumCacheSize, 0.75f, true);
    }

    /**
     * Constructs an empty <tt>LRUCache</tt> instance with the
     * specified initial capacity, maximumCacheSize,load factor and ordering mode.
     *
     * @param initialCapacity  the initial capacity.
     * @param maximumCacheSize
     * @param loadFactor       the load factor.
     * @param accessOrder      the ordering mode - <tt>true</tt> for
     *                         access-order, <tt>false</tt> for insertion-order.
     * @throws IllegalArgumentException if the initial capacity is negative
     *                                  or the load factor is non positive.
     */
    public LRUCache(int initialCapacity, int maximumCacheSize, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
        this.maxCacheSize = maximumCacheSize;
    }

    /**
     * Returns the maxCacheSize.
     */
    public int getMaxCacheSize() {
        return maxCacheSize;
    }

    protected boolean removeEldestEntry(Map.Entry entry) {
        return size() > maxCacheSize;
    }
}
