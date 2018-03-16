package org.apache.camel.impl;

import org.apache.camel.spi.Policy;
import org.apache.camel.Processor;

/**
 * Represents an {@link Policy} which adds no interceptors.
 *
 * @version $Revision: 1.1 $
 */
public class NoPolicy<E> implements Policy<E> {

    public Processor wrap(Processor processor) {
        return processor;
    }
}
