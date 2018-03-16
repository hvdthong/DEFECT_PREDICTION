package org.apache.camel.component.seda;

import java.util.concurrent.TimeUnit;

import org.apache.camel.AlreadyStoppedException;
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
 * @version $Revision: 582309 $
 */
public class SedaConsumer extends ServiceSupport implements Consumer, Runnable {
    private static final Log LOG = LogFactory.getLog(SedaConsumer.class);

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
        while (isRunAllowed()) {
            final Exchange exchange;
            try {
                exchange = endpoint.getQueue().poll(1000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                break;
            }
            if (exchange != null && isRunAllowed()) {
                processor.process(exchange, new AsyncCallback() {
                    public void done(boolean sync) {
                    }
                });
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
    }

}
