package org.apache.camel.processor;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.Service;
import org.apache.camel.impl.ServiceSupport;

/**
 * @version $Revision: 534145 $
 */
public class SendProcessor extends ServiceSupport implements Processor, Service {
    private Endpoint destination;
    private Producer producer;

    public SendProcessor(Endpoint destination) {
        this.destination = destination;
    }

    protected void doStop() throws Exception {
        if (producer != null) {
            try {
                producer.stop();
            }
            finally {
                producer = null;
            }
        }
    }

    protected void doStart() throws Exception {
        this.producer = destination.createProducer();
    }

    public void process(Exchange exchange) throws Exception {
        if (producer == null) {
            throw new IllegalStateException("No producer, this processor has not been started!");
        }
        producer.process(exchange);
    }

    public Endpoint getDestination() {
        return destination;
    }

    @Override
    public String toString() {
        return "sendTo(" + destination + ")";
    }
}
