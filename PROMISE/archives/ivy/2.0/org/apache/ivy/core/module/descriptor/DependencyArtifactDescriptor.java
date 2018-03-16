package org.apache.ivy.core.module.descriptor;

import java.net.URL;

import org.apache.ivy.util.extendable.ExtendableItem;

/**
 * This describes an artifact that is asked for a dependency. It is used to define an (additional)
 * artifact not declared by a dependency module descriptor.
 */
public interface DependencyArtifactDescriptor extends ExtendableItem {
    /**
     * Returns the dependency descriptor in which this dependency artifact descriptor is declared.
     * 
     * @return the dependency descriptor in which this dependency artifact descriptor is declared.
     */
    public DependencyDescriptor getDependencyDescriptor();
    /**
     * Returns the name of the artifact asked
     * 
     * @return
     */
    public String getName();

    /**
     * Returns the type of the artifact asked
     * 
     * @return
     */
    public String getType();

    /**
     * Returns the ext of the artifact asked
     * 
     * @return
     */
    public String getExt();

    /**
     * Returns the url to look this artifact at
     * 
     * @return
     */
    public URL getUrl();

    /**
     * Returns the configurations of the module in which the artifact is asked
     * 
     * @return an array of configuration names in which the artifact is asked
     */
    public String[] getConfigurations();
}
