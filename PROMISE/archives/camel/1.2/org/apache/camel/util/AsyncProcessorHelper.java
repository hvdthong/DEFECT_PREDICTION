package org.apache.camel.util;

import java.util.concurrent.CountDownLatch;

import org.apache.camel.AsyncCallback;
import org.apache.camel.AsyncProcessor;
import org.apache.camel.Exchange;

/**
 * Helper methods for AsyncProcessor objects.
 */
public final class AsyncProcessorHelper {
    
    private AsyncProcessorHelper() {
    }

    /**
     * Calls the async version of the processor's process method and waits
     * for it to complete before returning.  This can be used by AsyncProcessor
     * objects to implement their sync version of the process method.
     * 
     * @param processor
     * @param exchange
     * @throws Exception
     */
    public static void process(AsyncProcessor processor, Exchange exchange) throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        boolean sync = processor.process(exchange, new AsyncCallback() {
            public void done(boolean sync) {
                if (!sync) {
                    latch.countDown();
                }
            }
        });
        if (!sync) {
            latch.await();
        }
    }
}
