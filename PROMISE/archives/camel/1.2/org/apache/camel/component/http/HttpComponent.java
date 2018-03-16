package org.apache.camel.component.http;

import java.net.URI;
import java.util.Map;

import org.apache.camel.Consumer;
import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.impl.DefaultComponent;

/**
 * Component</a>
 * 
 * @version $Revision: 571436 $
 */
public class HttpComponent extends DefaultComponent<HttpExchange> {

    /**
     * Connects the URL specified on the endpoint to the specified processor.
     * 
     * @throws Exception
     */
    public void connect(HttpConsumer consumer) throws Exception {
    }

    /**
     * Disconnects the URL specified on the endpoint from the specified
     * processor.
     * 
     * @throws Exception
     */
    public void disconnect(HttpConsumer consumer) throws Exception {
    }


    @Override
    protected Endpoint<HttpExchange> createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        return new HttpEndpoint(uri, this, new URI(uri));
    }

}
