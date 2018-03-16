package org.apache.camel.component.queue;

import org.apache.camel.Consumer;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.ServiceSupport;

import java.util.concurrent.TimeUnit;

/**
 * @version $Revision: 534145 $
 */
public class QueueEndpointConsumer<E extends Exchange> extends ServiceSupport implements Consumer<E>, Runnable {
    private QueueEndpoint<E> endpoint;
    private Processor processor;
    private Thread thread;

    public QueueEndpointConsumer(QueueEndpoint<E> endpoint, Processor processor) {
        this.endpoint = endpoint;
        this.processor = processor;
    }

    @Override
    public String toString() {
        return "QueueEndpointConsumer: " + endpoint.getEndpointUri();
    }

    public void run() {
        while (!isStopping()) {
            E exchange;
            try {
                exchange = endpoint.getQueue().poll(1000, TimeUnit.MILLISECONDS);
            }
            catch (InterruptedException e) {
                break;
            }
            if (exchange != null && !isStopping()) {
                try {
                    processor.process(exchange);
                }
                catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void doStart() throws Exception {
        thread = new Thread(this, endpoint.getEndpointUri());
        thread.setDaemon(true);
        thread.start();
    }

    protected void doStop() throws Exception {
        thread.join();
    }
}
