package org.apache.tools.ant.types.resources.comparators;

import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.util.ResourceUtils;

/**
 * Compares Resources by content.
 * @since Ant 1.7
 */
public class Content extends ResourceComparator {

    private boolean binary = true;

    /**
     * Set binary mode for this Content ResourceComparator. If this
     * attribute is set to false, Resource content will be compared
     * ignoring platform line-ending conventions.
     * Default is <code>true</code>.
     * @param b whether to compare content in binary mode.
     */
    public void setBinary(boolean b) {
        binary = b;
    }

    /**
     * Learn whether this Content ResourceComparator is operating in binary mode.
     * @return boolean binary flag.
     */
    public boolean isBinary() {
        return binary;
    }

    /**
     * Compare two Resources by content.
     * @param foo the first Resource.
     * @param bar the second Resource.
     * @return a negative integer, zero, or a positive integer as the first
     *         argument is less than, equal to, or greater than the second.
     * @throws BuildException if I/O errors occur.
     * @see org.apache.tools.ant.util.ResourceUtils#compareContent(Resource, Resource, boolean).
     */
    protected int resourceCompare(Resource foo, Resource bar) {
        try {
            return ResourceUtils.compareContent(foo, bar, !binary);
        } catch (IOException e) {
            throw new BuildException(e);
        }
    }

}
