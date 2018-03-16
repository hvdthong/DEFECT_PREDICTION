package org.apache.camel.component.jt400;

import java.util.Map;

import org.apache.camel.CamelException;
import org.apache.camel.Component;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultComponent;

/**
 * {@link Component} to provide integration with AS/400 objects. 
 * 
 * Current implementation only supports working with data queues (*DTAQ)
 */
public class Jt400Component extends DefaultComponent<Exchange> {

    private static final String DATA_QUEUE = "DTAQ";

    @Override
    @SuppressWarnings("unchecked")
    protected Endpoint<Exchange> createEndpoint(String uri, String remaining, Map properties)
        throws Exception {
        String type = remaining.substring(remaining.lastIndexOf(".") + 1).toUpperCase();

        if (DATA_QUEUE.equals(type)) {
            return new Jt400DataQueueEndpoint(uri, this);
        }
        throw new CamelException(String.format("AS/400 Object type %s is not supported", type));
    }
}
