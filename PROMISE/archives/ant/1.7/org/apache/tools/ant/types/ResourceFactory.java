package org.apache.tools.ant.types;

/**
 * this interface should be implemented by classes (Scanners) needing
 * to deliver information about resources.
 *
 * @since Ant 1.5.2
 */
public interface ResourceFactory {

    /**
     * Query a resource (file, zipentry, ...) by name
     *
     * @param name relative path of the resource about which
     * information is sought.  Expects &quot;/&quot; to be used as the
     * directory separator.
     * @return instance of Resource; the exists attribute of Resource
     * will tell whether the sought resource exists
     */
    Resource getResource(String name);
}
