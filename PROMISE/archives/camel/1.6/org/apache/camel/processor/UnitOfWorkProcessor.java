package org.apache.camel.processor;

import org.apache.camel.AsyncCallback;
import org.apache.camel.AsyncProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultUnitOfWork;
import static org.apache.camel.util.ObjectHelper.wrapRuntimeCamelException;

/** 
 * Handles calling the UnitOfWork.done() method when processing of an exchange
 * is complete.
 */
public final class UnitOfWorkProcessor extends DelegateAsyncProcessor {

    public UnitOfWorkProcessor(AsyncProcessor processor) {
        super(processor);
    }
    
    @Override
    public String toString() {
        return "UnitOfWrok(" + processor + ")";
    }
    
    public boolean process(final Exchange exchange, final AsyncCallback callback) {
        if (exchange.getUnitOfWork() == null) {
            final DefaultUnitOfWork uow = new DefaultUnitOfWork();
            exchange.setUnitOfWork(uow);
            try {
                uow.start();
            } catch (Exception e) {
                throw wrapRuntimeCamelException(e);
            }
            return processor.process(exchange, new AsyncCallback() {
                public void done(boolean sync) {
                    callback.done(sync);
                    exchange.getUnitOfWork().done(exchange);
                    try {
                        uow.stop();
                    } catch (Exception e) {
                        throw wrapRuntimeCamelException(e);
                    }
                    exchange.setUnitOfWork(null);
                }
            });
        } else {
            return processor.process(exchange, callback);
        }
    }

}
