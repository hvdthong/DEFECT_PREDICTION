package org.apache.camel.component.cxf;

import org.apache.camel.AsyncCallback;
import org.apache.camel.AsyncProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.converter.AsyncProcessorTypeConverter;
import org.apache.camel.util.AsyncProcessorHelper;

/**
 * A simple class to wrap an existing processor (synchronous or asynchronous)
 * with two synchronous processors that will be executed before and after the
 * main processor.
 */
public class AsyncProcessorDecorator implements AsyncProcessor {

    private final AsyncProcessor processor;
    private final Processor before;
    private final Processor after;

    public AsyncProcessorDecorator(Processor processor, Processor before, Processor after) {
        this.processor = AsyncProcessorTypeConverter.convert(processor);
        this.before = before;
        this.after = after;
    }

    public void process(Exchange exchange) throws Exception {
        AsyncProcessorHelper.process(this, exchange);
    }

    public boolean process(final Exchange exchange, final AsyncCallback callback) {
        try {
            before.process(exchange);
        } catch (Throwable t) {
            exchange.setException(t);
            callback.done(true);
            return true;
        }
        return processor.process(exchange, new AsyncCallback() {
            public void done(boolean doneSynchronously) {
                try {
                    after.process(exchange);
                    callback.done(doneSynchronously);
                } catch (Throwable t) {
                    exchange.setException(t);
                }
            }
        });
    }

}
