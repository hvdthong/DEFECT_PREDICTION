package org.apache.camel.builder;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.model.ExceptionType;

import java.util.List;

/**
 * @version $Revision: 564644 $
 */
public interface ErrorHandlerBuilder {
    /**
     * Creates a copy of this builder
     */
    ErrorHandlerBuilder copy();

    /**
     * Creates the error handler interceptor
     */
    Processor createErrorHandler(Processor processor) throws Exception;

    void addErrorHandlers(ExceptionType exception);
}
