package org.apache.ivy.core.module.descriptor;

/**
 * Objects implementing this interface are aware of module configurations, and can thus be added to
 * configurations, and list their configurations.
 */
public interface ConfigurationAware {
    /**
     * Returns the configurations of the module to which the object is attached
     * 
     * @return an array of configuration names to which the object is attached
     */
    public String[] getConfigurations();

    /**
     * Tells this object that it will now be part of the given configuration
     * 
     * @param confName
     *            the name of the configuration to which the object is now attached
     */
    public void addConfiguration(String confName);
}
