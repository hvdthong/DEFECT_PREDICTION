package org.apache.camel.builder;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.model.ExceptionType;
import org.apache.camel.processor.ErrorHandlerSupport;

/**
 * Base class for builders of error handling.
 *
 * @version $Revision: 673837 $
 */
public abstract class ErrorHandlerBuilderSupport implements ErrorHandlerBuilder {
    private List<ExceptionType> exceptions = new ArrayList<ExceptionType>();

    public void addErrorHandlers(ExceptionType exception) {
        exceptions.add(exception);
    }

    protected void configure(ErrorHandlerSupport handler) {
        for (ExceptionType exception : exceptions) {
            handler.addExceptionPolicy(exception);
        }
    }

    public List<ExceptionType> getExceptions() {
        return exceptions;
    }
}
