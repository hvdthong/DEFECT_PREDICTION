package org.apache.camel.component.cxf;

import org.apache.camel.RuntimeCamelException;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientFactoryBean;

import java.util.List;

/**
 * Sends messages from Camel into the CXF endpoint
 *
 * @version $Revision: 534156 $
 */
public class CxfInvokeProducer extends DefaultProducer {
    private CxfInvokeEndpoint endpoint;
    private Client client;

    public CxfInvokeProducer(CxfInvokeEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    public void process(Exchange exchange) {
        CxfExchange cxfExchange = endpoint.toExchangeType(exchange);
        process(cxfExchange);
        exchange.copyFrom(cxfExchange);
    }

    public void process(CxfExchange exchange) {
        List params = exchange.getIn().getBody(List.class);
        Object[] response = null;
        try {
            response = client.invoke(endpoint.getProperty(CxfConstants.METHOD), params.toArray());
        }                                                           
        catch (Exception e) {
            throw new RuntimeCamelException(e);
        }

        CxfBinding binding = endpoint.getBinding();
        binding.storeCxfResponse(exchange, response);
    }

    @Override
    protected void doStart() throws Exception {

        if (client == null) {
            ClientFactoryBean cfBean = new ClientFactoryBean();
            cfBean.setAddress(getEndpoint().getEndpointUri());
            cfBean.setBus(endpoint.getBus());
            cfBean.setServiceClass(Class.forName(endpoint.getProperty(CxfConstants.SEI)));
            client = cfBean.create();
        }
    }

    @Override
    protected void doStop() throws Exception {
        if (client != null) {
            client.getConduit().close();
            client = null;
        }

        super.doStop();
    }
}


