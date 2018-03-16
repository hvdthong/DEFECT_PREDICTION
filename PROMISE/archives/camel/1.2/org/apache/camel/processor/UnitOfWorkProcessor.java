package org.apache.camel.processor;

import org.apache.camel.AsyncCallback;
import org.apache.camel.AsyncProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultUnitOfWork;
import org.apache.camel.spi.UnitOfWork;

/** 
 * Handles calling the UnitOfWork.done() method when processing of an exchange
 * is complete.
 */
public final class UnitOfWorkProcessor extends DelegateAsyncProcessor {

    public UnitOfWorkProcessor(AsyncProcessor processor) {
        super(processor);
    }
    
    public boolean process(final Exchange exchange, final AsyncCallback callback) {
        if (exchange.getUnitOfWork() == null) {
            exchange.setUnitOfWork(new DefaultUnitOfWork());
            return processor.process(exchange, new AsyncCallback() {
                public void done(boolean sync) {
                    callback.done(sync);
                    exchange.getUnitOfWork().done(exchange);
                    exchange.setUnitOfWork(null);
                }
            });
        } else {
            return processor.process(exchange, callback);
        }
    }

}
