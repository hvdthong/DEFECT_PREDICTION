package org.apache.tools.ant.util;

import java.util.Hashtable;
import java.util.Enumeration;

/** Hashtable implementation that allows delayed construction
 * of expensive objects
 *
 * All operations that need access to the full list of objects
 * will call initAll() first. Get and put are cheap.
 *
 * @since Ant 1.6
 */
public class LazyHashtable extends Hashtable {
    protected boolean initAllDone = false;

    /** No arg constructor. */
    public LazyHashtable() {
        super();
    }

    /** Used to be part of init. It must be done once - but
     * we delay it until we do need _all_ tasks. Otherwise we
     * just get the tasks that we need, and avoid costly init.
     */
    protected void initAll() {
        if (initAllDone) {
            return;
        }
        initAllDone = true;
    }


    /**
     * Get a enumeration over the elements.
     * @return an enumeration.
     */
    public Enumeration elements() {
        initAll();
        return super.elements();
    }

    /**
     * Check if the table is empty.
     * @return true if it is.
     */
    public boolean isEmpty() {
        initAll();
        return super.isEmpty();
    }

    /**
     * Get the size of the table.
     * @return the size.
     */
    public int size() {
        initAll();
        return super.size();
    }

    /**
     * Check if the table contains a particular value.
     * @param value the value to look for.
     * @return true if the table contains the value.
     */
    public boolean contains(Object value) {
        initAll();
        return super.contains(value);
    }

    /**
     * Check if the table contains a particular key.
     * @param value the key to look for.
     * @return true if the table contains key.
     */
    public boolean containsKey(Object value) {
        initAll();
        return super.containsKey(value);
    }

    /**
     * Delegates to {@link #contains contains}.
     * @param value the value to look for.
     * @return true if the table contains the value.
     */
    public boolean containsValue(Object value) {
        return contains(value);
    }

    /**
     * Get an enumeration over the keys.
     * @return an enumeration.
     */
    public Enumeration keys() {
        initAll();
        return super.keys();
    }

}
