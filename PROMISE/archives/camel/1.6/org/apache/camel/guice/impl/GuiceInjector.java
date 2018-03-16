package org.apache.camel.guice.impl;

import org.apache.camel.spi.Injector;

/**
 * An injector which uses Guice to perform the dependency injection of types
 *
 * @version $Revision: 705742 $
 */
public class GuiceInjector implements Injector {
    private final com.google.inject.Injector injector;

    public GuiceInjector(com.google.inject.Injector injector) {
        this.injector = injector;
    }

    public <T> T newInstance(Class<T> type) {
        return injector.getInstance(type);
    }

}
