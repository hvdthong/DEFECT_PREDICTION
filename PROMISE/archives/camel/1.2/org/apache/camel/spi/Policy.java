package org.apache.camel.spi;

import org.apache.camel.Processor;

/**
 * A strategy capable of applying interceptors to a processor
 *
 * @version $Revision: 1.1 $
 */
public interface Policy<E> {

    /**
     * Wraps any applicable interceptors around the given processor
     *
     * @param processor the processor to be intercepted
     * @return either the original processor or a processor wrapped in one or more interceptors
     */
    Processor wrap(Processor processor);
}
