package org.apache.camel.builder;

import org.apache.camel.model.ExceptionType;
import org.apache.camel.processor.ErrorHandlerSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * @version $Revision: 1.1 $
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
}
