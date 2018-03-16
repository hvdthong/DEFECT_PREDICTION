package org.apache.camel.util;

/**
 * Holder object for a given value.
 */
public class ValueHolder<V> {
    private V value;

    public ValueHolder() {
    }

    public ValueHolder(V val) {
        value = val;
    }

    public V get() {
        return value;
    }

    public void set(V val) {
        value = val;
    }
    
}
