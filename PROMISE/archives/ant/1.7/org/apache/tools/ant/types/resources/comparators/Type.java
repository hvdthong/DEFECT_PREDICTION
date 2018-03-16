package org.apache.tools.ant.types.resources.comparators;

import org.apache.tools.ant.types.Resource;

/**
 * Compares Resources by is-directory status.  As a container
 * of files, a directory is deemed "greater" than a file.
 * @since Ant 1.7
 */
public class Type extends ResourceComparator {

    /**
     * Compare two Resources.
     * @param foo the first Resource.
     * @param bar the second Resource.
     * @return a negative integer, zero, or a positive integer as the first
     *         argument is less than, equal to, or greater than the second.
     */
    protected int resourceCompare(Resource foo, Resource bar) {
        boolean f = foo.isDirectory();
        if (f == bar.isDirectory()) {
            return 0;
        }
        return f ? 1 : -1;
    }

}
