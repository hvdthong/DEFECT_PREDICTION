package org.apache.camel.impl;

import org.apache.camel.Exchange;
import org.apache.camel.Consumer;
import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.apache.camel.spi.ExceptionHandler;
import org.apache.camel.util.ServiceHelper;

/**
 * @version $Revision: 541323 $
 */
public class DefaultConsumer<E extends Exchange> extends ServiceSupport implements Consumer<E> {
    private Endpoint<E> endpoint;
    private Processor processor;
    private ExceptionHandler exceptionHandler;

    public DefaultConsumer(Endpoint<E> endpoint, Processor processor) {
        this.endpoint = endpoint;
        this.processor = processor;
    }

    @Override
	public String toString() {
		return "Consumer on " + endpoint;
	}


	public Endpoint<E> getEndpoint() {
        return endpoint;
    }

    public Processor getProcessor() {
        return processor;
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

    protected void doStop() throws Exception {
        ServiceHelper.stopServices(processor);
    }

    protected void doStart() throws Exception {
        ServiceHelper.startServices(processor);
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
