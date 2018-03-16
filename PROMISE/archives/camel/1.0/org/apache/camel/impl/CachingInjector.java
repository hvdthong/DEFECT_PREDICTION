package org.apache.camel.impl;

import org.apache.camel.impl.converter.TypeConverterRegistry;

/**
 * A caching proxy so that a single 
 * @version $Revision: 525537 $
 */
public class CachingInjector<T> {
    private final TypeConverterRegistry repository;
    private final Class<T> type;
    private T instance;

    public CachingInjector(TypeConverterRegistry repository, Class<T> type) {
        this.repository = repository;
        this.type = type;
    }

    public synchronized T newInstance() {
        if (instance == null) {
            instance = createInstance(type);
        }
        return instance;
    }

    protected T createInstance(Class<T> type) {
        return (T) repository.getInjector().newInstance(type);
    }
}
