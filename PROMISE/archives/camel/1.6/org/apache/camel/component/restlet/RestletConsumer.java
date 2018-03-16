package org.apache.camel.component.restlet;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.restlet.Restlet;
import org.restlet.data.Request;
import org.restlet.data.Response;

/**
 * A Restlet consumer acts as a server to listen client requests.
 *
 * @version $Revision: 730506 $
 */
public class RestletConsumer extends DefaultConsumer {
    private static final Log LOG = LogFactory.getLog(RestletConsumer.class);
    private Restlet restlet;

    public RestletConsumer(Endpoint endpoint, Processor processor) 
        throws Exception {
        super(endpoint, processor);
        
        restlet = new Restlet() {
            @Override
            public void handle(Request request, Response response) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Consumer restlet handle request method: " + request.getMethod());
                }
                
                try {
                    Exchange exchange = getEndpoint().createExchange();
                    RestletBinding binding = ((RestletEndpoint)getEndpoint()).getRestletBinding();
                    binding.populateExchangeFromRestletRequest(request, exchange);
                    getProcessor().process(exchange);
                    binding.populateRestletResponseFromExchange(exchange, response);
                } catch (Exception e) {
                    throw new RuntimeCamelException(e);
                }
            }
        };
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        ((RestletEndpoint)getEndpoint()).connect(this);
    }

    @Override
    public void doStop() throws Exception {
        ((RestletEndpoint)getEndpoint()).disconnect(this);
        super.doStop();
    }

    public Restlet getRestlet() {
        return restlet;
    }

}
