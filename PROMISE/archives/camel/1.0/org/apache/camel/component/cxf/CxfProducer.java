package org.apache.camel.component.cxf;

import org.apache.camel.RuntimeCamelException;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.apache.cxf.message.ExchangeImpl;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageImpl;
import org.apache.cxf.service.model.EndpointInfo;
import org.apache.cxf.transport.Conduit;
import org.apache.cxf.transport.Destination;
import org.apache.cxf.transport.MessageObserver;
import org.apache.cxf.transport.local.LocalConduit;
import org.apache.cxf.transport.local.LocalTransportFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Sends messages from Camel into the CXF endpoint
 *
 * @version $Revision: 534145 $
 */
public class CxfProducer extends DefaultProducer {
    private CxfEndpoint endpoint;
    private final LocalTransportFactory transportFactory;
    private Destination destination;
    private Conduit conduit;
    private ResultFuture future = new ResultFuture();

    public CxfProducer(CxfEndpoint endpoint, LocalTransportFactory transportFactory) {
        super(endpoint);
        this.endpoint = endpoint;
        this.transportFactory = transportFactory;
    }

    public void process(Exchange exchange) {
        CxfExchange cxfExchange = endpoint.toExchangeType(exchange);
        process(cxfExchange);
    }

    public void process(CxfExchange exchange) {
        try {
            CxfBinding binding = endpoint.getBinding();
            MessageImpl m = binding.createCxfMessage(exchange);
            ExchangeImpl e = new ExchangeImpl();
            e.setInMessage(m);
            m.put(LocalConduit.DIRECT_DISPATCH, Boolean.TRUE);
            m.setDestination(destination);
            synchronized (conduit) {
                conduit.prepare(m);

                if (endpoint.isInOut()) {
                    Message response = future.getResponse();

                    response = e.getOutMessage();
                    binding.storeCxfResponse(exchange, response);
                }
            }
        }
        catch (IOException e) {
            throw new RuntimeCamelException(e);
        }
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        EndpointInfo endpointInfo = endpoint.getEndpointInfo();
        destination = transportFactory.getDestination(endpointInfo);

        conduit = transportFactory.getConduit(endpointInfo);
        conduit.setMessageObserver(future);
    }

    @Override
    protected void doStop() throws Exception {
        super.doStop();

        if (conduit != null) {
            conduit.close();
        }
    }

    protected class ResultFuture implements MessageObserver {
        Message response;
        CountDownLatch latch = new CountDownLatch(1);

        public Message getResponse() {
            while (response == null) {
                try {
                    latch.await();
                }
                catch (InterruptedException e) {
                }
            }
            return response;
        }

        public synchronized void onMessage(Message message) {
            try {
                message.remove(LocalConduit.DIRECT_DISPATCH);
                this.response = message;
            }
            finally {
                latch.countDown();
            }
        }
    }
}
