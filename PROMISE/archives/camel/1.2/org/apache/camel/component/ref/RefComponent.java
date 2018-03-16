package org.apache.camel.component.ref;

import org.apache.camel.Exchange;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

import java.util.Map;

/**
 *
 */
public class RefComponent extends DefaultComponent<Exchange> {

    protected Endpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        String name = uri.substring(4);
        return getCamelContext().getRegistry().lookup(name, Endpoint.class);
    }

}
