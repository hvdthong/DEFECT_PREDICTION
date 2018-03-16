package org.apache.camel.component.restlet;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.restlet.Client;
import org.restlet.data.Request;
import org.restlet.data.Response;

/**
 * A Camel producer that acts as a client to Restlet server.
 *
 * @version $Revision: 730158 $
 */
public class RestletProducer extends DefaultProducer {
    private static final Log LOG = LogFactory.getLog(RestletProducer.class);
    private Client client;

    public RestletProducer(RestletEndpoint endpoint) throws Exception {
        super(endpoint);
        client = new Client(endpoint.getProtocol());
    }

    @Override
    public void doStart() throws Exception {
        super.doStart();
        client.start();
    }
    
    @Override
    public void doStop() throws Exception {
        client.stop();
        super.doStop();
    }
    
    public void process(Exchange exchange) throws Exception {
        RestletEndpoint endpoint = (RestletEndpoint)getEndpoint();
        
        String resourceUri = buildUri(endpoint);
        Request request = new Request(endpoint.getRestletMethod(), 
                resourceUri);
        
        RestletBinding binding = endpoint.getRestletBinding();
        binding.populateRestletRequestFromExchange(request, exchange);
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Client sends a request (method: " + request.getMethod() 
                    + ", uri: " + resourceUri + ")");
        }
        
        Response response = client.handle(request);
        binding.populateExchangeFromRestletResponse(exchange, response);
    }

    private static String buildUri(RestletEndpoint endpoint) {
            + endpoint.getPort() + endpoint.getUriPattern();
    }

}
