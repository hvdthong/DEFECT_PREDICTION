package org.apache.camel.spi;

/**
 * A provider of newly constructed objects
 *
 * @version $Revision: 532316 $
 */
public interface Provider<T> {

    /**
     * Returns the newly constructed instance
     */
    T get();
}
