package org.apache.tools.ant.types;

import java.util.Iterator;

/**
 * Interface describing a collection of Resources.
 * @since Ant 1.7
 */
public interface ResourceCollection {

    /**
     * Get an Iterator over the contents of this ResourceCollection, whose elements
     * are <code>org.apache.tools.ant.types.Resource</code> instances.
     * @return an Iterator of Resources.
     */
    Iterator iterator();

    /**
     * Learn the number of contained Resources.
     * @return number of elements as int.
     */
    int size();

    /**
     * Indicate whether this ResourceCollection is composed entirely of
     * Resources accessible via local filesystem conventions.  If true,
     * all Resources returned from this ResourceCollection should be
     * instances of FileResource.
     * @return whether this is a filesystem-only resource collection.
     */
    boolean isFilesystemOnly();

}
