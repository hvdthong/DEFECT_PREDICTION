package org.apache.tools.ant.types.resources;

import org.apache.tools.ant.BuildException;

/**
 * ResourceCollection that imposes a size limit on another ResourceCollection.
 * @since Ant 1.7.1
 */
public abstract class SizeLimitCollection extends BaseResourceCollectionWrapper {
    private static final String BAD_COUNT
        = "size-limited collection count should be set to an int >= 0";

    private int count = 1;

    /**
     * Set the number of resources to be included.
     * @param i the count as <code>int</count>.
     */
    public synchronized void setCount(int i) {
        count = i;
    }

    /**
     * Get the number of resources to be included. Default is 1.
     * @return the count as <code>int</count>.
     */
    public synchronized int getCount() {
        return count;
    }

    /**
     * Efficient size implementation.
     * @return int size
     */
    public synchronized int size() {
        int sz = getResourceCollection().size();
        int ct = getValidCount();
        return sz < ct ? sz : ct;
    }

    /**
     * Get the count, verifying it is >= 0.
     * @return int count
     */
    protected int getValidCount() {
        int ct = getCount();
        if (ct < 0) {
            throw new BuildException(BAD_COUNT);
        }
        return ct;
    }

}
