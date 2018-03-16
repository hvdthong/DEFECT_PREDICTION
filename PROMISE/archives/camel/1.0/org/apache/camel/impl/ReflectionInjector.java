package org.apache.camel.impl;

import org.apache.camel.RuntimeCamelException;
import org.apache.camel.spi.Injector;

/**
 * A simple implementation of {@link Injector} which just uses reflection to instantiate new objects
 * using their zero argument constructor. For more complex implementations try the Spring or Guice implementations.
 *
 * @version $Revision: 525537 $
 */
public class ReflectionInjector<T> implements Injector<T> {
    
    public T newInstance(Class<T> type) {
        try {
            return type.newInstance();
        }
        catch (InstantiationException e) {
            throw new RuntimeCamelException(e.getCause());
        }
        catch (IllegalAccessException e) {
            throw new RuntimeCamelException(e);
        }
    }
}
