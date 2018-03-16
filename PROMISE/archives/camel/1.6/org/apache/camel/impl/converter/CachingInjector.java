package org.apache.camel.impl.converter;

/**
 * A caching proxy
 *
 * @version $Revision: 648954 $
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

    protected T createInstance(Class<T> t) {
        return repository.getInjector().newInstance(t);
    }
}
