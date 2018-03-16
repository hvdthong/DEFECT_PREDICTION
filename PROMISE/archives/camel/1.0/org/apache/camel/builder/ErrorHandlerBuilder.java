package org.apache.camel.builder;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * @version $Revision: 534145 $
 */
public interface ErrorHandlerBuilder<E extends Exchange> {
    /**
     * Creates a copy of this builder
     */
    ErrorHandlerBuilder<E> copy();

    /**
     * Creates the error handler interceptor
     */
    Processor createErrorHandler(Processor processor) throws Exception;
}
