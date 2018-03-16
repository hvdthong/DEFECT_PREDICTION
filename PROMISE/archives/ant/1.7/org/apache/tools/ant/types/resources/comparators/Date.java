package org.apache.tools.ant.types.resources.comparators;

import org.apache.tools.ant.types.Resource;

/**
 * Compares Resources by last modification date.
 * @since Ant 1.7
 */
public class Date extends ResourceComparator {
    /**
     * Compare two Resources.
     * @param foo the first Resource.
     * @param bar the second Resource.
     * @return a negative integer, zero, or a positive integer as the first
     *         argument is less than, equal to, or greater than the second.
     */
    protected int resourceCompare(Resource foo, Resource bar) {
        long diff = foo.getLastModified() - bar.getLastModified();
        if (diff > 0) {
            return +1;
        } else if (diff < 0) {
            return -1;
        } else {
            return 0;
        }
    }

}
