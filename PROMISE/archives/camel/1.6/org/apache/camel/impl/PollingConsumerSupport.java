package org.apache.camel.impl;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.PollingConsumer;
import org.apache.camel.spi.ExceptionHandler;

/**
 * A useful base class for implementations of {@link PollingConsumer}
 * 
 * @version $Revision: 630591 $
 */
public abstract class PollingConsumerSupport<E extends Exchange> extends ServiceSupport implements
    PollingConsumer<E> {
    private final Endpoint<E> endpoint;
    private ExceptionHandler exceptionHandler;

    public PollingConsumerSupport(Endpoint<E> endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String toString() {
        return "PullConsumer on " + endpoint;
    }

    public Endpoint<E> getEndpoint() {
        return endpoint;
    }

    public ExceptionHandler getExceptionHandler() {
        if (exceptionHandler == null) {
            exceptionHandler = new LoggingExceptionHandler(getClass());
        }
        return exceptionHandler;
    }

    public void setExceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    /**
     * Handles the given exception using the {@link #getExceptionHandler()}
     * 
     * @param t the exception to handle
     */
    protected void handleException(Throwable t) {
        getExceptionHandler().handleException(t);
    }
}
