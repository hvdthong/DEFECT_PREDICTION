package org.apache.camel.processor.interceptor;

import org.apache.camel.Exchange;

/**
 * Represents an exception that occurred when processing an exchange
 *
 * @version $Revision: 673954 $
 */
public class ExceptionEvent {
    private final DebugInterceptor interceptor;
    private final Exchange exchange;
    private final Throwable exception;

    public ExceptionEvent(DebugInterceptor interceptor, Exchange exchange, Throwable exception) {
        this.interceptor = interceptor;
        this.exchange = exchange;
        this.exception = exception;
    }

    public Throwable getException() {
        return exception;
    }

    public Exchange getExchange() {
        return exchange;
    }

    public DebugInterceptor getInterceptor() {
        return interceptor;
    }
}
