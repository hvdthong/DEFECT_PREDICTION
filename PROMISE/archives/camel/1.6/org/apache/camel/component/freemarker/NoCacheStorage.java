package org.apache.camel.component.freemarker;

import freemarker.cache.CacheStorage;

/**
 * A cache storage for Freemarker with no cache used for development to force reload of templates
 * on every request.
 */
public class NoCacheStorage implements CacheStorage {

    public Object get(Object key) {
        return null;
    }

    public void put(Object key, Object value) {
    }

    public void remove(Object key) {
    }

    public void clear() {
    }

}
