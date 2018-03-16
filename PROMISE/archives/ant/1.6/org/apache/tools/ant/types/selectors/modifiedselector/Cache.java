package org.apache.tools.ant.types.selectors.modifiedselector;


import java.util.Iterator;


/**
 * A Cache let the user store key-value-pairs in a permanent manner and access
 * them.
 * It is possible that a client uses get() before load() therefore the
 * implementation must ensure that no error occurred because of the wrong
 * <i>order</i>.
 * The implementing class should implement a useful toString() method.
 *
 * @version 2003-09-13
 * @since  Ant 1.6
 */
public interface Cache {

    /**
     * Checks its prerequisites.
     * @return <i>true</i> if all is ok, otherwise <i>false</i>.
     */
    boolean isValid();

    /** Deletes the cache. If file based the file has to be deleted also. */
    void delete();

    /** Loads the cache, must handle not existing cache. */
    void load();

    /** Saves modification of the cache. */
    void save();

    /**
     * Returns a value for a given key from the cache.
     * @param key the key
     * @return the stored value
     */
    Object get(Object key);

    /**
     * Saves a key-value-pair in the cache.
     * @param key the key
     * @param value the value
     */
    void put(Object key, Object value);

    /**
     * Returns an iterator over the keys in the cache.
     * @return An iterator over the keys.
     */
    Iterator iterator();
}
