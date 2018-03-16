package org.apache.tools.ant.types.resources;

/**
 * Interface to be implemented by "touchable" resources;
 * that is, those whose modification time can be altered.
 * @since Ant 1.7
 */
public interface Touchable {
    /**
     * Method called to "touch" the resource.
     * @param modTime the time to set the modified "field" of the resource,
     *                measured in milliseconds since the epoch.
     */
    void touch(long modTime);
}
