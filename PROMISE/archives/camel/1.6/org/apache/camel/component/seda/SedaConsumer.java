package org.apache.camel.component.seda;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.camel.AsyncCallback;
import org.apache.camel.AsyncProcessor;
import org.apache.camel.Consumer;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.ServiceSupport;
import org.apache.camel.impl.converter.AsyncProcessorTypeConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A Consumer for the SEDA component.
 *
 * @version $Revision: 707305 $
 */
public class SedaConsumer extends ServiceSupport implements Consumer, Runnable {
    private static final transient Log LOG = LogFactory.getLog(SedaConsumer.class);

    private SedaEndpoint endpoint;
    private AsyncProcessor processor;
    private Thread thread;

    public SedaConsumer(SedaEndpoint endpoint, Processor processor) {
        this.endpoint = endpoint;
        this.processor = AsyncProcessorTypeConverter.convert(processor);
    }

    @Override
    public String toString() {
        return "SedaConsumer: " + endpoint.getEndpointUri();
    }

    public void run() {
        BlockingQueue<Exchange> queue = endpoint.getQueue();
        while (queue != null && isRunAllowed()) {
            final Exchange exchange;
            try {
                exchange = queue.poll(1000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Interupted: " + e, e);
                }
                continue;
            }
            if (exchange != null) {
                if (isRunAllowed()) {
                    try {
                        processor.process(exchange, new AsyncCallback() {
                            public void done(boolean sync) {
                            }
                        });
                    } catch (Exception e) {
                        LOG.error("Seda queue caught: " + e, e);
                    }
                } else {
                    LOG.warn("This consumer is stopped during polling an exchange, so putting it back on the seda queue: " + exchange);
                    try {
                        queue.put(exchange);
                    } catch (InterruptedException e) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Interupted: " + e, e);
                        }
                    }
                }
            }
        }
    }

    protected void doStart() throws Exception {
        thread = new Thread(this, getThreadName(endpoint.getEndpointUri()));
        thread.setDaemon(true);
        thread.start();
    }

    protected void doStop() throws Exception {
        thread.join();
        thread = null;
    }

}
