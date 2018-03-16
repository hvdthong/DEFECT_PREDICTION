package org.apache.tools.ant.types.resources.comparators;

import java.io.File;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.util.FileUtils;

/**
 * Compares filesystem Resources.
 * @since Ant 1.7
 */
public class FileSystem extends ResourceComparator {
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();

    /**
     * Compare two Resources.
     * @param foo the first Resource.
     * @param bar the second Resource.
     * @return a negative integer, zero, or a positive integer as the first
     *         argument is less than, equal to, or greater than the second.
     * @throws ClassCastException if either resource is not an instance of FileResource.
     */
    protected int resourceCompare(Resource foo, Resource bar) {
        File foofile = ((FileResource) foo).getFile();
        File barfile = ((FileResource) bar).getFile();
        return foofile.equals(barfile) ? 0
            : FILE_UTILS.isLeadingPath(foofile, barfile) ? -1
            : FILE_UTILS.normalize(foofile.getAbsolutePath()).compareTo(
                FILE_UTILS.normalize(barfile.getAbsolutePath()));
    }

}
