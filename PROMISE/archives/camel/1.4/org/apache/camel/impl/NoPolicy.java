package org.apache.camel.impl;

import org.apache.camel.Processor;
import org.apache.camel.spi.Policy;

/**
 * Represents an {@link Policy} which adds no interceptors.
 *
 * @version $Revision: 630591 $
 */
public class NoPolicy<E> implements Policy<E> {

    public Processor wrap(Processor processor) {
        return processor;
    }
}
