package org.apache.camel.spi;

/**
 * A provider of newly constructed objects
 *
 * @version $Revision: 688279 $
 */
public interface Provider<T> {

    /**
     * Returns the newly constructed instance
     *
     * @return the newly constructed instance
     */
    T get();
}
