package org.apache.camel.component.mock;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultComponent;

/**
 * A factory of {@link MockEndpoint} instances
 *
 * @version $Revision: 1.1 $
 */
public class MockComponent extends DefaultComponent<Exchange> {

    @Override
    protected Endpoint<Exchange> createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        return new MockEndpoint(uri, this);
    }
}
