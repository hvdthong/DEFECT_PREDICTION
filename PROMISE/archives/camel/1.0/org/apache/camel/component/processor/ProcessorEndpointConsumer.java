package org.apache.camel.component.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;

/**
 * @version $Revision: 1.1 $
*/
public class ProcessorEndpointConsumer extends DefaultConsumer<Exchange> {
    private final ProcessorEndpoint endpoint;

    public ProcessorEndpointConsumer(ProcessorEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
        this.endpoint = endpoint;
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        endpoint.getLoadBalancer().addProcessor(getProcessor());
    }

    @Override
    protected void doStop() throws Exception {
        endpoint.getLoadBalancer().removeProcessor(getProcessor());
        super.doStop();
    }
}
