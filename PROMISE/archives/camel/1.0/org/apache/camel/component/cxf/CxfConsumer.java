package org.apache.camel.component.cxf;

import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.Destination;
import org.apache.cxf.transport.MessageObserver;
import org.apache.cxf.transport.local.LocalTransportFactory;

/**
 * A consumer of exchanges for a service in CXF
 *
 * @version $Revision: 534145 $
 */
public class CxfConsumer extends DefaultConsumer<CxfExchange> {
    private CxfEndpoint endpoint;
    private final LocalTransportFactory transportFactory;
    private Destination destination;

    public CxfConsumer(CxfEndpoint endpoint, Processor processor, LocalTransportFactory transportFactory) {
        super(endpoint, processor);
        this.endpoint = endpoint;
        this.transportFactory = transportFactory;
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();

        destination = transportFactory.getDestination(endpoint.getEndpointInfo());
        destination.setMessageObserver(new MessageObserver() {
            public void onMessage(Message message) {
                incomingCxfMessage(message);
            }
        });
    }

    @Override
    protected void doStop() throws Exception {
        if (destination != null) {
            destination.shutdown();
        }
        super.doStop();
    }

    protected void incomingCxfMessage(Message message) {
        try {
            CxfExchange exchange = endpoint.createExchange(message);
			getProcessor().process(exchange);
		} catch (Exception e) {			
			e.printStackTrace();
		}
    }
}
