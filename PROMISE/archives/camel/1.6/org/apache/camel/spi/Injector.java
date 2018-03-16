package org.apache.camel.spi;

/**
 * A pluggable strategy for creating and possibly dependency injecting objects
 * which could be implemented using straight forward reflection or using Spring
 * or Guice to perform dependency injection.
 * 
 * @version $Revision: 659798 $
 */
public interface Injector {

    /**
     * Instantiates a new instance of the given type possibly injecting values
     * into the object in the process
     * 
     * @param type the type of object to create
     * @return a newly created instance
     */
    <T> T newInstance(Class<T> type);
}
