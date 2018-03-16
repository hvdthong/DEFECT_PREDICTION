package org.apache.camel.component.ref;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultComponent;

/**
 * Component for lookup of existing endpoints bound in the {@link org.apache.camel.spi.Registry}.
 * <p/>
 * This component uses the <tt>ref:</tt> notation instead of the mostly common <tt>uri:</tt> notation. 
 */
public class RefComponent extends DefaultComponent<Exchange> {

    protected Endpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        String name = uri.substring("ref:".length());
        return getCamelContext().getRegistry().lookup(name, Endpoint.class);
    }

}
