package org.apache.synapse.registry;

import java.net.URI;

/**
 * This interface defines the core information to be returned by a Registry implementation
 * about a resource being managed by it. Every Registry implementation *must* provide valid
 * information for the methods marked below as 'required'
 */
public interface RegistryEntry {

    /** The key for the resource - required */
    public String getKey();

    /** A name for the resource - optional */
    public String getName();

    /** The version of the resource - required */
    public long getVersion();

    /** The type of the resource - optional */
    public URI getType();

    /** A description for the resource - optional */
    public String getDescription();

    /** The created time for the resource - optional */
    public long getCreated();

    /** The last updated time for the resource - optional */
    public long getLastModified();

    /** The number of milliseconds this resource could be cached */
    public long getCachableDuration();
}
