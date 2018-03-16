package org.apache.camel.util;

/**
 * Represents a thread safe map of values which timeout after a period of
 * inactivity.
 *
 * @version $Revision: 659798 $
 */
public interface TimeoutMap extends Runnable {

    /**
     * Looks up the value in the map by the given key.
     *
     * @param key the key of the value to search for
     * @return the value for the given key or null if it is not present (or has timed out)
     */
    Object get(Object key);

    /**
     * Returns a copy of the keys in the map
     */
    Object[] getKeys();

    /**
     * Returns the size of the map
     */
    int size();

    /**
     * Adds the key value pair into the map such that some time after the given
     * timeout the entry will be evicted
     */
    void put(Object key, Object value, long timeoutMillis);

    /**
     * Removes the object with the given key
     *
     * @param key  key for the object to remove
     */
    void remove(Object key);

    /**
     * Purges any old entries from the map
     */
    void purge();
}
