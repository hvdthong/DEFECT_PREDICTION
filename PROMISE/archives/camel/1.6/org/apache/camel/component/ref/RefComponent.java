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
        int index = uri.indexOf(":");
        String name = uri;
        if (index >= 0) {
            name = uri.substring(index + 1);
        }
        return lookupEndpoint(name, parameters);
    }

    /**
     * Looks up an endpoint for a given name.
     *
     * Derived classes could use this name as a logical name and look it up on some registry.
     *
     * The default implementation will look up the name in the registry of the {@link #getCamelContext()} property
     */
    protected Endpoint lookupEndpoint(String name, Map parameters) {
        return getCamelContext().getRegistry().lookup(name, Endpoint.class);
    }

}
