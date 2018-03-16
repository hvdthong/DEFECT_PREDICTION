package org.apache.camel.component.stream;

import java.util.Map;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultComponent;

/**
 * Component providing streams connectivity
 */
public class StreamComponent extends DefaultComponent<Exchange> {


    @Override
    protected Endpoint<Exchange> createEndpoint(String uri, String remaining, Map parameters)
        throws Exception {
        return new StreamEndpoint(uri, this);
    }

}
