package org.apache.tools.ant.types.resources.comparators;

import org.apache.tools.ant.types.Resource;

/**
 * Compares Resources by existence.  Not existing is "less than" existing.
 * @since Ant 1.7
 */
public class Exists extends ResourceComparator {

    /**
     * Compare two Resources.
     * @param foo the first Resource.
     * @param bar the second Resource.
     * @return a negative integer, zero, or a positive integer as the first
     *         argument is less than, equal to, or greater than the second.
     */
    protected int resourceCompare(Resource foo, Resource bar) {
        boolean f = foo.isExists();
        if (f == bar.isExists()) {
            return 0;
        }
        return f ? 1 : -1;
    }

}
