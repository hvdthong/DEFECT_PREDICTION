package org.apache.camel.component.direct;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultComponent;

/**
 * Represents the component that manages {@link DirectEndpoint}. It holds the
 * list of named direct endpoints.
 *
 * @version $Revision: 640438 $
 */
public class DirectComponent<E extends Exchange> extends DefaultComponent<E> {

    protected Endpoint<E> createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        Endpoint<E> endpoint = new DirectEndpoint<E>(uri, this);
        setProperties(endpoint, parameters);
        return endpoint;
    }
}
