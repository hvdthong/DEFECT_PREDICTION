package org.apache.camel.spi;

/**
 * Represents a service registry which may be implemented via a Spring ApplicationContext,
 * via JNDI, a simple Map or the OSGI Service Registry
 *
 * @version $Revision: 1.1 $
 */
public interface Registry {

    /**
     * Looks up a service in the registry, returning the service or null if it could not be found.
     *
     * @param name the name of the service
     * @param type the type of the required service
     * @return the service from the registry or null if it could not be found
     */
    <T> T lookup(String name, Class<T> type);

    /**
     * Looks up a service in the registry based purely on name,
     * returning the service or null if it could not be found.
     *
     * @param name the name of the service
     * @return the service from the registry or null if it could not be found
     */
    Object lookup(String name);
}
